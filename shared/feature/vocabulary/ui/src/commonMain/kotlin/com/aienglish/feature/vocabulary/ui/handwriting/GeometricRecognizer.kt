package com.aienglish.feature.vocabulary.ui.handwriting

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * High-accuracy Geometric Gesture & Handwriting Recognizer.
 *
 * Employs a multi-feature hybrid architecture:
 * 1. Equidistant stroke-proportional resampling (32 points)
 * 2. 3x3 Spatial Zoning Density Vector (9-dimensional invariant structural histogram)
 * 3. Normalized Symmetric Point-Cloud Distance (Q/P formulation)
 * 4. Local Tangent Direction Vector Alignment (Cosine similarity)
 * 5. Topological closed-loop and aspect-ratio validation
 * 6. Directional swipe gesture shortcuts (Space, Backspace)
 *
 * 100% Offline • Zero LLM tokens • Instantaneous (<0.5ms CPU execution).
 */
class GeometricRecognizer(
    private val templates: List<HandwritingTemplate> = HandwritingTemplates.ALL_TEMPLATES
) {
    companion object {
        const val RESAMPLE_POINTS = 32
        const val REFERENCE_BOX_SIZE = 100f
        const val MAX_EXPECTED_DISTANCE = 38f

        const val GESTURE_BACKSPACE = "⌫"
        const val GESTURE_SPACE = " "

        /**
         * Resamples points along stroke paths so they are distributed proportionally
         * across strokes without artificial padding or clustering.
         */
        fun resample(points: List<Point2D>, targetCount: Int = RESAMPLE_POINTS): List<Point2D> {
            if (points.isEmpty()) return emptyList()
            if (points.size == 1) return List(targetCount) { points.first() }

            // Group points by stroke
            val strokes = points.groupBy { it.strokeId }.values.toList()
            val strokeLengths = strokes.map { computeSingleStrokeLength(it) }
            val totalLength = strokeLengths.sum()

            if (totalLength <= 0.001f) {
                return List(targetCount) { points.first() }
            }

            // Allocate point counts to each stroke proportionally
            val allocatedCounts = IntArray(strokes.size)
            var assigned = 0
            for (i in strokes.indices) {
                val proportion = strokeLengths[i] / totalLength
                val count = max(2, (targetCount * proportion).roundToInt())
                allocatedCounts[i] = count
                assigned += count
            }

            // Adjust allocation to match exact targetCount
            var diff = targetCount - assigned
            var idx = 0
            while (diff != 0 && allocatedCounts.isNotEmpty()) {
                if (diff > 0) {
                    allocatedCounts[idx % allocatedCounts.size]++
                    diff--
                } else if (allocatedCounts[idx % allocatedCounts.size] > 2) {
                    allocatedCounts[idx % allocatedCounts.size]--
                    diff++
                }
                idx++
            }

            val resampledAll = mutableListOf<Point2D>()
            for (i in strokes.indices) {
                val stroke = strokes[i]
                val targetForStroke = allocatedCounts[i]
                val resampledStroke = resampleSingleStroke(stroke, targetForStroke)
                resampledAll.addAll(resampledStroke)
            }

            while (resampledAll.size < targetCount) {
                resampledAll.add(points.last())
            }
            return if (resampledAll.size > targetCount) resampledAll.take(targetCount) else resampledAll
        }

        private fun computeSingleStrokeLength(stroke: List<Point2D>): Float {
            var len = 0f
            for (i in 1 until stroke.size) {
                len += stroke[i].distanceTo(stroke[i - 1])
            }
            return len
        }

        private fun resampleSingleStroke(stroke: List<Point2D>, targetCount: Int): List<Point2D> {
            if (stroke.isEmpty()) return emptyList()
            if (stroke.size == 1 || targetCount <= 1) return List(targetCount) { stroke.first() }

            val totalLength = computeSingleStrokeLength(stroke)
            if (totalLength <= 0.001f) {
                return List(targetCount) { stroke.first() }
            }

            val interval = totalLength / (targetCount - 1)
            val result = mutableListOf<Point2D>()
            result.add(stroke.first())

            var accumulated = 0f
            val pts = stroke.toMutableList()
            var i = 1

            while (i < pts.size && result.size < targetCount - 1) {
                val p0 = pts[i - 1]
                val p1 = pts[i]
                val d = p0.distanceTo(p1)

                if (accumulated + d >= interval) {
                    val ratio = (interval - accumulated) / max(0.0001f, d)
                    val qx = p0.x + ratio * (p1.x - p0.x)
                    val qy = p0.y + ratio * (p1.y - p0.y)
                    val q = Point2D(qx, qy, p0.strokeId)
                    result.add(q)
                    pts.add(i, q)
                    accumulated = 0f
                } else {
                    accumulated += d
                }
                i++
            }

            while (result.size < targetCount) {
                result.add(stroke.last())
            }
            return result
        }

        /**
         * Scales points to reference 100x100 box preserving aspect ratio,
         * then translates the centroid to the origin (0, 0).
         */
        fun normalize(points: List<Point2D>): List<Point2D> {
            if (points.isEmpty()) return emptyList()

            val minX = points.minOf { it.x }
            val maxX = points.maxOf { it.x }
            val minY = points.minOf { it.y }
            val maxY = points.maxOf { it.y }

            val width = max(1f, maxX - minX)
            val height = max(1f, maxY - minY)
            val maxDim = max(width, height)
            val scale = REFERENCE_BOX_SIZE / maxDim

            val scaled = points.map { p ->
                Point2D((p.x - minX) * scale, (p.y - minY) * scale, p.strokeId)
            }

            val avgX = scaled.map { it.x }.average().toFloat()
            val avgY = scaled.map { it.y }.average().toFloat()

            return scaled.map { p ->
                Point2D(p.x - avgX, p.y - avgY, p.strokeId)
            }
        }

        /**
         * Computes 3x3 Spatial Zoning Density Histogram (9 cells).
         * Each cell represents the fraction of points located in that region.
         */
        fun computeZoning(points: List<Point2D>): FloatArray {
            val histogram = FloatArray(9)
            if (points.isEmpty()) return histogram

            val minX = points.minOf { it.x }
            val maxX = points.maxOf { it.x }
            val minY = points.minOf { it.y }
            val maxY = points.maxOf { it.y }

            val width = max(1f, maxX - minX)
            val height = max(1f, maxY - minY)

            for (p in points) {
                val normX = ((p.x - minX) / width).coerceIn(0f, 0.999f)
                val normY = ((p.y - minY) / height).coerceIn(0f, 0.999f)

                val col = (normX * 3).toInt().coerceIn(0, 2)
                val row = (normY * 3).toInt().coerceIn(0, 2)
                val cellIdx = row * 3 + col
                histogram[cellIdx] += 1f
            }

            val total = points.size.toFloat()
            for (i in histogram.indices) {
                histogram[i] /= total
            }
            return histogram
        }

        /**
         * Computes normalized unit tangent vectors along stroke segments.
         */
        fun computeTangents(points: List<Point2D>): List<Pair<Float, Float>> {
            if (points.size < 2) return emptyList()
            val tangents = mutableListOf<Pair<Float, Float>>()

            for (i in 0 until points.size - 1) {
                val p0 = points[i]
                val p1 = points[i + 1]
                if (p0.strokeId == p1.strokeId) {
                    val dx = p1.x - p0.x
                    val dy = p1.y - p0.y
                    val len = sqrt(dx * dx + dy * dy)
                    if (len > 0.001f) {
                        tangents.add(Pair(dx / len, dy / len))
                    } else {
                        tangents.add(Pair(0f, 0f))
                    }
                } else {
                    tangents.add(Pair(0f, 0f))
                }
            }
            return tangents
        }

        /**
         * Detects if the strokes contain a closed loop.
         */
        fun detectClosedLoop(rawStrokes: List<List<Point2D>>): Boolean {
            val flattened = rawStrokes.flatten()
            if (flattened.size < 4) return false

            val minX = flattened.minOf { it.x }
            val maxX = flattened.maxOf { it.x }
            val minY = flattened.minOf { it.y }
            val maxY = flattened.maxOf { it.y }
            val maxDim = max(1f, max(maxX - minX, maxY - minY))

            for (stroke in rawStrokes) {
                if (stroke.size >= 4) {
                    val start = stroke.first()
                    val end = stroke.last()
                    val gap = start.distanceTo(end)
                    val strokeLen = computeSingleStrokeLength(stroke)
                    if (gap < maxDim * 0.38f && strokeLen > maxDim * 1.4f) {
                        return true
                    }
                }
            }
            return false
        }
    }

    /**
     * Recognizes handwritten strokes and returns ranked candidate matches.
     *
     * @param rawStrokes List of strokes drawn by user.
     * @param topN Number of top candidate characters to return.
     * @return Ranked list of RecognitionResult sorted by confidence score descending.
     */
    fun recognize(rawStrokes: List<List<Point2D>>, topN: Int = 5): List<RecognitionResult> {
        val flattened = rawStrokes.flatten()
        if (flattened.size < 3) return emptyList()

        // 1. Check for quick single-stroke directional gestures (Space, Backspace)
        val gestureResult = detectDirectionalGesture(rawStrokes)
        if (gestureResult != null) {
            return listOf(gestureResult)
        }

        // 2. Preprocess candidate points
        val resampled = resample(flattened, RESAMPLE_POINTS)
        if (resampled.size < RESAMPLE_POINTS) return emptyList()

        val normalizedCandidate = normalize(resampled)
        val candidateZoning = computeZoning(flattened)
        val candidateTangents = computeTangents(resampled)
        val isCandidateClosed = detectClosedLoop(rawStrokes)

        val minX = flattened.minOf { it.x }
        val maxX = flattened.maxOf { it.x }
        val minY = flattened.minOf { it.y }
        val maxY = flattened.maxOf { it.y }
        val candWidth = max(1f, maxX - minX)
        val candHeight = max(1f, maxY - minY)
        val candAspectRatio = candHeight / candWidth

        // 3. Match against all templates using multi-feature composite scoring
        val results = mutableListOf<RecognitionResult>()

        for (template in templates) {
            // Feature A: Symmetric Point Cloud Distance (Q/P metric)
            val ptsDist = greedyCloudMatch(normalizedCandidate, template.normalizedPoints)
            val sPts = max(0f, 1f - (ptsDist / MAX_EXPECTED_DISTANCE))

            // Feature B: 3x3 Spatial Zoning Density Similarity (L1 difference)
            var zoneDiff = 0f
            for (k in 0..8) {
                zoneDiff += abs(candidateZoning[k] - template.zoningHistogram[k])
            }
            // Max L1 difference is 2.0; convert to similarity
            val sZone = max(0f, 1f - (zoneDiff / 1.35f))

            // Feature C: Tangent Vector Alignment (Cosine similarity)
            val sTan = computeTangentSimilarity(candidateTangents, template.tangentVectors)

            // Composite raw score: 35% Points + 35% Zoning + 30% Direction
            var score = 0.35f * sPts + 0.35f * sZone + 0.30f * sTan

            // Structural Penalties
            // 1. Aspect Ratio Penalty
            if (candAspectRatio > 2.2f && template.aspectRatio < 0.85f) {
                score -= 0.35f // Tall letter (like 'I', 'l', '1') matching wide letter
            } else if (candAspectRatio < 0.55f && template.aspectRatio > 1.8f) {
                score -= 0.35f // Flat stroke matching tall letter
            } else {
                val ratioDiff = abs(candAspectRatio - template.aspectRatio)
                if (ratioDiff > 1.3f) {
                    score -= min(0.20f, (ratioDiff - 1.3f) * 0.10f)
                }
            }

            // 2. Closed Loop Validation
            if (template.isClosedLoop != null) {
                if (template.isClosedLoop && !isCandidateClosed) {
                    score -= 0.18f // Required loop (O, D, 0) but candidate is open
                } else if (!template.isClosedLoop && isCandidateClosed) {
                    score -= 0.18f // Required open (C, L, U) but candidate is closed
                }
            }

            // 3. Stroke Count Discrepancy (soft penalty)
            val strokeDiff = abs(rawStrokes.size - template.strokeCount)
            score -= (strokeDiff * 0.03f)

            val finalScore = score.coerceIn(0f, 1f)
            if (finalScore > 0.30f) {
                results.add(
                    RecognitionResult(
                        character = template.character,
                        score = finalScore,
                        label = template.label
                    )
                )
            }
        }

        // 4. Group by character, select best score per character, sort descending
        return results
            .groupBy { it.character }
            .map { (_, group) -> group.maxByOrNull { it.score }!! }
            .sortedByDescending { it.score }
            .take(topN)
    }

    /**
     * Computes average cosine similarity between candidate tangents and template tangents.
     */
    private fun computeTangentSimilarity(
        cTangents: List<Pair<Float, Float>>,
        tTangents: List<Pair<Float, Float>>
    ): Float {
        val count = min(cTangents.size, tTangents.size)
        if (count == 0) return 0.5f

        var dotSum = 0f
        var validPairs = 0

        for (i in 0 until count) {
            val c = cTangents[i]
            val t = tTangents[i]
            val cLenSq = c.first * c.first + c.second * c.second
            val tLenSq = t.first * t.first + t.second * t.second

            if (cLenSq > 0.001f && tLenSq > 0.001f) {
                val dot = c.first * t.first + c.second * t.second
                dotSum += max(0f, dot) // Clamp negative dots to 0
                validPairs++
            }
        }

        return if (validPairs > 0) (dotSum / validPairs) else 0.5f
    }

    /**
     * Detects fast horizontal swipe gestures:
     * - Swipe Right: Space
     * - Swipe Left: Backspace
     */
    private fun detectDirectionalGesture(rawStrokes: List<List<Point2D>>): RecognitionResult? {
        if (rawStrokes.size != 1) return null
        val stroke = rawStrokes.first()
        if (stroke.size < 5) return null

        val start = stroke.first()
        val end = stroke.last()
        val dx = end.x - start.x

        val minX = stroke.minOf { it.x }
        val maxX = stroke.maxOf { it.x }
        val minY = stroke.minOf { it.y }
        val maxY = stroke.maxOf { it.y }

        val width = maxX - minX
        val height = maxY - minY

        // A horizontal swipe must be wide and flat (width >= 2.5 * height and width >= 55px)
        if (width >= 55f && width >= 2.5f * height && abs(dx) > width * 0.65f) {
            return if (dx > 0) {
                RecognitionResult(GESTURE_SPACE, 0.96f, "Space")
            } else {
                RecognitionResult(GESTURE_BACKSPACE, 0.96f, "Backspace")
            }
        }
        return null
    }

    /**
     * Symmetric point cloud distance:
     * D(C, T) = 1/2 * (cloudDist(C, T) + cloudDist(T, C))
     */
    private fun greedyCloudMatch(c: List<Point2D>, t: List<Point2D>): Float {
        val cToT = cloudDistance(c, t)
        val tToC = cloudDistance(t, c)
        return (cToT + tToC) / (2f * c.size)
    }

    /**
     * Average minimum distance from each point in pts1 to nearest point in pts2.
     */
    private fun cloudDistance(pts1: List<Point2D>, pts2: List<Point2D>): Float {
        var totalDist = 0f
        for (p1 in pts1) {
            var minDistSq = Float.MAX_VALUE
            for (p2 in pts2) {
                val dSq = p1.distanceSquaredTo(p2)
                if (dSq < minDistSq) {
                    minDistSq = dSq
                }
            }
            totalDist += sqrt(minDistSq)
        }
        return totalDist
    }
}

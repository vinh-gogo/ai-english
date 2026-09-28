package com.aienglish.feature.vocabulary.ui.handwriting

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

/**
 * Geometric Gesture Recognizer implementing the $P Point-Cloud algorithm
 * with stroke geometry heuristics and aspect-ratio preservation.
 *
 * Runs 100% offline on client CPU (zero network, zero LLM tokens, ~1-2ms execution).
 */
class GeometricRecognizer(
    private val templates: List<HandwritingTemplate> = HandwritingTemplates.ALL_TEMPLATES
) {
    companion object {
        const val RESAMPLE_POINTS = 32
        const val REFERENCE_BOX_SIZE = 100f
        const val MAX_EXPECTED_DISTANCE = 42f

        const val GESTURE_BACKSPACE = "⌫"
        const val GESTURE_SPACE = " "

        /**
         * Resamples points along stroke paths so they are spaced at equidistant intervals.
         */
        fun resample(points: List<Point2D>, targetCount: Int): List<Point2D> {
            if (points.isEmpty()) return emptyList()
            val totalLength = computePathLength(points)
            if (totalLength <= 0.001f) {
                return List(targetCount) { points.first() }
            }

            val interval = totalLength / (targetCount - 1)
            val resampled = mutableListOf<Point2D>()
            resampled.add(points.first())

            var accumulatedDist = 0f
            val currentPoints = points.toMutableList()
            var i = 1

            while (i < currentPoints.size && resampled.size < targetCount - 1) {
                val p0 = currentPoints[i - 1]
                val p1 = currentPoints[i]

                // If moving between different strokes, do not interpolate across the air gap
                if (p0.strokeId != p1.strokeId) {
                    i++
                    continue
                }

                val d = p0.distanceTo(p1)
                if (accumulatedDist + d >= interval) {
                    val ratio = (interval - accumulatedDist) / d
                    val qx = p0.x + ratio * (p1.x - p0.x)
                    val qy = p0.y + ratio * (p1.y - p0.y)
                    val q = Point2D(qx, qy, p0.strokeId)
                    resampled.add(q)
                    currentPoints.add(i, q)
                    accumulatedDist = 0f
                } else {
                    accumulatedDist += d
                }
                i++
            }

            while (resampled.size < targetCount) {
                resampled.add(points.last())
            }

            return resampled
        }

        /**
         * Scales points to reference 100x100 box preserving aspect ratio,
         * then translates the centroid to the origin (0, 0).
         */
        fun normalize(points: List<Point2D>): List<Point2D> {
            if (points.isEmpty()) return emptyList()

            var minX = Float.MAX_VALUE
            var maxX = -Float.MAX_VALUE
            var minY = Float.MAX_VALUE
            var maxY = -Float.MAX_VALUE

            for (p in points) {
                if (p.x < minX) minX = p.x
                if (p.x > maxX) maxX = p.x
                if (p.y < minY) minY = p.y
                if (p.y > maxY) maxY = p.y
            }

            val width = max(1f, maxX - minX)
            val height = max(1f, maxY - minY)
            val maxDim = max(width, height)
            val scale = REFERENCE_BOX_SIZE / maxDim

            // Scale
            val scaled = points.map { p ->
                Point2D((p.x - minX) * scale, (p.y - minY) * scale, p.strokeId)
            }

            // Find centroid
            val avgX = scaled.map { it.x }.average().toFloat()
            val avgY = scaled.map { it.y }.average().toFloat()

            // Translate to origin
            return scaled.map { p ->
                Point2D(p.x - avgX, p.y - avgY, p.strokeId)
            }
        }

        /**
         * Computes total path length over all strokes.
         */
        private fun computePathLength(points: List<Point2D>): Float {
            var length = 0f
            for (i in 1 until points.size) {
                if (points[i].strokeId == points[i - 1].strokeId) {
                    length += points[i].distanceTo(points[i - 1])
                }
            }
            return length
        }
    }

    /**
     * Recognizes handwritten strokes and returns sorted candidate matches.
     *
     * @param rawStrokes List of strokes drawn by user (each stroke is a List of Point2D).
     * @param topN Number of top matches to return.
     * @return Ranked list of RecognitionResult sorted by confidence score descending.
     */
    fun recognize(rawStrokes: List<List<Point2D>>, topN: Int = 4): List<RecognitionResult> {
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

        val candidateFeatures = extractFeatures(flattened)
        val normalizedCandidate = normalize(resampled)

        // 3. Match against all templates using symmetric point cloud distance
        val results = mutableListOf<RecognitionResult>()

        for (template in templates) {
            val dist = greedyCloudMatch(normalizedCandidate, template.normalizedPoints)
            var score = max(0f, 1f - (dist / MAX_EXPECTED_DISTANCE))

            // Stroke count penalty (soft penalty: 4% per stroke discrepancy)
            val strokeDiff = abs(rawStrokes.size - template.strokeCount)
            score -= (strokeDiff * 0.04f)

            // Aspect ratio compatibility
            val ratioDiff = abs(candidateFeatures.aspectRatio - template.aspectRatio)
            if (ratioDiff > 1.2f) {
                score -= min(0.25f, (ratioDiff - 1.2f) * 0.12f)
            }

            // Closed loop heuristic (helps distinguish 'O' from 'C', 'D' from 'P' etc.)
            if (template.isClosedLoop != null) {
                if (template.isClosedLoop != candidateFeatures.isClosedLoop) {
                    score -= 0.15f
                }
            }

            val finalScore = score.coerceIn(0f, 1f)
            if (finalScore > 0.35f) {
                results.add(
                    RecognitionResult(
                        character = template.character,
                        score = finalScore,
                        label = template.label
                    )
                )
            }
        }

        // 4. Sort descending and deduplicate character results (keeping highest score)
        return results
            .groupBy { it.character }
            .map { (_, group) -> group.maxByOrNull { it.score }!! }
            .sortedByDescending { it.score }
            .take(topN)
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
        val dy = end.y - start.y

        val minX = stroke.minOf { it.x }
        val maxX = stroke.maxOf { it.x }
        val minY = stroke.minOf { it.y }
        val maxY = stroke.maxOf { it.y }

        val width = maxX - minX
        val height = maxY - minY

        // A horizontal swipe must be wide and flat (width >= 2.5 * height and width >= 60px)
        if (width >= 60f && width >= 2.5f * height && abs(dx) > width * 0.7f) {
            return if (dx > 0) {
                RecognitionResult(GESTURE_SPACE, 0.95f, "Space")
            } else {
                RecognitionResult(GESTURE_BACKSPACE, 0.95f, "Backspace")
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

    private fun extractFeatures(points: List<Point2D>): GestureFeatures {
        val minX = points.minOf { it.x }
        val maxX = points.maxOf { it.x }
        val minY = points.minOf { it.y }
        val maxY = points.maxOf { it.y }

        val width = max(1f, maxX - minX)
        val height = max(1f, maxY - minY)
        val aspectRatio = height / width

        // Closed loop check: start and end of first stroke are close relative to bounding box
        val first = points.first()
        val endOfFirst = points.lastOrNull { it.strokeId == first.strokeId } ?: points.last()
        val closeDistance = first.distanceTo(endOfFirst)
        val isClosed = closeDistance < (max(width, height) * 0.32f)

        return GestureFeatures(
            aspectRatio = aspectRatio,
            isClosedLoop = isClosed
        )
    }

    private data class GestureFeatures(
        val aspectRatio: Float,
        val isClosedLoop: Boolean
    )
}

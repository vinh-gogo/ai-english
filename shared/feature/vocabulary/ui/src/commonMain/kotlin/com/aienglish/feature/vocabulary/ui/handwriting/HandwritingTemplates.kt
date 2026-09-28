package com.aienglish.feature.vocabulary.ui.handwriting

import kotlin.math.max

/**
 * Pre-compiled canonical templates for English letters, digits, and gestures.
 */
data class HandwritingTemplate(
    val character: String,
    val label: String,
    val strokeCount: Int,
    val aspectRatio: Float,
    val isClosedLoop: Boolean?,
    val normalizedPoints: List<Point2D>
)

object HandwritingTemplates {

    private fun createTemplate(
        character: String,
        label: String = character,
        rawStrokes: List<List<Pair<Float, Float>>>,
        isClosed: Boolean? = null
    ): HandwritingTemplate {
        val densePoints = mutableListOf<Point2D>()
        rawStrokes.forEachIndexed { strokeIdx, strokeAnchors ->
            if (strokeAnchors.isEmpty()) return@forEachIndexed
            if (strokeAnchors.size == 1) {
                val (x, y) = strokeAnchors[0]
                densePoints.add(Point2D(x, y, strokeIdx))
            } else {
                for (i in 1 until strokeAnchors.size) {
                    val (x0, y0) = strokeAnchors[i - 1]
                    val (x1, y1) = strokeAnchors[i]
                    val dx = x1 - x0
                    val dy = y1 - y0
                    val dist = kotlin.math.sqrt(dx * dx + dy * dy)
                    val steps = max(1, (dist / 3f).toInt())
                    for (step in 0 until steps) {
                        val t = step.toFloat() / steps
                        densePoints.add(Point2D(x0 + t * dx, y0 + t * dy, strokeIdx))
                    }
                }
                val last = strokeAnchors.last()
                densePoints.add(Point2D(last.first, last.second, strokeIdx))
            }
        }

        val minX = densePoints.minOf { it.x }
        val maxX = densePoints.maxOf { it.x }
        val minY = densePoints.minOf { it.y }
        val maxY = densePoints.maxOf { it.y }
        val w = max(1f, maxX - minX)
        val h = max(1f, maxY - minY)
        val aspectRatio = h / w

        val resampled = GeometricRecognizer.resample(densePoints, GeometricRecognizer.RESAMPLE_POINTS)
        val normalized = GeometricRecognizer.normalize(resampled)

        return HandwritingTemplate(
            character = character,
            label = label,
            strokeCount = rawStrokes.size,
            aspectRatio = aspectRatio,
            isClosedLoop = isClosed,
            normalizedPoints = normalized
        )
    }

    val ALL_TEMPLATES: List<HandwritingTemplate> by lazy {
        listOf(
            // === UPPERCASE A-Z ===
            createTemplate("A", "A", listOf(
                listOf(15f to 100f, 50f to 0f, 85f to 100f),
                listOf(30f to 60f, 70f to 60f)
            )),
            createTemplate("B", "B", listOf(
                listOf(20f to 0f, 20f to 100f),
                listOf(20f to 0f, 65f to 0f, 75f to 25f, 65f to 50f, 20f to 50f),
                listOf(20f to 50f, 70f to 50f, 80f to 75f, 70f to 100f, 20f to 100f)
            )),
            createTemplate("C", "C", listOf(
                listOf(80f to 18f, 50f to 0f, 20f to 30f, 20f to 70f, 50f to 100f, 80f to 82f)
            ), isClosed = false),
            createTemplate("D", "D", listOf(
                listOf(20f to 0f, 20f to 100f),
                listOf(20f to 0f, 65f to 0f, 80f to 50f, 65f to 100f, 20f to 100f)
            ), isClosed = true),
            createTemplate("E", "E", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 0f, 75f to 0f),
                listOf(25f to 50f, 65f to 50f),
                listOf(25f to 100f, 75f to 100f)
            )),
            createTemplate("E", "E_single", listOf(
                listOf(75f to 0f, 25f to 0f, 25f to 50f, 65f to 50f, 25f to 50f, 25f to 100f, 75f to 100f)
            )),
            createTemplate("F", "F", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 0f, 75f to 0f),
                listOf(25f to 50f, 65f to 50f)
            )),
            createTemplate("G", "G", listOf(
                listOf(80f to 20f, 50f to 0f, 20f to 30f, 20f to 70f, 50f to 100f, 80f to 80f, 80f to 55f, 55f to 55f)
            )),
            createTemplate("H", "H", listOf(
                listOf(20f to 0f, 20f to 100f),
                listOf(80f to 0f, 80f to 100f),
                listOf(20f to 50f, 80f to 50f)
            )),
            createTemplate("I", "I", listOf(
                listOf(50f to 0f, 50f to 100f)
            )),
            createTemplate("I", "I_serif", listOf(
                listOf(30f to 0f, 70f to 0f),
                listOf(50f to 0f, 50f to 100f),
                listOf(30f to 100f, 70f to 100f)
            )),
            createTemplate("J", "J", listOf(
                listOf(70f to 0f, 70f to 75f, 50f to 100f, 25f to 85f, 20f to 65f)
            )),
            createTemplate("K", "K", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(75f to 10f, 25f to 55f),
                listOf(30f to 50f, 75f to 100f)
            )),
            createTemplate("L", "L", listOf(
                listOf(25f to 0f, 25f to 100f, 75f to 100f)
            )),
            createTemplate("M", "M", listOf(
                listOf(15f to 100f, 15f to 0f, 50f to 60f, 85f to 0f, 85f to 100f)
            )),
            createTemplate("N", "N", listOf(
                listOf(20f to 100f, 20f to 0f, 80f to 100f, 80f to 0f)
            )),
            createTemplate("O", "O", listOf(
                listOf(50f to 0f, 20f to 25f, 20f to 75f, 50f to 100f, 80f to 75f, 80f to 25f, 50f to 0f)
            ), isClosed = true),
            createTemplate("P", "P", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 0f, 70f to 0f, 80f to 25f, 70f to 50f, 25f to 50f)
            )),
            createTemplate("Q", "Q", listOf(
                listOf(50f to 0f, 20f to 25f, 20f to 75f, 50f to 100f, 80f to 75f, 80f to 25f, 50f to 0f),
                listOf(60f to 70f, 90f to 100f)
            ), isClosed = true),
            createTemplate("R", "R", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 0f, 70f to 0f, 80f to 25f, 70f to 50f, 25f to 50f),
                listOf(45f to 50f, 80f to 100f)
            )),
            createTemplate("S", "S", listOf(
                listOf(80f to 15f, 50f to 0f, 25f to 25f, 50f to 50f, 75f to 75f, 50f to 100f, 20f to 85f)
            )),
            createTemplate("T", "T", listOf(
                listOf(15f to 0f, 85f to 0f),
                listOf(50f to 0f, 50f to 100f)
            )),
            createTemplate("U", "U", listOf(
                listOf(20f to 0f, 20f to 70f, 50f to 100f, 80f to 70f, 80f to 0f)
            )),
            createTemplate("V", "V", listOf(
                listOf(20f to 0f, 50f to 100f, 80f to 0f)
            )),
            createTemplate("W", "W", listOf(
                listOf(15f to 0f, 30f to 100f, 50f to 45f, 70f to 100f, 85f to 0f)
            )),
            createTemplate("X", "X", listOf(
                listOf(20f to 0f, 80f to 100f),
                listOf(80f to 0f, 20f to 100f)
            )),
            createTemplate("Y", "Y", listOf(
                listOf(20f to 0f, 50f to 50f),
                listOf(80f to 0f, 50f to 50f),
                listOf(50f to 50f, 50f to 100f)
            )),
            createTemplate("Z", "Z", listOf(
                listOf(20f to 0f, 80f to 0f, 20f to 100f, 80f to 100f)
            )),

            // === LOWERCASE a-z ===
            createTemplate("a", "a", listOf(
                listOf(75f to 30f, 50f to 15f, 25f to 35f, 25f to 65f, 50f to 90f, 75f to 75f, 75f to 15f, 75f to 95f)
            )),
            createTemplate("b", "b", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 50f, 50f to 35f, 75f to 50f, 75f to 85f, 50f to 100f, 25f to 95f)
            )),
            createTemplate("c", "c", listOf(
                listOf(75f to 25f, 50f to 15f, 25f to 45f, 25f to 75f, 50f to 95f, 75f to 80f)
            ), isClosed = false),
            createTemplate("d", "d", listOf(
                listOf(75f to 50f, 50f to 35f, 25f to 50f, 25f to 85f, 50f to 100f, 75f to 95f),
                listOf(75f to 0f, 75f to 100f)
            )),
            createTemplate("e", "e", listOf(
                listOf(25f to 60f, 75f to 60f, 75f to 30f, 50f to 15f, 25f to 40f, 25f to 80f, 50f to 100f, 75f to 85f)
            )),
            createTemplate("f", "f", listOf(
                listOf(70f to 15f, 50f to 0f, 35f to 25f, 35f to 100f),
                listOf(20f to 45f, 55f to 45f)
            )),
            createTemplate("g", "g", listOf(
                listOf(75f to 45f, 50f to 30f, 25f to 50f, 25f to 75f, 50f to 90f, 75f to 85f),
                listOf(75f to 35f, 75f to 105f, 50f to 125f, 25f to 110f)
            )),
            createTemplate("h", "h", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 50f, 50f to 35f, 75f to 55f, 75f to 100f)
            )),
            createTemplate("i", "i", listOf(
                listOf(50f to 35f, 50f to 100f)
            )),
            createTemplate("j", "j", listOf(
                listOf(60f to 35f, 60f to 105f, 45f to 125f, 25f to 110f)
            )),
            createTemplate("k", "k", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(70f to 35f, 25f to 70f),
                listOf(35f to 60f, 70f to 100f)
            )),
            createTemplate("l", "l", listOf(
                listOf(50f to 0f, 50f to 90f, 65f to 100f)
            )),
            createTemplate("m", "m", listOf(
                listOf(20f to 35f, 20f to 100f),
                listOf(20f to 55f, 35f to 35f, 50f to 55f, 50f to 100f),
                listOf(50f to 55f, 65f to 35f, 80f to 55f, 80f to 100f)
            )),
            createTemplate("n", "n", listOf(
                listOf(25f to 35f, 25f to 100f),
                listOf(25f to 55f, 50f to 35f, 75f to 55f, 75f to 100f)
            )),
            createTemplate("o", "o", listOf(
                listOf(50f to 30f, 25f to 50f, 25f to 80f, 50f to 100f, 75f to 80f, 75f to 50f, 50f to 30f)
            ), isClosed = true),
            createTemplate("p", "p", listOf(
                listOf(25f to 25f, 25f to 120f),
                listOf(25f to 45f, 50f to 30f, 75f to 45f, 75f to 80f, 50f to 95f, 25f to 90f)
            )),
            createTemplate("q", "q", listOf(
                listOf(75f to 45f, 50f to 30f, 25f to 45f, 25f to 80f, 50f to 95f, 75f to 90f),
                listOf(75f to 25f, 75f to 120f)
            )),
            createTemplate("r", "r", listOf(
                listOf(30f to 35f, 30f to 100f),
                listOf(30f to 55f, 50f to 35f, 70f to 40f)
            )),
            createTemplate("s", "s", listOf(
                listOf(75f to 35f, 50f to 20f, 30f to 40f, 50f to 60f, 70f to 80f, 50f to 100f, 25f to 85f)
            )),
            createTemplate("t", "t", listOf(
                listOf(45f to 10f, 45f to 90f, 60f to 100f),
                listOf(25f to 35f, 65f to 35f)
            )),
            createTemplate("u", "u", listOf(
                listOf(25f to 35f, 25f to 80f, 50f to 100f, 75f to 80f, 75f to 35f),
                listOf(75f to 35f, 75f to 100f)
            )),
            createTemplate("v", "v", listOf(
                listOf(25f to 35f, 50f to 100f, 75f to 35f)
            )),
            createTemplate("w", "w", listOf(
                listOf(15f to 35f, 30f to 100f, 50f to 60f, 70f to 100f, 85f to 35f)
            )),
            createTemplate("x", "x", listOf(
                listOf(25f to 35f, 75f to 100f),
                listOf(75f to 35f, 25f to 100f)
            )),
            createTemplate("y", "y", listOf(
                listOf(25f to 35f, 50f to 75f),
                listOf(75f to 35f, 30f to 125f)
            )),
            createTemplate("z", "z", listOf(
                listOf(25f to 35f, 75f to 35f, 25f to 100f, 75f to 100f)
            )),

            // === DIGITS 0-9 ===
            createTemplate("0", "0", listOf(
                listOf(50f to 0f, 20f to 25f, 20f to 75f, 50f to 100f, 80f to 75f, 80f to 25f, 50f to 0f)
            ), isClosed = true),
            createTemplate("1", "1", listOf(
                listOf(35f to 20f, 50f to 0f, 50f to 100f)
            )),
            createTemplate("2", "2", listOf(
                listOf(25f to 25f, 50f to 0f, 75f to 25f, 75f to 45f, 25f to 100f, 75f to 100f)
            )),
            createTemplate("3", "3", listOf(
                listOf(25f to 15f, 75f to 15f, 45f to 50f, 75f to 75f, 50f to 100f, 25f to 85f)
            )),
            createTemplate("4", "4", listOf(
                listOf(70f to 0f, 70f to 100f),
                listOf(70f to 10f, 20f to 65f, 85f to 65f)
            )),
            createTemplate("5", "5", listOf(
                listOf(75f to 0f, 30f to 0f, 30f to 45f, 75f to 55f, 75f to 85f, 50f to 100f, 25f to 90f)
            )),
            createTemplate("6", "6", listOf(
                listOf(75f to 20f, 50f to 0f, 25f to 35f, 25f to 75f, 50f to 100f, 75f to 80f, 75f to 60f, 50f to 50f, 25f to 60f)
            )),
            createTemplate("7", "7", listOf(
                listOf(20f to 0f, 80f to 0f, 40f to 100f)
            )),
            createTemplate("8", "8", listOf(
                listOf(50f to 0f, 25f to 25f, 75f to 75f, 50f to 100f, 25f to 75f, 75f to 25f, 50f to 0f)
            ), isClosed = true),
            createTemplate("9", "9", listOf(
                listOf(50f to 50f, 25f to 40f, 25f to 20f, 50f to 0f, 75f to 25f, 75f to 70f, 50f to 100f, 25f to 90f)
            ))
        )
    }
}

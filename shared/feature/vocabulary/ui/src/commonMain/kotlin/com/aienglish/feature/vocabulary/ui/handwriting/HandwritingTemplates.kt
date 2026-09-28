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
    val normalizedPoints: List<Point2D>,
    val zoningHistogram: FloatArray,
    val tangentVectors: List<Pair<Float, Float>>
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
        val zoning = GeometricRecognizer.computeZoning(densePoints)
        val tangents = GeometricRecognizer.computeTangents(resampled)

        return HandwritingTemplate(
            character = character,
            label = label,
            strokeCount = rawStrokes.size,
            aspectRatio = aspectRatio,
            isClosedLoop = isClosed,
            normalizedPoints = normalized,
            zoningHistogram = zoning,
            tangentVectors = tangents
        )
    }

    val ALL_TEMPLATES: List<HandwritingTemplate> by lazy {
        listOf(
            // === UPPERCASE A-Z ===
            // 'A' - 2-stroke style (inverted V then crossbar)
            createTemplate("A", "A_2stroke", listOf(
                listOf(15f to 100f, 50f to 0f, 85f to 100f),
                listOf(30f to 60f, 70f to 60f)
            )),
            // 'A' - 3-stroke style (left leg, right leg, crossbar)
            createTemplate("A", "A_3stroke", listOf(
                listOf(15f to 100f, 50f to 0f),
                listOf(50f to 0f, 85f to 100f),
                listOf(30f to 60f, 70f to 60f)
            )),
            // 'B' - 2-stroke style (vertical backbone, double loop)
            createTemplate("B", "B_2stroke", listOf(
                listOf(20f to 0f, 20f to 100f),
                listOf(20f to 0f, 65f to 0f, 75f to 25f, 65f to 50f, 20f to 50f, 70f to 50f, 80f to 75f, 70f to 100f, 20f to 100f)
            )),
            // 'B' - 3-stroke style (vertical backbone, top loop, bot loop)
            createTemplate("B", "B_3stroke", listOf(
                listOf(20f to 0f, 20f to 100f),
                listOf(20f to 0f, 65f to 0f, 75f to 25f, 65f to 50f, 20f to 50f),
                listOf(20f to 50f, 70f to 50f, 80f to 75f, 70f to 100f, 20f to 100f)
            )),
            // 'C' - 1-stroke counterclockwise open arc
            createTemplate("C", "C", listOf(
                listOf(80f to 18f, 50f to 0f, 20f to 30f, 20f to 70f, 50f to 100f, 80f to 82f)
            ), isClosed = false),
            // 'D' - 2-stroke style (vertical line then right curve)
            createTemplate("D", "D_2stroke", listOf(
                listOf(20f to 0f, 20f to 100f),
                listOf(20f to 0f, 65f to 0f, 80f to 50f, 65f to 100f, 20f to 100f)
            ), isClosed = true),
            // 'D' - 1-stroke style (down, trace up, right curve)
            createTemplate("D", "D_1stroke", listOf(
                listOf(20f to 0f, 20f to 100f, 20f to 0f, 65f to 0f, 80f to 50f, 65f to 100f, 20f to 100f)
            ), isClosed = true),
            // 'E' - 4-stroke style
            createTemplate("E", "E_4stroke", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 0f, 75f to 0f),
                listOf(25f to 50f, 65f to 50f),
                listOf(25f to 100f, 75f to 100f)
            )),
            // 'E' - 2-stroke style (top, down, bot in one stroke, then mid bar)
            createTemplate("E", "E_2stroke", listOf(
                listOf(75f to 0f, 25f to 0f, 25f to 100f, 75f to 100f),
                listOf(25f to 50f, 65f to 50f)
            )),
            // 'F' - 3-stroke style
            createTemplate("F", "F", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 0f, 75f to 0f),
                listOf(25f to 50f, 65f to 50f)
            )),
            // 'G' - 1-stroke style
            createTemplate("G", "G", listOf(
                listOf(80f to 20f, 50f to 0f, 20f to 30f, 20f to 70f, 50f to 100f, 80f to 80f, 80f to 55f, 55f to 55f)
            )),
            // 'H' - 3-stroke style
            createTemplate("H", "H_3stroke", listOf(
                listOf(20f to 0f, 20f to 100f),
                listOf(80f to 0f, 80f to 100f),
                listOf(20f to 50f, 80f to 50f)
            )),
            // 'I' - 1-stroke vertical bar
            createTemplate("I", "I", listOf(
                listOf(50f to 0f, 50f to 100f)
            )),
            // 'I' - 3-stroke with serifs
            createTemplate("I", "I_serif", listOf(
                listOf(30f to 0f, 70f to 0f),
                listOf(50f to 0f, 50f to 100f),
                listOf(30f to 100f, 70f to 100f)
            )),
            // 'J' - 1-stroke
            createTemplate("J", "J", listOf(
                listOf(70f to 0f, 70f to 75f, 50f to 100f, 25f to 85f, 20f to 65f)
            )),
            // 'K' - 3-stroke
            createTemplate("K", "K_3stroke", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(75f to 10f, 25f to 55f),
                listOf(30f to 50f, 75f to 100f)
            )),
            // 'K' - 2-stroke
            createTemplate("K", "K_2stroke", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(75f to 10f, 25f to 55f, 75f to 100f)
            )),
            // 'L' - 1-stroke
            createTemplate("L", "L", listOf(
                listOf(25f to 0f, 25f to 100f, 75f to 100f)
            )),
            // 'M' - 1-stroke (start bottom-left, up, down, up, down)
            createTemplate("M", "M_1stroke", listOf(
                listOf(15f to 100f, 15f to 0f, 50f to 60f, 85f to 0f, 85f to 100f)
            )),
            // 'N' - 1-stroke (start bottom-left, up, diagonal down, up)
            createTemplate("N", "N_1stroke", listOf(
                listOf(20f to 100f, 20f to 0f, 80f to 100f, 80f to 0f)
            )),
            // 'N' - 3-stroke
            createTemplate("N", "N_3stroke", listOf(
                listOf(20f to 100f, 20f to 0f),
                listOf(20f to 0f, 80f to 100f),
                listOf(80f to 100f, 80f to 0f)
            )),
            // 'O' - 1-stroke closed circle
            createTemplate("O", "O", listOf(
                listOf(50f to 0f, 20f to 25f, 20f to 75f, 50f to 100f, 80f to 75f, 80f to 25f, 50f to 0f)
            ), isClosed = true),
            // 'P' - 2-stroke
            createTemplate("P", "P_2stroke", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 0f, 70f to 0f, 80f to 25f, 70f to 50f, 25f to 50f)
            )),
            // 'P' - 1-stroke
            createTemplate("P", "P_1stroke", listOf(
                listOf(25f to 100f, 25f to 0f, 70f to 0f, 80f to 25f, 70f to 50f, 25f to 50f)
            )),
            // 'Q' - 2-stroke
            createTemplate("Q", "Q", listOf(
                listOf(50f to 0f, 20f to 25f, 20f to 75f, 50f to 100f, 80f to 75f, 80f to 25f, 50f to 0f),
                listOf(60f to 70f, 90f to 100f)
            ), isClosed = true),
            // 'R' - 3-stroke
            createTemplate("R", "R_3stroke", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 0f, 70f to 0f, 80f to 25f, 70f to 50f, 25f to 50f),
                listOf(45f to 50f, 80f to 100f)
            )),
            // 'R' - 2-stroke
            createTemplate("R", "R_2stroke", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 0f, 70f to 0f, 80f to 25f, 70f to 50f, 25f to 50f, 80f to 100f)
            )),
            // 'S' - 1-stroke
            createTemplate("S", "S", listOf(
                listOf(80f to 15f, 50f to 0f, 25f to 25f, 50f to 50f, 75f to 75f, 50f to 100f, 20f to 85f)
            )),
            // 'T' - 2-stroke (top bar first, vertical down)
            createTemplate("T", "T_topFirst", listOf(
                listOf(15f to 0f, 85f to 0f),
                listOf(50f to 0f, 50f to 100f)
            )),
            // 'T' - 2-stroke (vertical down first, top bar)
            createTemplate("T", "T_vertFirst", listOf(
                listOf(50f to 0f, 50f to 100f),
                listOf(15f to 0f, 85f to 0f)
            )),
            // 'U' - 1-stroke
            createTemplate("U", "U", listOf(
                listOf(20f to 0f, 20f to 70f, 50f to 100f, 80f to 70f, 80f to 0f)
            )),
            // 'V' - 1-stroke
            createTemplate("V", "V", listOf(
                listOf(20f to 0f, 50f to 100f, 80f to 0f)
            )),
            // 'W' - 1-stroke
            createTemplate("W", "W", listOf(
                listOf(15f to 0f, 30f to 100f, 50f to 45f, 70f to 100f, 85f to 0f)
            )),
            // 'X' - 2-stroke (\ then /)
            createTemplate("X", "X_slash1", listOf(
                listOf(20f to 0f, 80f to 100f),
                listOf(80f to 0f, 20f to 100f)
            )),
            // 'X' - 2-stroke (/ then \)
            createTemplate("X", "X_slash2", listOf(
                listOf(80f to 0f, 20f to 100f),
                listOf(20f to 0f, 80f to 100f)
            )),
            // 'Y' - 2-stroke
            createTemplate("Y", "Y_2stroke", listOf(
                listOf(20f to 0f, 50f to 50f),
                listOf(80f to 0f, 20f to 100f)
            )),
            // 'Y' - 3-stroke
            createTemplate("Y", "Y_3stroke", listOf(
                listOf(20f to 0f, 50f to 50f),
                listOf(80f to 0f, 50f to 50f),
                listOf(50f to 50f, 50f to 100f)
            )),
            // 'Z' - 1-stroke
            createTemplate("Z", "Z", listOf(
                listOf(20f to 0f, 80f to 0f, 20f to 100f, 80f to 100f)
            )),

            // === LOWERCASE a-z ===
            // 'a' - 1-stroke (circle with vertical tail)
            createTemplate("a", "a_1stroke", listOf(
                listOf(75f to 30f, 50f to 15f, 25f to 35f, 25f to 65f, 50f to 90f, 75f to 75f, 75f to 15f, 75f to 95f)
            )),
            // 'a' - 2-stroke (circle, then right vertical bar)
            createTemplate("a", "a_2stroke", listOf(
                listOf(75f to 30f, 50f to 15f, 25f to 35f, 25f to 65f, 50f to 90f, 75f to 75f, 75f to 30f),
                listOf(75f to 20f, 75f to 95f)
            )),
            // 'b' - 1-stroke (down, trace up, right loop)
            createTemplate("b", "b_1stroke", listOf(
                listOf(25f to 0f, 25f to 100f, 25f to 50f, 50f to 35f, 75f to 50f, 75f to 85f, 50f to 100f, 25f to 95f)
            )),
            // 'b' - 2-stroke (down line, then loop)
            createTemplate("b", "b_2stroke", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 50f, 50f to 35f, 75f to 50f, 75f to 85f, 50f to 100f, 25f to 95f)
            )),
            // 'c' - 1-stroke
            createTemplate("c", "c", listOf(
                listOf(75f to 25f, 50f to 15f, 25f to 45f, 25f to 75f, 50f to 95f, 75f to 80f)
            ), isClosed = false),
            // 'd' - 2-stroke (left circle, then tall right vertical)
            createTemplate("d", "d_2stroke", listOf(
                listOf(75f to 50f, 50f to 35f, 25f to 50f, 25f to 85f, 50f to 100f, 75f to 95f),
                listOf(75f to 0f, 75f to 100f)
            )),
            // 'e' - 1-stroke
            createTemplate("e", "e", listOf(
                listOf(25f to 60f, 75f to 60f, 75f to 30f, 50f to 15f, 25f to 40f, 25f to 80f, 50f to 100f, 75f to 85f)
            )),
            // 'f' - 2-stroke (hook down, crossbar)
            createTemplate("f", "f", listOf(
                listOf(70f to 15f, 50f to 0f, 35f to 25f, 35f to 100f),
                listOf(20f to 45f, 55f to 45f)
            )),
            // 'g' - 2-stroke (circle, then hook down-left)
            createTemplate("g", "g_2stroke", listOf(
                listOf(75f to 45f, 50f to 30f, 25f to 50f, 25f to 75f, 50f to 90f, 75f to 85f),
                listOf(75f to 35f, 75f to 105f, 50f to 125f, 25f to 110f)
            )),
            // 'h' - 2-stroke (tall line down, then arch)
            createTemplate("h", "h_2stroke", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(25f to 50f, 50f to 35f, 75f to 55f, 75f to 100f)
            )),
            // 'h' - 1-stroke
            createTemplate("h", "h_1stroke", listOf(
                listOf(25f to 0f, 25f to 100f, 25f to 50f, 50f to 35f, 75f to 55f, 75f to 100f)
            )),
            // 'i' - 2-stroke (short vertical, dot)
            createTemplate("i", "i_dot", listOf(
                listOf(50f to 35f, 50f to 100f),
                listOf(50f to 10f, 50f to 15f)
            )),
            // 'i' - 1-stroke (short vertical)
            createTemplate("i", "i_plain", listOf(
                listOf(50f to 35f, 50f to 100f)
            )),
            // 'j' - 2-stroke (descender hook, dot)
            createTemplate("j", "j_dot", listOf(
                listOf(60f to 35f, 60f to 105f, 45f to 125f, 25f to 110f),
                listOf(60f to 10f, 60f to 15f)
            )),
            // 'j' - 1-stroke
            createTemplate("j", "j_plain", listOf(
                listOf(60f to 35f, 60f to 105f, 45f to 125f, 25f to 110f)
            )),
            // 'k' - 2-stroke
            createTemplate("k", "k", listOf(
                listOf(25f to 0f, 25f to 100f),
                listOf(70f to 35f, 25f to 70f, 70f to 100f)
            )),
            // 'l' - 1-stroke (straight down)
            createTemplate("l", "l_straight", listOf(
                listOf(50f to 0f, 50f to 100f)
            )),
            // 'l' - 1-stroke (down with slight right tail)
            createTemplate("l", "l_hook", listOf(
                listOf(50f to 0f, 50f to 90f, 65f to 100f)
            )),
            // 'm' - 1-stroke
            createTemplate("m", "m", listOf(
                listOf(20f to 35f, 20f to 100f, 20f to 55f, 35f to 35f, 50f to 55f, 50f to 100f, 50f to 55f, 65f to 35f, 80f to 55f, 80f to 100f)
            )),
            // 'n' - 1-stroke
            createTemplate("n", "n", listOf(
                listOf(25f to 35f, 25f to 100f, 25f to 55f, 50f to 35f, 75f to 55f, 75f to 100f)
            )),
            // 'o' - 1-stroke closed loop
            createTemplate("o", "o", listOf(
                listOf(50f to 30f, 25f to 50f, 25f to 80f, 50f to 100f, 75f to 80f, 75f to 50f, 50f to 30f)
            ), isClosed = true),
            // 'p' - 2-stroke
            createTemplate("p", "p_2stroke", listOf(
                listOf(25f to 25f, 25f to 120f),
                listOf(25f to 45f, 50f to 30f, 75f to 45f, 75f to 80f, 50f to 95f, 25f to 90f)
            )),
            // 'q' - 2-stroke
            createTemplate("q", "q", listOf(
                listOf(75f to 45f, 50f to 30f, 25f to 45f, 25f to 80f, 50f to 95f, 75f to 90f),
                listOf(75f to 25f, 75f to 120f)
            )),
            // 'r' - 1-stroke
            createTemplate("r", "r", listOf(
                listOf(30f to 35f, 30f to 100f, 30f to 55f, 50f to 35f, 70f to 40f)
            )),
            // 's' - 1-stroke
            createTemplate("s", "s", listOf(
                listOf(75f to 35f, 50f to 20f, 30f to 40f, 50f to 60f, 70f to 80f, 50f to 100f, 25f to 85f)
            )),
            // 't' - 2-stroke (down with slight right hook, then crossbar)
            createTemplate("t", "t_2strokeHook", listOf(
                listOf(45f to 10f, 45f to 90f, 60f to 100f),
                listOf(25f to 35f, 65f to 35f)
            )),
            // 't' - 2-stroke (straight down, then crossbar)
            createTemplate("t", "t_2strokeStraight", listOf(
                listOf(45f to 10f, 45f to 100f),
                listOf(25f to 35f, 65f to 35f)
            )),
            // 'u' - 1-stroke
            createTemplate("u", "u", listOf(
                listOf(25f to 35f, 25f to 80f, 50f to 100f, 75f to 80f, 75f to 35f, 75f to 100f)
            )),
            // 'v' - 1-stroke
            createTemplate("v", "v", listOf(
                listOf(25f to 35f, 50f to 100f, 75f to 35f)
            )),
            // 'w' - 1-stroke
            createTemplate("w", "w", listOf(
                listOf(15f to 35f, 30f to 100f, 50f to 60f, 70f to 100f, 85f to 35f)
            )),
            // 'x' - 2-stroke
            createTemplate("x", "x_slash1", listOf(
                listOf(25f to 35f, 75f to 100f),
                listOf(75f to 35f, 25f to 100f)
            )),
            createTemplate("x", "x_slash2", listOf(
                listOf(75f to 35f, 25f to 100f),
                listOf(25f to 35f, 75f to 100f)
            )),
            // 'y' - 2-stroke (u shape, descender tail)
            createTemplate("y", "y_2stroke", listOf(
                listOf(25f to 35f, 25f to 75f, 50f to 75f, 75f to 35f),
                listOf(75f to 35f, 35f to 125f)
            )),
            // 'y' - 2-stroke (slanted lines)
            createTemplate("y", "y_slanted", listOf(
                listOf(25f to 35f, 50f to 75f),
                listOf(75f to 35f, 30f to 125f)
            )),
            // 'z' - 1-stroke
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
            createTemplate("1", "1_straight", listOf(
                listOf(50f to 0f, 50f to 100f)
            )),
            createTemplate("2", "2", listOf(
                listOf(25f to 25f, 50f to 0f, 75f to 25f, 75f to 45f, 25f to 100f, 75f to 100f)
            )),
            createTemplate("3", "3", listOf(
                listOf(25f to 15f, 75f to 15f, 45f to 50f, 75f to 75f, 50f to 100f, 25f to 85f)
            )),
            createTemplate("4", "4_2stroke", listOf(
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

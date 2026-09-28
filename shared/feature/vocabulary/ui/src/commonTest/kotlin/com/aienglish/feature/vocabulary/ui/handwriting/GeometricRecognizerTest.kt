package com.aienglish.feature.vocabulary.ui.handwriting

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class GeometricRecognizerTest {

    private val recognizer = GeometricRecognizer()

    @Test
    fun resample_producesExactTargetPointCount() {
        val points = listOf(
            Point2D(0f, 0f, 0),
            Point2D(50f, 50f, 0),
            Point2D(100f, 100f, 0)
        )
        val resampled = GeometricRecognizer.resample(points, 32)
        assertEquals(32, resampled.size)
    }

    @Test
    fun normalize_centersCentroidNearOrigin() {
        val points = (0..31).map { i ->
            Point2D(i * 10f, i * 5f, 0)
        }
        val normalized = GeometricRecognizer.normalize(points)
        val avgX = normalized.map { it.x }.average().toFloat()
        val avgY = normalized.map { it.y }.average().toFloat()

        assertTrue(kotlin.math.abs(avgX) < 0.1f)
        assertTrue(kotlin.math.abs(avgY) < 0.1f)
    }

    @Test
    fun computeZoning_sumsToOne() {
        val points = (0..31).map { i ->
            Point2D(i * 3f, (31 - i) * 3f, 0)
        }
        val zoning = GeometricRecognizer.computeZoning(points)
        assertEquals(9, zoning.size)
        val sum = zoning.sum()
        assertTrue(kotlin.math.abs(sum - 1f) < 0.01f)
    }

    @Test
    fun detectDirectionalGesture_swipeRightIsSpace() {
        val swipeRight = listOf(
            (0..10).map { i -> Point2D(10f + i * 15f, 50f, 0) }
        )
        val results = recognizer.recognize(swipeRight)
        assertEquals(1, results.size)
        assertEquals(GeometricRecognizer.GESTURE_SPACE, results.first().character)
    }

    @Test
    fun detectDirectionalGesture_swipeLeftIsBackspace() {
        val swipeLeft = listOf(
            (0..10).map { i -> Point2D(160f - i * 15f, 50f, 0) }
        )
        val results = recognizer.recognize(swipeLeft)
        assertEquals(1, results.size)
        assertEquals(GeometricRecognizer.GESTURE_BACKSPACE, results.first().character)
    }

    @Test
    fun recognize_letterA_returnsTopCandidateA() {
        val stroke1 = listOf(
            Point2D(15f, 100f, 0),
            Point2D(32f, 50f, 0),
            Point2D(50f, 0f, 0),
            Point2D(68f, 50f, 0),
            Point2D(85f, 100f, 0)
        )
        val stroke2 = listOf(
            Point2D(30f, 60f, 1),
            Point2D(70f, 60f, 1)
        )

        val results = recognizer.recognize(listOf(stroke1, stroke2), topN = 3)
        assertTrue(results.isNotEmpty(), "Results should not be empty")
        assertEquals("A", results.first().character, "Top match for drawn A should be 'A'")
        assertTrue(results.first().score > 0.65f, "Confidence score should be > 0.65")
    }

    @Test
    fun recognize_letterO_returnsTopCandidateO() {
        val stroke = listOf(
            Point2D(50f, 0f, 0),
            Point2D(20f, 25f, 0),
            Point2D(20f, 75f, 0),
            Point2D(50f, 100f, 0),
            Point2D(80f, 75f, 0),
            Point2D(80f, 25f, 0),
            Point2D(50f, 0f, 0)
        )

        val results = recognizer.recognize(listOf(stroke), topN = 3)
        assertTrue(results.isNotEmpty())
        val topChars = results.map { it.character }
        assertTrue(topChars.contains("O") || topChars.contains("o") || topChars.contains("0"))
    }

    @Test
    fun recognize_letterC_doesNotConfuseWithO() {
        // Open arc for 'C' (starts top-right, curves around, ends bottom-right)
        val strokeC = listOf(
            Point2D(80f, 18f, 0),
            Point2D(50f, 0f, 0),
            Point2D(20f, 30f, 0),
            Point2D(20f, 70f, 0),
            Point2D(50f, 100f, 0),
            Point2D(80f, 82f, 0)
        )
        val results = recognizer.recognize(listOf(strokeC), topN = 3)
        assertTrue(results.isNotEmpty())
        val top = results.first().character
        assertTrue(top.equals("C", ignoreCase = true), "Open arc should recognize as C/c, got $top")
    }

    @Test
    fun recognize_letterT_returnsT() {
        val topBar = listOf(Point2D(15f, 0f, 0), Point2D(85f, 0f, 0))
        val vertical = listOf(Point2D(50f, 0f, 1), Point2D(50f, 100f, 1))
        val results = recognizer.recognize(listOf(topBar, vertical), topN = 3)
        assertTrue(results.isNotEmpty())
        val top = results.first().character
        assertTrue(top.equals("T", ignoreCase = true), "Drawn T should recognize as T/t, got $top")
    }

    @Test
    fun recognize_emptyPoints_returnsEmpty() {
        val emptyStrokes = emptyList<List<Point2D>>()
        assertTrue(recognizer.recognize(emptyStrokes).isEmpty())
    }

    @Test
    fun recognize_singleTapDot_recognizesAsDotOrNang() {
        val tapStroke = listOf(listOf(Point2D(50f, 80f, 0)))
        val results = recognizer.recognize(tapStroke, topN = 3)
        assertTrue(results.isNotEmpty(), "Single tap should be recognized as dot or heavy mark")
        val top = results.first().character
        assertTrue(top == "̣" || top == "." || results.any { it.character == "̣" }, "Top results should include dot or nặng")
    }

    @Test
    fun recognize_acuteSlash_returnsAcute() {
        // Dấu sắc (slash from bottom-left to top-right)
        val acuteStroke = listOf(
            listOf(
                Point2D(25f, 85f, 0),
                Point2D(50f, 50f, 0),
                Point2D(75f, 15f, 0)
            )
        )
        val results = recognizer.recognize(acuteStroke, topN = 3)
        assertTrue(results.isNotEmpty())
        val topChars = results.map { it.character }
        assertTrue(topChars.contains("́") || results.any { it.label.contains("dấu_sắc") })
    }

    @Test
    fun recognize_circumflex_returnsCircumflex() {
        // Dấu mũ (^)
        val circumflexStroke = listOf(
            listOf(
                Point2D(20f, 75f, 0),
                Point2D(50f, 20f, 0),
                Point2D(80f, 75f, 0)
            )
        )
        val results = recognizer.recognize(circumflexStroke, topN = 3)
        assertTrue(results.isNotEmpty())
        val topChars = results.map { it.character }
        assertTrue(topChars.contains("^") || results.any { it.label.contains("dấu_mũ") })
    }
}

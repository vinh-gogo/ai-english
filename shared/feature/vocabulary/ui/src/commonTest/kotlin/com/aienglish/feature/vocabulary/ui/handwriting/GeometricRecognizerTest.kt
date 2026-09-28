package com.aienglish.feature.vocabulary.ui.handwriting

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
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
        // Draw standard 'A' strokes:
        // stroke 1: diagonal up-right and down-right
        // stroke 2: crossbar
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
        assertTrue(results.first().score > 0.70f, "Confidence score should be > 0.70")
    }

    @Test
    fun recognize_letterO_returnsTopCandidateO() {
        // Draw circle
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
    fun recognize_emptyOrTooFewPoints_returnsEmpty() {
        val emptyStrokes = emptyList<List<Point2D>>()
        assertTrue(recognizer.recognize(emptyStrokes).isEmpty())

        val tinyStroke = listOf(listOf(Point2D(10f, 10f, 0)))
        assertTrue(recognizer.recognize(tinyStroke).isEmpty())
    }
}

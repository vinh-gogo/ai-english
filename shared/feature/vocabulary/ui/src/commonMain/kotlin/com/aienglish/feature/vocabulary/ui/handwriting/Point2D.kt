package com.aienglish.feature.vocabulary.ui.handwriting

import kotlin.math.sqrt

/**
 * 2D Point representation for handwriting stroke points.
 *
 * @property x X-coordinate on canvas.
 * @property y Y-coordinate on canvas.
 * @property strokeId Index of the stroke this point belongs to.
 * @property timestamp Milliseconds timestamp when the point was recorded.
 */
data class Point2D(
    val x: Float,
    val y: Float,
    val strokeId: Int = 0,
    val timestamp: Long = 0L
) {
    fun distanceTo(other: Point2D): Float {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt(dx * dx + dy * dy)
    }

    fun distanceSquaredTo(other: Point2D): Float {
        val dx = x - other.x
        val dy = y - other.y
        return dx * dx + dy * dy
    }
}

/**
 * Candidate match returned by the geometric gesture recognizer.
 */
data class RecognitionResult(
    val character: String,
    val score: Float, // Normalized 0.0 to 1.0 (higher = better match)
    val label: String = character
)

package com.aienglish.feature.grammargraph.ui.screen

import kotlin.math.abs
import kotlin.math.sqrt

internal class PhysicsNode(
    val id: String,
    val pattern: String,
    var x: Float, // Coordinates in DP units
    var y: Float, // Coordinates in DP units
    val widthDp: Float,
    val heightDp: Float,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var isBeingDragged: Boolean = false
)

internal fun getCapsuleIntersection(w: Float, h: Float, ux: Float, uy: Float): Float {
    val r = h / 2f
    val d = (w - h) / 2f
    if (d <= 0f) {
        return r
    }
    
    // Check intersection with flat top/bottom edges
    val tFlat = r / (abs(uy) + 0.0001f)
    val xFlat = tFlat * ux
    if (abs(xFlat) <= d) {
        return tFlat
    }
    
    // Check intersection with left/right circular caps
    val cx = if (ux > 0f) d else -d
    val b = ux * cx
    val c = cx * cx - r * r
    val discriminant = b * b - c
    return if (discriminant >= 0f) {
        b + sqrt(discriminant)
    } else {
        r
    }
}

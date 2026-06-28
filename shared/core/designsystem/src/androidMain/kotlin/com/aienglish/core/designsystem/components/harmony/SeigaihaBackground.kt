package com.aienglish.core.designsystem.components.harmony

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import com.aienglish.core.designsystem.theme.SurfaceDark

@Composable
actual fun SeigaihaBackground(isDark: Boolean) {
    val bgColor = SurfaceDark
    // White with 12% alpha has the perfect contrast and visibility on OLED/LCD dark themes
    val patternColor = Color.White.copy(alpha = 0.12f)
    
    val density = LocalDensity.current
    val scale = 1.5f
    val rowStep = 20f * scale
    val colStep = 60f * scale

    Canvas(modifier = Modifier.fillMaxSize()) {
        val widthPx = size.width
        val heightPx = size.height
        
        // Fill the entire canvas background first
        drawRect(color = bgColor)

        val rows = (heightPx / rowStep).toInt() + 2
        val cols = (widthPx / colStep).toInt() + 2

        for (r in -1..rows) {
            val yOffset = r * rowStep
            val isOddRow = r % 2 != 0
            
            for (c in -1..cols) {
                val xOffset = c * colStep + (if (isOddRow) colStep / 2f else 0f)
                val centerX = xOffset
                val centerY = yOffset

                val maxRadius = 30f * scale
                
                // 1. Draw solid background semi-circle to cover the rows behind it
                drawArc(
                    color = bgColor,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(centerX - maxRadius, centerY - maxRadius),
                    size = Size(maxRadius * 2, maxRadius * 2),
                    style = Fill
                )
                
                // 2. Draw the concentric waves / roof tiles lines
                val radii = listOf(30f, 24f, 18f, 12f, 6f).map { it * scale }
                radii.forEach { radius ->
                    drawArc(
                        color = patternColor,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(centerX - radius, centerY - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = 1f * scale)
                    )
                }
            }
        }
    }
}

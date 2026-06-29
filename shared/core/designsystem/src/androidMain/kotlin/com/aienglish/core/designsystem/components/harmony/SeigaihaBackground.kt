package com.aienglish.core.designsystem.components.harmony

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
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
    val maxRadius = 30f * scale
    val strokeWidth = 1f * scale
    
    val radii = remember { floatArrayOf(30f * scale, 24f * scale, 18f * scale, 12f * scale, 6f * scale) }
    val strokeStyle = remember { Stroke(width = strokeWidth) }

    var cachedBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var cachedSize by remember { mutableStateOf(Size.Zero) }
    val drawScope = remember { CanvasDrawScope() }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        
        if (cachedBitmap == null || cachedSize != size) {
            val bitmapWidth = width.toInt().coerceAtLeast(1)
            val bitmapHeight = height.toInt().coerceAtLeast(1)
            val newBitmap = ImageBitmap(bitmapWidth, bitmapHeight)
            val newCanvas = androidx.compose.ui.graphics.Canvas(newBitmap)
            
            drawScope.draw(
                density = this,
                layoutDirection = layoutDirection,
                canvas = newCanvas,
                size = size
            ) {
                // 1. Fill background
                drawRect(color = bgColor)

                val rows = (height / rowStep).toInt() + 2
                val cols = (width / colStep).toInt() + 2

                for (r in -1..rows) {
                    val yOffset = r * rowStep
                    val isOddRow = r % 2 != 0
                    
                    for (c in -1..cols) {
                        val xOffset = c * colStep + (if (isOddRow) colStep / 2f else 0f)
                        val centerX = xOffset
                        val centerY = yOffset

                        // Draw solid background semi-circle
                        drawArc(
                            color = bgColor,
                            startAngle = 180f,
                            sweepAngle = 180f,
                            useCenter = true,
                            topLeft = Offset(centerX - maxRadius, centerY - maxRadius),
                            size = Size(maxRadius * 2, maxRadius * 2),
                            style = Fill
                        )
                        
                        // Draw concentric waves (zero allocation loop)
                        for (i in 0 until radii.size) {
                            val radius = radii[i]
                            drawArc(
                                color = patternColor,
                                startAngle = 180f,
                                sweepAngle = 180f,
                                useCenter = false,
                                topLeft = Offset(centerX - radius, centerY - radius),
                                size = Size(radius * 2, radius * 2),
                                style = strokeStyle
                            )
                        }
                    }
                }
            }
            cachedBitmap = newBitmap
            cachedSize = size
        }

        cachedBitmap?.let {
            drawImage(image = it)
        }
    }
}

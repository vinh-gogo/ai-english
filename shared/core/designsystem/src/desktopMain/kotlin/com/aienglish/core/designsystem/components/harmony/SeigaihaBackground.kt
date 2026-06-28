package com.aienglish.core.designsystem.components.harmony

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toAwtImage
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.platform.LocalDensity
import com.aienglish.core.designsystem.theme.SurfaceDark
import com.aienglish.core.designsystem.theme.BorderColor
import java.awt.image.BufferedImage
import org.jetbrains.skia.FilterBlurMode
import org.jetbrains.skia.MaskFilter
import org.jetbrains.skia.Paint as SkiaPaint
import org.jetbrains.skia.PaintMode
import org.jetbrains.skia.Shader as SkiaShader
import org.jetbrains.skia.Rect
import androidx.compose.ui.graphics.nativeCanvas

@Composable
actual fun SeigaihaBackground(isDark: Boolean) {
    val bgColor = if (isDark) ComposeColor(0xFF0F172A) else SurfaceDark
    val patternColor = ComposeColor.White.copy(alpha = 0.04f)
    
    val density = LocalDensity.current
    val scale = 1.25f
    val rowStep = 20f * scale
    val colStep = 60f * scale

    val cloudBitmap = remember(isDark) {
        val path = if (isDark) "bmp/cloud-top-left-side-dark.bmp" else "bmp/cloud-top-left-side-light.bmp"
        try {
            androidx.compose.ui.res.useResource(path) {
                androidx.compose.ui.res.loadImageBitmap(it).softenIntelligently()
            }
        } catch (e: Exception) {
            null
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val widthPx = with(density) { maxWidth.toPx() }.toInt()
        val heightPx = with(density) { maxHeight.toPx() }.toInt()

        val fullBackground = remember(widthPx, heightPx, isDark, cloudBitmap) {
            if (widthPx <= 0 || heightPx <= 0) return@remember null

            val bitmap = ImageBitmap(widthPx, heightPx)
            val canvas = androidx.compose.ui.graphics.Canvas(bitmap)
            
            val skiaStrokePaint = SkiaPaint().apply {
                color = patternColor.toArgb()
                mode = PaintMode.STROKE
                strokeWidth = 1f * scale
                isAntiAlias = true
                maskFilter = MaskFilter.makeBlur(FilterBlurMode.NORMAL, 0.35f * scale)
            }
            val skiaFillPaint = SkiaPaint().apply {
                color = bgColor.toArgb()
                mode = PaintMode.FILL
                isAntiAlias = true
            }

            canvas.drawRect(androidx.compose.ui.geometry.Rect(0f, 0f, widthPx.toFloat(), heightPx.toFloat()), androidx.compose.ui.graphics.Paint().apply { color = bgColor })

            val rows = (heightPx / rowStep).toInt() + 2
            val cols = (widthPx / colStep).toInt() + 2

            for (r in -1..rows) {
                val yOffset = r * rowStep
                val isOddRow = r % 2 != 0
                
                for (c in -1..cols) {
                    val xOffset = c * colStep + (if (isOddRow) colStep / 2f else 0f)
                    val centerX = xOffset
                    val centerY = yOffset

                    val radius30 = 30f * scale
                    canvas.nativeCanvas.drawArc(
                        centerX - radius30, centerY - radius30, centerX + radius30, centerY + radius30,
                        180f, 180f, true, skiaFillPaint
                    )
                    val radii = listOf(30f, 24f, 18f, 12f, 6f).map { it * scale }
                    radii.forEach { r ->
                        canvas.nativeCanvas.drawArc(
                            centerX - r, centerY - r, centerX + r, centerY + r,
                            180f, 180f, false, skiaStrokePaint
                        )
                    }
                }
            }

            cloudBitmap?.let { cloud ->
                val topLeftDstWidth = maxOf(1, cloud.width - 150)
                val topLeftDstHeight = maxOf(1, cloud.height - 150)
                canvas.drawImageRect(
                    image = cloud,
                    srcOffset = androidx.compose.ui.unit.IntOffset.Zero,
                    srcSize = androidx.compose.ui.unit.IntSize(cloud.width, cloud.height),
                    dstOffset = androidx.compose.ui.unit.IntOffset.Zero,
                    dstSize = androidx.compose.ui.unit.IntSize(topLeftDstWidth, topLeftDstHeight),
                    paint = androidx.compose.ui.graphics.Paint().apply {
                        filterQuality = androidx.compose.ui.graphics.FilterQuality.High
                        alpha = 0.8f
                    }
                )
            }

            val noiseShader = SkiaShader.makeFractalNoise(0.75f, 0.75f, 3, 0f)
            val noisePaint = SkiaPaint().apply {
                shader = noiseShader
                alpha = (0.04f * 255).toInt()
            }
            canvas.nativeCanvas.drawRect(Rect.makeWH(widthPx.toFloat(), heightPx.toFloat()), noisePaint)

            bitmap
        }

        Canvas(modifier = Modifier.fillMaxSize()) {
            fullBackground?.let {
                drawImage(it)
                drawRect(color = bgColor.copy(alpha = 0.15f))
                drawRect(color = ComposeColor.Black.copy(alpha = 0.05f))
            }
        }
    }
}

private fun ImageBitmap.softenIntelligently(): ImageBitmap {
    val awtImage = this.toAwtImage()
    val width = awtImage.width
    val height = awtImage.height
    
    val grayValues = IntArray(width * height)
    val histogram = IntArray(256)
    for (y in 0 until height) {
        for (x in 0 until width) {
            val rgb = awtImage.getRGB(x, y)
            val r = (rgb shr 16) and 0xFF
            val g = (rgb shr 8) and 0xFF
            val b = rgb and 0xFF
            val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt().coerceIn(0, 255)
            grayValues[y * width + x] = gray
            histogram[gray]++
        }
    }
    
    val total = width * height
    var sum = 0f
    for (t in 0..255) {
        sum += t * histogram[t]
    }
    
    var sumB = 0f
    var wB = 0
    var wF = 0
    
    var varMax = 0f
    var threshold = 0
    
    for (t in 0..255) {
        wB += histogram[t]
        if (wB == 0) continue
        wF = total - wB
        if (wF == 0) break
        
        sumB += t * histogram[t]
        val mB = sumB / wB
        val mF = (sum - sumB) / wF
        
        val varBetween = wB.toFloat() * wF.toFloat() * (mB - mF) * (mB - mF)
        if (varBetween > varMax) {
            varMax = varBetween
            threshold = t
        }
    }
    
    val result = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    for (y in 0 until height) {
        for (x in 0 until width) {
            val rgb = awtImage.getRGB(x, y)
            val alpha = (rgb shr 24) and 0xFF
            val r = (rgb shr 16) and 0xFF
            val g = (rgb shr 8) and 0xFF
            val b = rgb and 0xFF
            
            val grayValue = grayValues[y * width + x]
            
            val softeningStrength = if (grayValue < threshold) {
                0.70
            } else {
                val range = 255.0 - threshold
                if (range > 0) 0.70 * (255.0 - grayValue) / range else 0.0
            }
            
            val nr = (r + (255 - r) * softeningStrength).toInt().coerceIn(0, 255)
            val ng = (g + (255 - g) * softeningStrength).toInt().coerceIn(0, 255)
            val nb = (b + (255 - b) * softeningStrength).toInt().coerceIn(0, 255)
            
            result.setRGB(x, y, (alpha shl 24) or (nr shl 16) or (ng shl 8) or nb)
        }
    }
    
    return result.toComposeImageBitmap()
}

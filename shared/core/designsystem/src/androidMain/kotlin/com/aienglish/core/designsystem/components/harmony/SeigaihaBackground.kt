package com.aienglish.core.designsystem.components.harmony

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color as ComposeColor
import com.aienglish.core.designsystem.theme.SurfaceDark

@Composable
actual fun SeigaihaBackground(isDark: Boolean) {
    val bgColor = if (isDark) ComposeColor(0xFF0F172A) else SurfaceDark
    val patternColor = ComposeColor.White.copy(alpha = 0.04f)

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawRect(color = bgColor)
        drawRect(color = patternColor.copy(alpha = 0.15f))
        drawRect(color = ComposeColor.Black.copy(alpha = 0.05f))
    }
}

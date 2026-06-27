package com.aienglish.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val accent: Color = RoyalBlue,
    val accentLight: Color = RoyalBlueLight,
    val accentDark: Color = RoyalBlueDark,
    val background: Color = SurfaceDark,
    val surface: Color = SurfaceCard,
    val surfaceElevated: Color = SurfaceElevated,
    val mainText: Color = TextPrimary,
    val mutedText: Color = TextSecondary,
    val hintText: Color = TextMuted,
    val correct: Color = Correct,
    val error: Color = Wrong,
    val warning: Color = Warning,
    val border: Color = BorderColor
)

val LocalAppColors = staticCompositionLocalOf { AppColors() }

@Composable
fun AIEnglishTheme(content: @Composable () -> Unit) {
    val colors = AppColors()
    val darkScheme = darkColorScheme(
        primary = RoyalBlue,
        onPrimary = Color.White,
        primaryContainer = RoyalBlueDark,
        background = SurfaceDark,
        surface = SurfaceCard,
        error = Wrong,
        onBackground = TextPrimary,
        onSurface = TextPrimary
    )
    CompositionLocalProvider(LocalAppColors provides colors) {
        MaterialTheme(colorScheme = darkScheme, content = content)
    }
}

package com.aienglish.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Custom app color tokens mapped to the "Sunset Warmth" design system.
 *
 * Use [LocalAppColors].current in Composables instead of hardcoded colors.
 *
 * Token mapping:
 * - accent           → Terracotta CTA (#944A00)
 * - accentHighlight  → Terracotta Bright (#E67E22) for hover/pressed states
 * - accentLight      → Pale Terracotta (#FFDC25) for chip/tag backgrounds
 * - background       → Warm Cream (#FFF8F6)
 * - surface          → Pure White (#FFFFFF) for cards
 * - surfaceContainer → Warm tinted container (#FFE9E3)
 * - mainText         → Deep Cocoa (#2C160E)
 * - mutedText        → Warm Brown (#564337)
 * - hintText         → Outline Brown (#897365)
 * - border           → Outline Light (#DCC1B1)
 * - correct          → Deep Green (#2E7D32)
 * - error            → Warm Red (#BA1A1A)
 * - warning          → Amber Brown (#B45309)
 */
data class AppColors(
    val accent: Color = Terracotta,
    val accentHighlight: Color = TerracottaBright,
    val accentLight: Color = TerracottaPale,
    val accentDark: Color = TerracottaDark,
    val background: Color = SurfaceWarm,
    val surface: Color = SurfaceWhite,
    val surfaceElevated: Color = SurfaceContainerLow,
    val surfaceContainer: Color = SurfaceContainer,
    val surfaceContainerHigh: Color = SurfaceContainerHigh,
    val surfaceContainerHighest: Color = SurfaceContainerHighest,
    val surfaceDim: Color = SurfaceDim,
    val mainText: Color = OnSurfaceDark,
    val mutedText: Color = OnSurfaceVariant,
    val hintText: Color = OutlineBrown,
    val border: Color = OutlineLight,
    val inverseSurface: Color = InverseSurface,
    val inverseOnSurface: Color = InverseOnSurface,
    val secondaryContainer: Color = SandContainer,
    val onSecondaryContainer: Color = OnSandContainer,
    val tertiaryContainer: Color = SlateContainer,
    val onTertiaryContainer: Color = OnSlateContainer,
    val correct: Color = SemanticCorrect,
    val error: Color = SemanticError,
    val errorContainer: Color = SemanticErrorContainer,
    val warning: Color = SemanticWarning
)

val LocalAppColors = staticCompositionLocalOf { AppColors() }

/** Convenience accessor: `MaterialTheme.appColors` */
val MaterialTheme.appColors: AppColors
    @Composable get() = LocalAppColors.current

@Composable
fun AIEnglishTheme(content: @Composable () -> Unit) {
    val colors = AppColors()
    val lightScheme = lightColorScheme(
        primary            = Terracotta,
        onPrimary          = Color.White,
        primaryContainer   = TerracottaBright,
        onPrimaryContainer = TerracottaDeep,
        secondary          = SandMid,
        onSecondary        = Color.White,
        secondaryContainer = SandContainer,
        onSecondaryContainer = OnSandContainer,
        tertiary           = SlateMid,
        onTertiary         = Color.White,
        tertiaryContainer  = SlateContainer,
        onTertiaryContainer = OnSlateContainer,
        background         = SurfaceWarm,
        onBackground       = OnSurfaceDark,
        surface            = SurfaceWhite,
        onSurface          = OnSurfaceDark,
        surfaceVariant     = SurfaceContainerHighest,
        onSurfaceVariant   = OnSurfaceVariant,
        outline            = OutlineBrown,
        outlineVariant     = OutlineLight,
        error              = SemanticError,
        onError            = Color.White,
        errorContainer     = SemanticErrorContainer,
        onErrorContainer   = OnSemanticError,
        inverseSurface     = InverseSurface,
        inverseOnSurface   = InverseOnSurface,
        inversePrimary     = TerracottaLight,
        surfaceTint        = Terracotta
    )
    CompositionLocalProvider(LocalAppColors provides colors) {
        MaterialTheme(colorScheme = lightScheme, content = content)
    }
}

package com.aienglish.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ── App Theme Enum ────────────────────────────────────────────────────────
enum class AppTheme(val displayName: String, val emoji: String, val isDark: Boolean = false) {
    SUNSET_WARMTH("Sunset Warmth", "🌅"),
    NATURE_MINT("Nature Mint", "🌿"),
    BERRY_SORBET("Berry Sorbet", "🍓"),
    NORDIC_SLATE("Nordic Slate", "🧱"),
    MIDNIGHT_INDIGO("Midnight Indigo", "🌌", isDark = true)
}

// ── App Color Tokens ──────────────────────────────────────────────────────
data class AppColors(
    val accent: Color,
    val accentHighlight: Color,
    val accentLight: Color,
    val accentDark: Color,
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceDim: Color,
    val mainText: Color,
    val mutedText: Color,
    val hintText: Color,
    val border: Color,
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val correct: Color,
    val error: Color,
    val errorContainer: Color,
    val warning: Color,
    // M3 roles for MaterialTheme bridge
    val m3Primary: Color,
    val m3PrimaryContainer: Color,
    val m3OnPrimaryContainer: Color,
    val m3Secondary: Color,
    val m3SecondaryContainer: Color,
    val m3OnSecondaryContainer: Color,
    val m3Tertiary: Color,
    val m3TertiaryContainer: Color,
    val m3OnTertiaryContainer: Color,
    val m3Outline: Color,
    val m3OutlineVariant: Color,
    val m3InversePrimary: Color,
    val m3SurfaceVariant: Color,
    val m3OnSurfaceVariant: Color
)

// ── Sunset Warmth Factory ─────────────────────────────────────────────────
fun sunsetWarmthColors() = AppColors(
    accent                 = Terracotta,
    accentHighlight        = TerracottaBright,
    accentLight            = TerracottaPale,
    accentDark             = TerracottaDark,
    background             = SW_Surface,
    surface                = SW_SurfaceWhite,
    surfaceElevated        = SW_SurfaceContainerLow,
    surfaceContainer       = SW_SurfaceContainer,
    surfaceContainerHigh   = SW_SurfaceContainerHigh,
    surfaceContainerHighest = SW_SurfaceContainerHighest,
    surfaceDim             = SW_SurfaceDim,
    mainText               = SW_OnSurface,
    mutedText              = SW_OnSurfaceVariant,
    hintText               = SW_Outline,
    border                 = SW_OutlineVariant,
    inverseSurface         = SW_InverseSurface,
    inverseOnSurface       = SW_InverseOnSurface,
    secondaryContainer     = SW_SecondaryContainer,
    onSecondaryContainer   = SW_OnSecondaryContainer,
    tertiaryContainer      = SW_TertiaryContainer,
    onTertiaryContainer    = SW_OnTertiaryContainer,
    correct                = SW_Correct,
    error                  = SW_Error,
    errorContainer         = SW_ErrorContainer,
    warning                = SW_Warning,
    m3Primary              = Terracotta,
    m3PrimaryContainer     = TerracottaBright,
    m3OnPrimaryContainer   = TerracottaDeep,
    m3Secondary            = SW_Secondary,
    m3SecondaryContainer   = SW_SecondaryContainer,
    m3OnSecondaryContainer = SW_OnSecondaryContainer,
    m3Tertiary             = SW_Tertiary,
    m3TertiaryContainer    = SW_TertiaryContainer,
    m3OnTertiaryContainer  = SW_OnTertiaryContainer,
    m3Outline              = SW_Outline,
    m3OutlineVariant       = SW_OutlineVariant,
    m3InversePrimary       = TerracottaLight,
    m3SurfaceVariant       = SW_SurfaceContainerHighest,
    m3OnSurfaceVariant     = SW_OnSurfaceVariant
)

// ── Nature Mint Factory ───────────────────────────────────────────────────
fun natureMintColors() = AppColors(
    accent                 = MintGreen,
    accentHighlight        = MintBright,
    accentLight            = MintPale,
    accentDark             = MintDark,
    background             = NM_Surface,
    surface                = NM_SurfaceWhite,
    surfaceElevated        = NM_SurfaceContainerLow,
    surfaceContainer       = NM_SurfaceContainer,
    surfaceContainerHigh   = NM_SurfaceContainerHigh,
    surfaceContainerHighest = NM_SurfaceContainerHighest,
    surfaceDim             = NM_SurfaceDim,
    mainText               = NM_OnSurface,
    mutedText              = NM_OnSurfaceVariant,
    hintText               = NM_Outline,
    border                 = NM_OutlineVariant,
    inverseSurface         = NM_InverseSurface,
    inverseOnSurface       = NM_InverseOnSurface,
    secondaryContainer     = NM_SecondaryContainer,
    onSecondaryContainer   = NM_OnSecondaryContainer,
    tertiaryContainer      = NM_TertiaryContainer,
    onTertiaryContainer    = NM_OnTertiaryContainer,
    correct                = NM_Correct,
    error                  = NM_Error,
    errorContainer         = NM_ErrorContainer,
    warning                = NM_Warning,
    m3Primary              = MintGreen,
    m3PrimaryContainer     = MintBright,
    m3OnPrimaryContainer   = MintDeep,
    m3Secondary            = NM_Secondary,
    m3SecondaryContainer   = NM_SecondaryContainer,
    m3OnSecondaryContainer = NM_OnSecondaryContainer,
    m3Tertiary             = NM_Tertiary,
    m3TertiaryContainer    = NM_TertiaryContainer,
    m3OnTertiaryContainer  = NM_OnTertiaryContainer,
    m3Outline              = NM_Outline,
    m3OutlineVariant       = NM_OutlineVariant,
    m3InversePrimary       = MintLight,
    m3SurfaceVariant       = NM_SurfaceContainerHighest,
    m3OnSurfaceVariant     = NM_OnSurfaceVariant
)

// ── Berry Sorbet Factory ─────────────────────────────────────────────────────────
fun berrySorbetColors() = AppColors(
    accent                 = BerryRed,
    accentHighlight        = BerryBright,
    accentLight            = BerryPale,
    accentDark             = BerryDark,
    background             = BS_Surface,
    surface                = BS_SurfaceWhite,
    surfaceElevated        = BS_SurfaceContainerLow,
    surfaceContainer       = BS_SurfaceContainer,
    surfaceContainerHigh   = BS_SurfaceContainerHigh,
    surfaceContainerHighest = BS_SurfaceContainerHighest,
    surfaceDim             = BS_SurfaceDim,
    mainText               = BS_OnSurface,
    mutedText              = BS_OnSurfaceVariant,
    hintText               = BS_Outline,
    border                 = BS_OutlineVariant,
    inverseSurface         = BS_InverseSurface,
    inverseOnSurface       = BS_InverseOnSurface,
    secondaryContainer     = BS_SecondaryContainer,
    onSecondaryContainer   = BS_OnSecondaryContainer,
    tertiaryContainer      = BS_TertiaryContainer,
    onTertiaryContainer    = BS_OnTertiaryContainer,
    correct                = BS_Correct,
    error                  = BS_Error,
    errorContainer         = BS_ErrorContainer,
    warning                = BS_Warning,
    m3Primary              = BerryRed,
    m3PrimaryContainer     = BerryBright,
    m3OnPrimaryContainer   = BerryDeep,
    m3Secondary            = BS_Secondary,
    m3SecondaryContainer   = BS_SecondaryContainer,
    m3OnSecondaryContainer = BS_OnSecondaryContainer,
    m3Tertiary             = BS_Tertiary,
    m3TertiaryContainer    = BS_TertiaryContainer,
    m3OnTertiaryContainer  = BS_OnTertiaryContainer,
    m3Outline              = BS_Outline,
    m3OutlineVariant       = BS_OutlineVariant,
    m3InversePrimary       = BerryLight,
    m3SurfaceVariant       = BS_SurfaceContainerHighest,
    m3OnSurfaceVariant     = BS_OnSurfaceVariant
)

// ── Nordic Slate Factory ─────────────────────────────────────────────────────────
fun nordicSlateColors() = AppColors(
    accent                 = SlateDeep,
    accentHighlight        = SlatePrimary,
    accentLight            = SlateFixedDim,
    accentDark             = SlateDarkest,
    background             = NS_Surface,
    surface                = NS_SurfaceWhite,
    surfaceElevated        = NS_SurfaceContainerLow,
    surfaceContainer       = NS_SurfaceContainer,
    surfaceContainerHigh   = NS_SurfaceContainerHigh,
    surfaceContainerHighest = NS_SurfaceContainerHighest,
    surfaceDim             = NS_SurfaceDim,
    mainText               = NS_OnSurface,
    mutedText              = NS_OnSurfaceVariant,
    hintText               = NS_Outline,
    border                 = NS_OutlineVariant,
    inverseSurface         = NS_InverseSurface,
    inverseOnSurface       = NS_InverseOnSurface,
    secondaryContainer     = NS_SecondaryContainer,
    onSecondaryContainer   = NS_OnSecondaryContainer,
    tertiaryContainer      = NS_TertiaryContainer,
    onTertiaryContainer    = NS_OnTertiaryContainer,
    correct                = NS_Correct,
    error                  = NS_Error,
    errorContainer         = NS_ErrorContainer,
    warning                = NS_Warning,
    m3Primary              = SlateDeep,
    m3PrimaryContainer     = SlatePrimary,
    m3OnPrimaryContainer   = SlateContainer,
    m3Secondary            = NS_Secondary,
    m3SecondaryContainer   = NS_SecondaryContainer,
    m3OnSecondaryContainer = NS_OnSecondaryContainer,
    m3Tertiary             = NS_Tertiary,
    m3TertiaryContainer    = NS_TertiaryContainer,
    m3OnTertiaryContainer  = NS_OnTertiaryContainer,
    m3Outline              = NS_Outline,
    m3OutlineVariant       = NS_OutlineVariant,
    m3InversePrimary       = SlatePale,
    m3SurfaceVariant       = NS_SurfaceContainerHighest,
    m3OnSurfaceVariant     = NS_OnSurfaceVariant
)

// ── Midnight Indigo Factory (Dark Theme) ──────────────────────────────────
fun midnightIndigoColors() = AppColors(
    accent                 = VioletLight,
    accentHighlight        = VioletContainer,
    accentLight            = VioletPale,
    accentDark             = VioletDimmed,
    background             = MI_Surface,
    surface                = MI_SurfaceContainer,
    surfaceElevated        = MI_SurfaceContainerLow,
    surfaceContainer       = MI_SurfaceContainerHigh,
    surfaceContainerHigh   = MI_SurfaceContainerHigh,
    surfaceContainerHighest = MI_SurfaceContainerHighest,
    surfaceDim             = MI_SurfaceDim,
    mainText               = MI_OnSurface,
    mutedText              = MI_OnSurfaceVariant,
    hintText               = MI_Outline,
    border                 = MI_OutlineVariant,
    inverseSurface         = MI_InverseSurface,
    inverseOnSurface       = MI_InverseOnSurface,
    secondaryContainer     = MI_SecondaryContainer,
    onSecondaryContainer   = MI_OnSecondaryContainer,
    tertiaryContainer      = MI_TertiaryContainer,
    onTertiaryContainer    = MI_OnTertiaryContainer,
    correct                = MI_Correct,
    error                  = MI_Error,
    errorContainer         = MI_ErrorContainer,
    warning                = MI_Warning,
    m3Primary              = VioletLight,
    m3PrimaryContainer     = VioletContainer,
    m3OnPrimaryContainer   = VioletDeep,
    m3Secondary            = MI_Secondary,
    m3SecondaryContainer   = MI_SecondaryContainer,
    m3OnSecondaryContainer = MI_OnSecondaryContainer,
    m3Tertiary             = MI_Tertiary,
    m3TertiaryContainer    = MI_TertiaryContainer,
    m3OnTertiaryContainer  = MI_OnTertiaryContainer,
    m3Outline              = MI_Outline,
    m3OutlineVariant       = MI_OutlineVariant,
    m3InversePrimary       = VioletDimmed,
    m3SurfaceVariant       = MI_SurfaceContainerHighest,
    m3OnSurfaceVariant     = MI_OnSurfaceVariant
)

// ── Composition Locals ────────────────────────────────────────────────────
val LocalAppColors = staticCompositionLocalOf<AppColors> { sunsetWarmthColors() }
val LocalAppTheme  = staticCompositionLocalOf { AppTheme.SUNSET_WARMTH }

/** Convenience: `MaterialTheme.appColors` */
val MaterialTheme.appColors: AppColors
    @Composable get() = LocalAppColors.current

/** Convenience: `MaterialTheme.appTheme` */
val MaterialTheme.appTheme: AppTheme
    @Composable get() = LocalAppTheme.current

// ── Theme Composable ──────────────────────────────────────────────────────
@Composable
fun AIEnglishTheme(
    theme: AppTheme = AppTheme.SUNSET_WARMTH,
    content: @Composable () -> Unit
) {
    val colors = when (theme) {
        AppTheme.SUNSET_WARMTH   -> sunsetWarmthColors()
        AppTheme.NATURE_MINT     -> natureMintColors()
        AppTheme.BERRY_SORBET    -> berrySorbetColors()
        AppTheme.NORDIC_SLATE    -> nordicSlateColors()
        AppTheme.MIDNIGHT_INDIGO -> midnightIndigoColors()
    }

    val colorScheme = if (theme.isDark) {
        darkColorScheme(
            primary              = colors.m3Primary,
            onPrimary            = VioletOnPrimary,
            primaryContainer     = colors.m3PrimaryContainer,
            onPrimaryContainer   = colors.m3OnPrimaryContainer,
            secondary            = colors.m3Secondary,
            onSecondary          = Color(0xFF2D2A5B),
            secondaryContainer   = colors.m3SecondaryContainer,
            onSecondaryContainer = colors.m3OnSecondaryContainer,
            tertiary             = colors.m3Tertiary,
            onTertiary           = Color(0xFF272377),
            tertiaryContainer    = colors.m3TertiaryContainer,
            onTertiaryContainer  = colors.m3OnTertiaryContainer,
            background           = colors.background,
            onBackground         = colors.mainText,
            surface              = colors.surface,
            onSurface            = colors.mainText,
            surfaceVariant       = colors.m3SurfaceVariant,
            onSurfaceVariant     = colors.m3OnSurfaceVariant,
            outline              = colors.m3Outline,
            outlineVariant       = colors.m3OutlineVariant,
            error                = colors.error,
            onError              = colors.m3OnPrimaryContainer,
            errorContainer       = colors.errorContainer,
            onErrorContainer     = MI_OnError,
            inverseSurface       = colors.inverseSurface,
            inverseOnSurface     = colors.inverseOnSurface,
            inversePrimary       = colors.m3InversePrimary,
            surfaceTint          = colors.m3Primary
        )
    } else {
        lightColorScheme(
            primary              = colors.m3Primary,
            onPrimary            = Color.White,
            primaryContainer     = colors.m3PrimaryContainer,
            onPrimaryContainer   = colors.m3OnPrimaryContainer,
            secondary            = colors.m3Secondary,
            onSecondary          = Color.White,
            secondaryContainer   = colors.m3SecondaryContainer,
            onSecondaryContainer = colors.m3OnSecondaryContainer,
            tertiary             = colors.m3Tertiary,
            onTertiary           = Color.White,
            tertiaryContainer    = colors.m3TertiaryContainer,
            onTertiaryContainer  = colors.m3OnTertiaryContainer,
            background           = colors.background,
            onBackground         = colors.mainText,
            surface              = colors.surface,
            onSurface            = colors.mainText,
            surfaceVariant       = colors.m3SurfaceVariant,
            onSurfaceVariant     = colors.m3OnSurfaceVariant,
            outline              = colors.m3Outline,
            outlineVariant       = colors.m3OutlineVariant,
            error                = colors.error,
            onError              = Color.White,
            errorContainer       = colors.errorContainer,
            onErrorContainer     = colors.m3OnPrimaryContainer,
            inverseSurface       = colors.inverseSurface,
            inverseOnSurface     = colors.inverseOnSurface,
            inversePrimary       = colors.m3InversePrimary,
            surfaceTint          = colors.m3Primary
        )
    }

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTheme  provides theme
    ) {
        MaterialTheme(colorScheme = colorScheme, content = content)
    }
}

package com.aienglish.feature.profile.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.core.designsystem.theme.AppTheme
import com.aienglish.feature.profile.ui.viewmodel.ProfileState
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel

@Composable
internal fun ThemeSection(
    state: ProfileState,
    viewModel: ProfileViewModel,
    colors: AppColors
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                Icons.Rounded.Palette,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(20.dp)
            )
            Text(
                "Giao diện",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.mainText
            )
        }

        Text(
            "Chọn bảng màu và phong cách giao diện cho app",
            fontSize = 13.sp,
            color = colors.mutedText,
            lineHeight = 18.sp
        )

        AppTheme.entries.forEach { theme ->
            ThemeOptionCard(
                theme = theme,
                isSelected = state.appTheme == theme,
                colors = colors,
                onSelect = { viewModel.onThemeChanged(theme) }
            )
        }
    }
}

@Composable
internal fun ThemeOptionCard(
    theme: AppTheme,
    isSelected: Boolean,
    colors: AppColors,
    onSelect: () -> Unit
) {
    val borderColor = if (isSelected) colors.accent else colors.border
    val bgColor     = if (isSelected) colors.accent.copy(alpha = 0.06f) else colors.surface

    val swatches = when (theme) {
        AppTheme.SUNSET_WARMTH   -> listOf(
            Color(0xFF944A00), Color(0xFFE67E22), Color(0xFFFFF8F6), Color(0xFFFFDCC5)
        )
        AppTheme.NATURE_MINT     -> listOf(
            Color(0xFF006D36), Color(0xFF50C878), Color(0xFFF8FAF8), Color(0xFF83FBA5)
        )
        AppTheme.BERRY_SORBET    -> listOf(
            Color(0xFFB60E3D), Color(0xFFDA3054), Color(0xFFFFF8F7), Color(0xFFFEB2C0)
        )
        AppTheme.NORDIC_SLATE    -> listOf(
            Color(0xFF162839), Color(0xFF2C3E50), Color(0xFFF7FAF9), Color(0xFFB5C8DF)
        )
        AppTheme.MIDNIGHT_INDIGO -> listOf(
            Color(0xFF101415), Color(0xFF1D2022), Color(0xFFD0BCFF), Color(0xFFA078FF)
        )
    }

    val description = when (theme) {
        AppTheme.SUNSET_WARMTH   -> "Ấm áp • Terracotta & Kem"
        AppTheme.NATURE_MINT     -> "Tươi mát • Xanh Mint & Trắng"
        AppTheme.BERRY_SORBET    -> "Năng động • Berry Đỏ & Kem Hồng"
        AppTheme.NORDIC_SLATE    -> "Tối giản • Slate Xám & Tuyết"
        AppTheme.MIDNIGHT_INDIGO -> "Dark Mode • Violet & Deep Space"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onSelect)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
            swatches.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(1.5.dp, colors.border.copy(alpha = 0.4f), CircleShape)
                )
            }
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                "${theme.emoji} ${theme.displayName}",
                fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = colors.mainText
            )
            Text(description, fontSize = 12.sp, color = colors.mutedText)
        }

        if (isSelected) {
            Icon(
                Icons.Rounded.CheckCircle,
                contentDescription = "Đang dùng",
                tint = colors.accent,
                modifier = Modifier.size(22.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, colors.border, CircleShape)
            )
        }
    }
}

package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.ui.viewmodel.LevelType
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel

@Composable
internal fun TargetLevelSystem(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    val currentType = state.levelType
    val selectedLevel = state.selectedLevels[currentType] ?: currentType.defaultLevel
    val topicCount = state.topicCounts[selectedLevel] ?: 0

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surface.copy(alpha = 0.75f),
        border = BorderStroke(1.dp, colors.border.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ── 1. Header: Tiêu đề & Bộ chuyển thang đo trên cùng 1 hàng ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.accent.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.TrackChanges,
                            contentDescription = null,
                            tint = colors.accent,
                            modifier = Modifier.size(15.dp)
                        )
                    }

                    Text(
                        text = "Trình độ mục tiêu",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mainText
                    )
                }

                // Bộ chuyển thang điểm tối giản
                when (state.targetLanguage) {
                    "es" -> DualScaleTabs(
                        sys1 = LevelType.DELE, label1 = "DELE",
                        sys2 = LevelType.SIELE, label2 = "SIELE",
                        currentSys = currentType,
                        onSelect = { viewModel.onLevelTypeSelected(it) },
                        colors = colors
                    )
                    "fr" -> DualScaleTabs(
                        sys1 = LevelType.DELF, label1 = "DELF",
                        sys2 = LevelType.DALF, label2 = "DALF",
                        currentSys = currentType,
                        onSelect = { viewModel.onLevelTypeSelected(it) },
                        colors = colors
                    )
                    "de" -> DualScaleTabs(
                        sys1 = LevelType.GOETHE, label1 = "Goethe",
                        sys2 = LevelType.TESTDAF, label2 = "TestDaF",
                        currentSys = currentType,
                        onSelect = { viewModel.onLevelTypeSelected(it) },
                        colors = colors
                    )
                    "ja", "zh", "ko", "vi", "hi" -> SingleScaleTag(currentType, colors)
                    else -> DualScaleTabs(
                        sys1 = LevelType.CEFR, label1 = "CEFR",
                        sys2 = LevelType.TOEIC, label2 = "TOEIC",
                        currentSys = currentType,
                        onSelect = { viewModel.onLevelTypeSelected(it) },
                        colors = colors
                    )
                }
            }

            // ── 2. Dải các lựa chọn cấp độ (Level Chips Row) ──
            LevelSystemRow(
                levels = currentType.levels,
                colors = colors,
                isSelected = { it == selectedLevel },
                onClick = { viewModel.onLevelSelected(it) },
                displayTransform = { level -> getLevelDisplayLabel(currentType, level) },
                spacing = if (currentType.levels.size >= 6) 6.dp else 8.dp,
                fontSize = when {
                    currentType == LevelType.VIET -> 11.sp
                    currentType.levels.size >= 6 -> 12.sp
                    else -> 13.sp
                }
            )

            // ── 3. Dòng tóm tắt thông tin cần thiết nhất ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.EmojiEvents,
                        contentDescription = null,
                        tint = colors.accent,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${getLevelFullDisplayName(currentType, selectedLevel)} · ${getLevelShortMeaning(currentType, selectedLevel)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.mainText
                    )
                }

                if (topicCount > 0) {
                    Text(
                        text = "$topicCount chủ đề",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = colors.accent
                    )
                }
            }
        }
    }
}

@Composable
private fun DualScaleTabs(
    sys1: LevelType, label1: String,
    sys2: LevelType, label2: String,
    currentSys: LevelType,
    onSelect: (LevelType) -> Unit,
    colors: AppColors
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surfaceContainer)
            .border(BorderStroke(1.dp, colors.border.copy(alpha = 0.4f)), RoundedCornerShape(8.dp))
            .padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        SegmentTab(
            text = label1,
            isSelected = currentSys == sys1,
            onClick = { onSelect(sys1) },
            colors = colors
        )
        SegmentTab(
            text = label2,
            isSelected = currentSys == sys2,
            onClick = { onSelect(sys2) },
            colors = colors
        )
    }
}

@Composable
private fun SegmentTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: AppColors
) {
    val bg = if (isSelected) colors.surface else Color.Transparent
    val borderModifier = if (isSelected) {
        Modifier.border(BorderStroke(1.dp, colors.border.copy(alpha = 0.5f)), RoundedCornerShape(6.dp))
    } else Modifier

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bg)
            .then(borderModifier)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) colors.accent else colors.mutedText
        )
    }
}

@Composable
private fun SingleScaleTag(currentType: LevelType, colors: AppColors) {
    val tagText = when (currentType) {
        LevelType.JLPT -> "JLPT"
        LevelType.HSK -> "HSK"
        LevelType.TOPIK -> "TOPIK"
        LevelType.VIET -> "Giáo dục VN"
        LevelType.HINDI -> "Hindi"
        else -> currentType.name
    }
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = colors.surfaceContainer,
        border = BorderStroke(1.dp, colors.border.copy(alpha = 0.4f))
    ) {
        Text(
            text = tagText,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.mutedText,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun LevelSystemRow(
    levels: List<String>,
    colors: AppColors,
    isSelected: (String) -> Boolean,
    onClick: (String) -> Unit,
    displayTransform: (String) -> String = { it },
    spacing: androidx.compose.ui.unit.Dp = 8.dp,
    fontSize: TextUnit = 13.sp
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        levels.forEach { level ->
            LevelChip(
                text = displayTransform(level),
                isSelected = isSelected(level),
                onClick = { onClick(level) },
                colors = colors,
                fontSize = fontSize
            )
        }
    }
}

@Composable
private fun RowScope.LevelChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: AppColors,
    fontSize: TextUnit = 13.sp
) {
    val gradientBg = if (isSelected) {
        Brush.verticalGradient(
            colors = listOf(
                colors.accent,
                colors.accentDark
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                colors.surfaceElevated,
                colors.surfaceElevated
            )
        )
    }

    val borderColor = if (isSelected) {
        colors.accentHighlight.copy(alpha = 0.8f)
    } else {
        colors.border.copy(alpha = 0.6f)
    }

    val mainTextColor = if (isSelected) Color.White else colors.mainText

    Box(
        modifier = Modifier
            .weight(1f)
            .height(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(gradientBg)
            .border(BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor), RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = mainTextColor,
            fontSize = fontSize,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun getLevelDisplayLabel(levelType: LevelType, level: String): String {
    return when (levelType) {
        LevelType.TOEIC -> if (level == "900") "900+" else level
        LevelType.HSK -> level.replace("HSK", "H")
        LevelType.TESTDAF -> level.replace("TDN", "T")
        LevelType.TOPIK -> level.replace("TOPIK", "T")
        LevelType.VIET -> when (level) {
            "TIEU_HOC" -> "Tiểu học"
            "TRUNG_HOC" -> "Trung học"
            "DAI_HOC" -> "Đại học"
            else -> level
        }
        else -> level
    }
}

private fun getLevelFullDisplayName(levelType: LevelType, level: String): String {
    return when (levelType) {
        LevelType.TOEIC -> if (level == "900") "TOEIC 900+" else "TOEIC $level"
        LevelType.HSK -> level.replace("HSK", "HSK ")
        LevelType.TOPIK -> level.replace("TOPIK", "TOPIK ")
        LevelType.VIET -> when (level) {
            "TIEU_HOC" -> "Tiểu học"
            "TRUNG_HOC" -> "Trung học"
            "DAI_HOC" -> "Đại học"
            else -> level
        }
        LevelType.TESTDAF -> level.replace("TDN", "TestDaF ")
        else -> level
    }
}

private fun getLevelShortMeaning(levelType: LevelType, level: String): String {
    return when (levelType) {
        LevelType.CEFR, LevelType.DELE, LevelType.DELF, LevelType.GOETHE -> when (level) {
            "A1" -> "Mới bắt đầu"
            "A2" -> "Sơ cấp"
            "B1" -> "Trung cấp"
            "B2" -> "Trung cấp cao"
            "C1" -> "Nâng cao"
            "C2" -> "Thành thạo"
            else -> level
        }
        LevelType.SIELE -> when (level) {
            "A1" -> "Mới bắt đầu"
            "A2" -> "Sơ cấp"
            "B1" -> "Trung cấp"
            "B2" -> "Trung cấp cao"
            "C1" -> "Nâng cao"
            else -> level
        }
        LevelType.DALF -> when (level) {
            "C1" -> "Nâng cao"
            "C2" -> "Thành thạo"
            else -> level
        }
        LevelType.TESTDAF -> when (level) {
            "TDN3" -> "Dự bị đại học"
            "TDN4" -> "Đủ chuẩn đại học"
            "TDN5" -> "Học thuật xuất sắc"
            else -> level
        }
        LevelType.TOEIC -> when (level) {
            "300" -> "Căn bản"
            "500" -> "Giao tiếp hàng ngày"
            "700" -> "Đi làm & Đàm phán"
            "900" -> "Thành thạo chuyên nghiệp"
            else -> level
        }
        LevelType.JLPT -> when (level) {
            "N5" -> "Nhập môn"
            "N4" -> "Sơ cấp"
            "N3" -> "Trung cấp"
            "N2" -> "Trung cấp cao"
            "N1" -> "Cao cấp"
            else -> level
        }
        LevelType.HSK -> when (level) {
            "HSK1" -> "Nhập môn"
            "HSK2" -> "Sơ cấp"
            "HSK3" -> "Trung cấp"
            "HSK4" -> "Giao tiếp tốt"
            "HSK5" -> "Nâng cao"
            "HSK6" -> "Thông thạo"
            else -> level
        }
        LevelType.TOPIK -> when (level) {
            "TOPIK1" -> "Nhập môn"
            "TOPIK2" -> "Sơ cấp"
            "TOPIK3" -> "Trung cấp 1"
            "TOPIK4" -> "Trung cấp 2"
            "TOPIK5" -> "Cao cấp 1"
            "TOPIK6" -> "Cao cấp 2"
            else -> level
        }
        LevelType.VIET -> when (level) {
            "TIEU_HOC" -> "Cơ bản & Đời sống"
            "TRUNG_HOC" -> "Mở rộng & Xã hội"
            "DAI_HOC" -> "Chuyên sâu & Học thuật"
            else -> level
        }
        LevelType.HINDI -> when (level) {
            "A1" -> "Mới bắt đầu"
            "A2" -> "Sơ cấp"
            "B1" -> "Trung cấp"
            "B2" -> "Trung cấp cao"
            else -> level
        }
    }
}

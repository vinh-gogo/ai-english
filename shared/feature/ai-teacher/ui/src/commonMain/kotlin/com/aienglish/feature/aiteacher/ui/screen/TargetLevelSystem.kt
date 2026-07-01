package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Thang điểm mục tiêu",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mutedText
            )

            when (state.targetLanguage) {
                "ja" -> SingleLevelSystemLabel("JLPT", colors)
                "zh" -> SingleLevelSystemLabel("HSK", colors)
                "ko" -> SingleLevelSystemLabel("TOPIK", colors)
                "vi" -> SingleLevelSystemLabel("Tiếng Việt", colors)
                "hi" -> SingleLevelSystemLabel("Hindi", colors)
                "es" -> DualLevelSystemSelector(
                    LevelType.DELE, "DELE",
                    LevelType.SIELE, "SIELE",
                    state.levelType, viewModel, colors
                )
                "fr" -> DualLevelSystemSelector(
                    LevelType.DELF, "DELF",
                    LevelType.DALF, "DALF",
                    state.levelType, viewModel, colors
                )
                "de" -> DualLevelSystemSelector(
                    LevelType.GOETHE, "Goethe",
                    LevelType.TESTDAF, "TestDaF",
                    state.levelType, viewModel, colors
                )
                else -> DualLevelSystemSelector(
                    LevelType.CEFR, "CEFR",
                    LevelType.TOEIC, "TOEIC",
                    state.levelType, viewModel, colors
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        when (state.levelType) {
            LevelType.CEFR -> LevelSystemRow(
                levels = listOf("A1", "A2", "B1", "B2", "C1", "C2"),
                state = state, colors = colors,
                isSelected = { it == state.selectedCefrLevel },
                onClick = { viewModel.onCefrLevelSelected(it) },
                spacing = 6.dp
            )
            LevelType.TOEIC -> LevelSystemRow(
                levels = listOf("300", "500", "700", "900"),
                state = state, colors = colors,
                isSelected = { it == state.selectedToeicLevel },
                onClick = { viewModel.onToeicLevelSelected(it) },
                displayTransform = { if (it == "900") ">900" else it }
            )
            LevelType.JLPT -> LevelSystemRow(
                levels = listOf("N5", "N4", "N3", "N2", "N1"),
                state = state, colors = colors,
                isSelected = { it == state.selectedJlptLevel },
                onClick = { viewModel.onJlptLevelSelected(it) }
            )
            LevelType.HSK -> LevelSystemRow(
                levels = listOf("HSK1", "HSK2", "HSK3", "HSK4", "HSK5", "HSK6"),
                state = state, colors = colors,
                isSelected = { it == state.selectedHskLevel },
                onClick = { viewModel.onHskLevelSelected(it) },
                displayTransform = { it.replace("HSK", "H") },
                spacing = 6.dp, fontSize = 12.sp
            )
            LevelType.DELE -> LevelSystemRow(
                levels = listOf("A1", "A2", "B1", "B2", "C1", "C2"),
                state = state, colors = colors,
                isSelected = { it == state.selectedDeleLevel },
                onClick = { viewModel.onDeleLevelSelected(it) },
                spacing = 6.dp
            )
            LevelType.SIELE -> LevelSystemRow(
                levels = listOf("A1", "A2", "B1", "B2", "C1"),
                state = state, colors = colors,
                isSelected = { it == state.selectedSieleLevel },
                onClick = { viewModel.onSieleLevelSelected(it) }
            )
            LevelType.HINDI -> LevelSystemRow(
                levels = listOf("A1", "A2", "B1", "B2"),
                state = state, colors = colors,
                isSelected = { it == state.selectedHindiLevel },
                onClick = { viewModel.onHindiLevelSelected(it) }
            )
            LevelType.DELF -> LevelSystemRow(
                levels = listOf("A1", "A2", "B1", "B2"),
                state = state, colors = colors,
                isSelected = { it == state.selectedDelfLevel },
                onClick = { viewModel.onDelfLevelSelected(it) }
            )
            LevelType.DALF -> LevelSystemRow(
                levels = listOf("C1", "C2"),
                state = state, colors = colors,
                isSelected = { it == state.selectedDalfLevel },
                onClick = { viewModel.onDalfLevelSelected(it) }
            )
            LevelType.GOETHE -> LevelSystemRow(
                levels = listOf("A1", "A2", "B1", "B2", "C1", "C2"),
                state = state, colors = colors,
                isSelected = { it == state.selectedGoetheLevel },
                onClick = { viewModel.onGoetheLevelSelected(it) },
                spacing = 6.dp
            )
            LevelType.TESTDAF -> LevelSystemRow(
                levels = listOf("TDN3", "TDN4", "TDN5"),
                state = state, colors = colors,
                isSelected = { it == state.selectedTestdafLevel },
                onClick = { viewModel.onTestdafLevelSelected(it) },
                displayTransform = { it.replace("TDN", "T") }
            )
            LevelType.TOPIK -> LevelSystemRow(
                levels = listOf("TOPIK1", "TOPIK2", "TOPIK3", "TOPIK4", "TOPIK5", "TOPIK6"),
                state = state, colors = colors,
                isSelected = { it == state.selectedTopikLevel },
                onClick = { viewModel.onTopikLevelSelected(it) },
                displayTransform = { it.replace("TOPIK", "T") },
                spacing = 6.dp, fontSize = 12.sp
            )
            LevelType.VIET -> LevelSystemRow(
                levels = listOf("TIEU_HOC", "TRUNG_HOC", "DAI_HOC"),
                state = state, colors = colors,
                isSelected = { it == state.selectedVietLevel },
                onClick = { viewModel.onVietLevelSelected(it) },
                displayTransform = { 
                    when(it) {
                        "TIEU_HOC" -> "TH"
                        "TRUNG_HOC" -> "THCS"
                        "DAI_HOC" -> "ĐH"
                        else -> it
                    }
                }
            )
        }
    }
}

@Composable
private fun LevelSystemRow(
    levels: List<String>,
    state: TeacherUiState,
    colors: AppColors,
    isSelected: (String) -> Boolean,
    onClick: (String) -> Unit,
    displayTransform: (String) -> String = { it },
    spacing: androidx.compose.ui.unit.Dp = 8.dp,
    fontSize: TextUnit = 13.sp
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
        levels.forEach { level ->
            LevelChip(
                text = displayTransform(level),
                count = state.topicCounts[level],
                isSelected = isSelected(level),
                onClick = { onClick(level) },
                colors = colors,
                fontSize = fontSize
            )
        }
    }
}

@Composable
private fun SingleLevelSystemLabel(label: String, colors: AppColors) {
    Text(
        text = label,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = colors.hintText,
        modifier = Modifier
            .background(colors.surfaceElevated, RoundedCornerShape(4.dp))
            .border(1.dp, colors.border, RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}

@Composable
private fun DualLevelSystemSelector(
    sys1: LevelType, label1: String,
    sys2: LevelType, label2: String,
    currentSys: LevelType,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    Row(
        modifier = Modifier
            .background(colors.surfaceElevated, RoundedCornerShape(6.dp))
            .border(1.dp, colors.border, RoundedCornerShape(6.dp))
            .padding(2.dp)
    ) {
        val isSys1 = currentSys == sys1
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (isSys1) colors.accent else Color.Transparent)
                .clickable { viewModel.onLevelTypeSelected(sys1) }
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                label1,
                fontSize = 10.sp,
                fontWeight = if (isSys1) FontWeight.Bold else FontWeight.Medium,
                color = if (isSys1) Color.White else colors.hintText
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (!isSys1) colors.accent else Color.Transparent)
                .clickable { viewModel.onLevelTypeSelected(sys2) }
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(
                label2,
                fontSize = 10.sp,
                fontWeight = if (!isSys1) FontWeight.Bold else FontWeight.Medium,
                color = if (!isSys1) Color.White else colors.hintText
            )
        }
    }
}

@Composable
private fun RowScope.LevelChip(
    text: String,
    count: Int?,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: AppColors,
    fontSize: TextUnit = 13.sp
) {
    val bgColor = if (isSelected) colors.accent else colors.surfaceElevated
    val textColor = if (isSelected) Color.White else colors.mainText
    val borderColor = if (isSelected) colors.accent else colors.border

    Box(
        modifier = Modifier
            .weight(1f)
            .height(44.dp)
            .background(bgColor, RoundedCornerShape(10.dp))
            .border(BorderStroke(1.dp, borderColor), RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Text(
                text = text,
                color = textColor,
                fontSize = fontSize,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
            if (count != null && count > 0) {
                Text(
                    text = "$count",
                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else colors.mutedText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
            LevelType.CEFR -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("A1", "A2", "B1", "B2", "C1", "C2").forEach { level ->
                        LevelChip(
                            text = level,
                            isSelected = level == state.selectedCefrLevel,
                            onClick = { viewModel.onCefrLevelSelected(level) },
                            colors = colors
                        )
                    }
                }
            }
            LevelType.TOEIC -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("300", "500", "700", "900").forEach { level ->
                        LevelChip(
                            text = if (level == "900") ">900" else level,
                            isSelected = level == state.selectedToeicLevel,
                            onClick = { viewModel.onToeicLevelSelected(level) },
                            colors = colors
                        )
                    }
                }
            }
            LevelType.JLPT -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("N5", "N4", "N3", "N2", "N1").forEach { level ->
                        LevelChip(
                            text = level,
                            isSelected = level == state.selectedJlptLevel,
                            onClick = { viewModel.onJlptLevelSelected(level) },
                            colors = colors
                        )
                    }
                }
            }
            LevelType.HSK -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("HSK1", "HSK2", "HSK3", "HSK4", "HSK5", "HSK6").forEach { level ->
                        LevelChip(
                            text = level.replace("HSK", "H"),
                            isSelected = level == state.selectedHskLevel,
                            onClick = { viewModel.onHskLevelSelected(level) },
                            colors = colors,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            LevelType.DELE -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("A1", "A2", "B1", "B2", "C1", "C2").forEach { level ->
                        LevelChip(
                            text = level,
                            isSelected = level == state.selectedDeleLevel,
                            onClick = { viewModel.onDeleLevelSelected(level) },
                            colors = colors
                        )
                    }
                }
            }
            LevelType.SIELE -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("A1", "A2", "B1", "B2", "C1").forEach { level ->
                        LevelChip(
                            text = level,
                            isSelected = level == state.selectedSieleLevel,
                            onClick = { viewModel.onSieleLevelSelected(level) },
                            colors = colors
                        )
                    }
                }
            }
            LevelType.HINDI -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("A1", "A2", "B1", "B2").forEach { level ->
                        LevelChip(
                            text = level,
                            isSelected = level == state.selectedHindiLevel,
                            onClick = { viewModel.onHindiLevelSelected(level) },
                            colors = colors
                        )
                    }
                }
            }
            LevelType.DELF -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("A1", "A2", "B1", "B2").forEach { level ->
                        LevelChip(
                            text = level,
                            isSelected = level == state.selectedDelfLevel,
                            onClick = { viewModel.onDelfLevelSelected(level) },
                            colors = colors
                        )
                    }
                }
            }
            LevelType.DALF -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("C1", "C2").forEach { level ->
                        LevelChip(
                            text = level,
                            isSelected = level == state.selectedDalfLevel,
                            onClick = { viewModel.onDalfLevelSelected(level) },
                            colors = colors
                        )
                    }
                }
            }
            LevelType.GOETHE -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("A1", "A2", "B1", "B2", "C1", "C2").forEach { level ->
                        LevelChip(
                            text = level,
                            isSelected = level == state.selectedGoetheLevel,
                            onClick = { viewModel.onGoetheLevelSelected(level) },
                            colors = colors
                        )
                    }
                }
            }
            LevelType.TESTDAF -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("TDN3", "TDN4", "TDN5").forEach { level ->
                        LevelChip(
                            text = level,
                            isSelected = level == state.selectedTestdafLevel,
                            onClick = { viewModel.onTestdafLevelSelected(level) },
                            colors = colors
                        )
                    }
                }
            }
            LevelType.TOPIK -> {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("TOPIK1", "TOPIK2", "TOPIK3", "TOPIK4", "TOPIK5", "TOPIK6").forEach { level ->
                        LevelChip(
                            text = level.replace("TOPIK", "T"),
                            isSelected = level == state.selectedTopikLevel,
                            onClick = { viewModel.onTopikLevelSelected(level) },
                            colors = colors,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SingleLevelSystemLabel(text: String, colors: AppColors) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = colors.accent
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun DualLevelSystemSelector(
    type1: LevelType, name1: String,
    type2: LevelType, name2: String,
    currentType: LevelType,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surfaceElevated)
            .padding(2.dp)
    ) {
        LevelTypeSelectorOption(name1, currentType == type1, { viewModel.onLevelTypeSelected(type1) }, colors)
        LevelTypeSelectorOption(name2, currentType == type2, { viewModel.onLevelTypeSelected(type2) }, colors)
    }
}

@Composable
private fun LevelTypeSelectorOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: AppColors
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) colors.accent else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else colors.mutedText
        )
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
    Surface(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) colors.accent else colors.surface,
        border = BorderStroke(1.dp, if (isSelected) colors.accent else colors.border)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = if (isSelected) Color.White else colors.mainText,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.domain.model.TopicItem
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel

@Composable
internal fun TopicCarouselCard(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors,
    selectedIdx: Int,
    filteredTopics: List<Pair<Int, TopicItem>>,
    currentTopic: TopicItem?
) {
    val currentTopicIndex = remember(selectedIdx, filteredTopics) {
        filteredTopics.indexOfFirst { it.first == selectedIdx }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (!state.isCustomTopicMode) {
                val prevIdx = remember(currentTopicIndex, filteredTopics) {
                    if (currentTopicIndex != -1 && filteredTopics.isNotEmpty())
                        filteredTopics[(currentTopicIndex - 1 + filteredTopics.size) % filteredTopics.size].first
                    else selectedIdx
                }
                IconButton(onClick = { viewModel.onTopicIndexSelected(prevIdx) }, modifier = Modifier.padding(start = 4.dp)) {
                    Icon(Icons.Default.ChevronLeft, "Trước", tint = colors.mainText)
                }
            } else {
                Spacer(Modifier.width(48.dp))
            }

            Column(
                modifier = Modifier.weight(1f).fillMaxHeight().padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val localIndexText = remember(currentTopicIndex, filteredTopics) {
                    if (currentTopicIndex != -1) "${currentTopicIndex + 1} / ${filteredTopics.size}" else "1 / 1"
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        if (state.isCustomTopicMode) "CHỦ ĐỀ TỰ CHỌN" else "CHỦ ĐỀ $localIndexText",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.accent
                    )
                }
                
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (state.isCustomTopicMode) {
                            if (state.customTopicInput.isBlank()) "Hãy nhập chủ đề tự do..." else state.customTopicInput
                        } else {
                            currentTopic?.name ?: ""
                        },
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (state.isCustomTopicMode && state.customTopicInput.isBlank()) colors.hintText else colors.mainText,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            if (!state.isCustomTopicMode) {
                val nextIdx = remember(currentTopicIndex, filteredTopics) {
                    if (currentTopicIndex != -1 && filteredTopics.isNotEmpty())
                        filteredTopics[(currentTopicIndex + 1) % filteredTopics.size].first
                    else selectedIdx
                }
                IconButton(onClick = { viewModel.onTopicIndexSelected(nextIdx) }, modifier = Modifier.padding(end = 4.dp)) {
                    Icon(Icons.Default.ChevronRight, "Sau", tint = colors.mainText)
                }
            } else {
                Spacer(Modifier.width(48.dp))
            }
        }
    }
}

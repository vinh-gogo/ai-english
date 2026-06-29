package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.domain.model.TopicItem
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState

@Composable
internal fun TopicCarouselCard(
    state: TeacherUiState,
    colors: AppColors,
    currentTopic: TopicItem?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.5.dp, colors.accent.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "CHỦ ĐỀ ĐANG CHỌN",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.accent
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = if (state.isCustomTopicMode) {
                        if (state.customTopicInput.isBlank()) "Hãy nhập chủ đề tự do bên dưới..." else state.customTopicInput
                    } else {
                        currentTopic?.name ?: "Chưa chọn chủ đề"
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.isCustomTopicMode && state.customTopicInput.isBlank()) colors.hintText else colors.mainText,
                    maxLines = 3
                )
            }

        }
    }
}

package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
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
    currentTopic: TopicItem?,
    onCustomTopicInputChanged: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit = {},
    onAddClick: () -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(
            width = if (state.isCustomTopicMode && isFocused) 2.dp else 1.5.dp,
            color = if (state.isCustomTopicMode && isFocused) colors.accent else colors.accent.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.isCustomTopicMode) {
                    IconButton(
                        onClick = onAddClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null,
                            tint = if (isFocused) colors.accent else colors.hintText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(Modifier.width(6.dp))
                }
                Text(
                    text = "CHỦ ĐỀ ĐANG CHỌN",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.accent
                )
            }

            Spacer(Modifier.height(4.dp))
            if (state.isCustomTopicMode) {
                BasicTextField(
                    value = state.customTopicInput,
                    onValueChange = onCustomTopicInputChanged,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = colors.mainText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    cursorBrush = SolidColor(colors.accent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            isFocused = it.isFocused
                            onFocusChanged(it.isFocused)
                        },
                    decorationBox = { innerTextField ->
                        if (state.customTopicInput.isEmpty()) {
                            Text(
                                text = "Hãy nhập chủ đề tự do...",
                                color = colors.hintText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        innerTextField()
                    }
                )
            } else {
                val topicNameText = currentTopic?.name ?: "Chưa chọn chủ đề"
                Text(
                    text = buildInlineAnnotatedString(topicNameText, colors),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.mainText,
                    maxLines = 3
                )
            }
        }
    }
}





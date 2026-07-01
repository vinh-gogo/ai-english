package com.aienglish.feature.aiteacher.ui.screen.components.topic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun TopicActionHeader(
    isCustomTopicMode: Boolean,
    topicSearchQuery: String,
    filteredTopicsCount: Int,
    onRandomClick: () -> Unit,
    onToggleCustomMode: () -> Unit,
    colors: AppColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isCustomTopicMode) "Nhập chủ đề tùy chỉnh"
            else if (topicSearchQuery.isEmpty()) "Các chủ đề"
            else "Kết quả tìm kiếm ($filteredTopicsCount)",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = colors.mutedText
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!isCustomTopicMode && filteredTopicsCount > 0) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onRandomClick() }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = colors.accent
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Ngẫu nhiên",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.accent
                    )
                }
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { onToggleCustomMode() }
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isCustomTopicMode) Icons.Rounded.List else Icons.Rounded.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = colors.accent
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = if (isCustomTopicMode) "Chọn từ danh sách" else "Tự nhập",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.accent
                )
            }
        }
    }
}

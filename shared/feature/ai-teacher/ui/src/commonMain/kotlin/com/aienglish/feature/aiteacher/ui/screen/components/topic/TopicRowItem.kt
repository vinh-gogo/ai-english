package com.aienglish.feature.aiteacher.ui.screen.components.topic

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.rounded.AutoAwesome
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
import com.aienglish.feature.aiteacher.domain.model.TopicItem
import com.aienglish.feature.aiteacher.ui.screen.buildInlineAnnotatedString

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopicRowItem(
    topic: TopicItem,
    i: Int,
    isSelected: Boolean,
    isTtsLoading: Boolean,
    isLongPressed: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onDismissLongClick: () -> Unit,
    onSpeak: () -> Unit,
    onRemove: () -> Unit,
    colors: AppColors
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) colors.surfaceElevated else colors.surface
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) colors.accent else colors.border
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Rectangular index badge
            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) colors.accent else colors.border.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 0.dp,
                            bottomEnd = 4.dp,
                            bottomStart = 0.dp
                        )
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${i + 1}",
                        color = if (isSelected) Color.White else colors.mutedText,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (topic.isAiRecommended) {
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesome,
                            contentDescription = "AI Suggested",
                            tint = if (isSelected) Color.White else colors.accent,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                }
            }

            // Play audio button
            IconButton(
                onClick = onSpeak,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 12.dp)
                    .size(28.dp)
            ) {
                if (isTtsLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        strokeWidth = 2.dp,
                        color = if (isSelected) colors.accent else colors.mutedText
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Nghe chu de",
                        tint = if (isSelected) colors.accent else colors.mutedText,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Full topic name wrapping to multiple lines
            Text(
                text = buildInlineAnnotatedString(topic.name, colors),
                color = colors.mainText,
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                fontSize = 13.5.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
            )

            // Overlay with Remove button in the center
            if (isLongPressed) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(colors.background.copy(alpha = 0.85f))
                        .clickable { onDismissLongClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onRemove,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.error
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Xóa",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

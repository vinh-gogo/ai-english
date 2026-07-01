package com.aienglish.feature.aiteacher.ui.screen.components.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.ui.screen.MarkdownText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeedbackReviewCard(
    review: String,
    colors: AppColors,
    modifier: Modifier = Modifier,
    addedTopics: List<String> = emptyList(),
    onAddTopic: ((String) -> Unit)? = null,
    onRetry: (() -> Unit)? = null
) {
    val clipboard = LocalClipboardManager.current
    // Parse recommended topics from the review string
    val topicRegex = Regex("\\[TOPIC_RECOMMEND\\](.*?)\\[/TOPIC_RECOMMEND\\]", setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL))
    val recommendedTopics = topicRegex.findAll(review).map { it.groupValues[1].trim() }.toList()
    val cleanedReview = review.replace(topicRegex, "").trim()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Nhận xét từ AI Lingua", 
                    color = colors.accent, 
                    fontSize = 13.sp, 
                    fontWeight = FontWeight.Bold
                )
                Row {
                    androidx.compose.material3.IconButton(
                        onClick = {
                            clipboard.setText(AnnotatedString(review))
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Rounded.ContentCopy,
                            contentDescription = "Sao chép",
                            tint = colors.accent
                        )
                    }
                    if (onRetry != null) {
                        Spacer(Modifier.width(8.dp))
                        androidx.compose.material3.IconButton(
                            onClick = onRetry,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = androidx.compose.material.icons.Icons.Rounded.Refresh,
                                contentDescription = "Đánh giá lại",
                                tint = colors.accent
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            MarkdownText(cleanedReview, colors)
            
            if (recommendedTopics.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text(
                    "Đề xuất học tiếp để cải thiện:", 
                    color = colors.mainText, 
                    fontSize = 13.sp, 
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    recommendedTopics.forEach { topicName ->
                        val isAdded = addedTopics.contains(topicName)
                        
                        OutlinedButton(
                            onClick = { if (!isAdded) onAddTopic?.invoke(topicName) },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (isAdded) colors.mutedText else colors.accent
                            ),
                            border = BorderStroke(1.dp, if (isAdded) colors.border else colors.accent),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isAdded) Icons.Rounded.Check else Icons.Rounded.Add,
                                    contentDescription = if (isAdded) "Đã thêm" else "Thêm",
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isAdded) colors.mutedText else colors.accent
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = topicName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.core.designsystem.theme.LocalAppColors
import com.aienglish.feature.aiteacher.domain.model.HistoryStatus
import com.aienglish.feature.aiteacher.domain.model.LessonHistory
import com.aienglish.feature.aiteacher.ui.viewmodel.HistoryViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorySheet(
    historyViewModel: HistoryViewModel = koinInject(),
    onDismiss: () -> Unit
) {
    val state by historyViewModel.state.collectAsState()
    val colors = LocalAppColors.current
    var showClearDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.background,
        contentColor = colors.mainText,
        dragHandle = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.width(40.dp).height(4.dp).clip(CircleShape).background(colors.border))
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.History, null,
                    tint = colors.accent,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Lịch sử luyện tập",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText,
                    modifier = Modifier.weight(1f)
                )
                if (state.items.isNotEmpty()) {
                    IconButton(onClick = { showClearDialog = true }) {
                        Icon(
                            Icons.Rounded.DeleteSweep, null,
                            tint = colors.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, null, tint = colors.hintText)
                }
            }

            HorizontalDivider(color = colors.border)

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.accent, modifier = Modifier.size(32.dp))
                    }
                }

                state.items.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(220.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(64.dp).clip(CircleShape)
                                    .background(colors.surface),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Rounded.HistoryEdu, null,
                                    tint = colors.border,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Text("Chưa có lịch sử", color = colors.mutedText, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                            Text("Bắt đầu luyện tập để lưu kết quả!", color = colors.hintText, fontSize = 13.sp)
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .navigationBarsPadding(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Summary chip
                        val completed = state.items.count { it.status == HistoryStatus.COMPLETED }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            SummaryChip("${state.items.size} bài", Icons.Rounded.MenuBook, colors.accent, colors)
                            SummaryChip("$completed hoàn thành", Icons.Rounded.CheckCircle, colors.correct, colors)
                        }

                        Spacer(Modifier.height(4.dp))

                        state.items.forEach { item ->
                            HistoryItem(item, colors)
                        }

                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            containerColor = colors.surface,
            title = {
                Text("Xóa lịch sử?", color = colors.mainText, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Toàn bộ lịch sử luyện tập sẽ bị xóa. Không thể hoàn tác.",
                    color = colors.mutedText, fontSize = 14.sp
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    historyViewModel.clearHistory()
                    showClearDialog = false
                }) {
                    Text("Xóa", color = colors.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Hủy", color = colors.mutedText)
                }
            }
        )
    }
}

@Composable
private fun SummaryChip(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    colors: AppColors
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(0.1f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(13.dp))
        Text(text, fontSize = 12.sp, color = color, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun HistoryItem(item: LessonHistory, colors: AppColors) {
    Card(
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Score / Status circle
            val isCompleted = item.status == HistoryStatus.COMPLETED
            val circleColor = if (isCompleted) colors.correct else Color(0xFFFFA726)
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(circleColor.copy(0.12f)),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${item.score ?: 0}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = circleColor
                        )
                        Text("/10", fontSize = 9.sp, color = circleColor.copy(0.7f))
                    }
                } else {
                    Icon(
                        Icons.Rounded.HourglassTop, null,
                        tint = circleColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Content
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                // Topic + Level badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        item.topic,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = colors.mainText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    // Level badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(colors.accent.copy(0.15f))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            item.level,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.accent
                        )
                    }
                }

                // Status + date
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val statusText = if (isCompleted) "✓ Hoàn thành" else "⏳ Đang học"
                    Text(
                        statusText,
                        fontSize = 12.sp,
                        color = circleColor,
                        fontWeight = FontWeight.Medium
                    )
                    Text("·", fontSize = 12.sp, color = colors.border)
                    Text(
                        formatDate(item.createdAt),
                        fontSize = 12.sp,
                        color = colors.hintText
                    )
                }
            }
        }
    }
}

private fun formatDate(dateTimeStr: String): String {
    return try {
        val parts = dateTimeStr.split("T")
        val dateParts = parts[0].split("-")   // [2026, 06, 28]
        val timePart = parts.getOrNull(1)?.take(5) ?: ""  // 04:30
        "${dateParts[2]}/${dateParts[1]} $timePart"
    } catch (e: Exception) {
        dateTimeStr
    }
}

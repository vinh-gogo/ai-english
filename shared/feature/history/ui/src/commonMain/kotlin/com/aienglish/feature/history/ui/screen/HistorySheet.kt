package com.aienglish.feature.history.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
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
import com.aienglish.feature.history.domain.model.HistoryStatus
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.ui.viewmodel.HistoryViewModel
import org.koin.compose.koinInject
import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@androidx.compose.runtime.Immutable
data class HistoryItemState(
    val id: Long,
    val topic: String,
    val level: String,
    val score: Int?,
    val status: HistoryStatus,
    val createdAt: Long,
    val completedCount: Int,
    val totalCount: Int,
    val formattedDate: String,
    val hasLessonData: Boolean,
    val rawItem: LessonHistory
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorySheet(
    onReview: (LessonHistory) -> Unit,
    onReTest: (LessonHistory) -> Unit,
    onContinue: (LessonHistory) -> Unit,
    onDismiss: () -> Unit,
    historyViewModel: HistoryViewModel = koinInject()
) {
    val state by historyViewModel.state.collectAsState()
    
    val handleReview = remember(onReview, onDismiss) {
        { item: LessonHistory ->
            onReview(item)
            onDismiss()
        }
    }
    val handleReTest = remember(onReTest, onDismiss) {
        { item: LessonHistory ->
            onReTest(item)
            onDismiss()
        }
    }
    val handleContinue = remember(onContinue, onDismiss) {
        { item: LessonHistory ->
            onContinue(item)
            onDismiss()
        }
    }

    val colors = LocalAppColors.current
    var showClearDialog by remember { mutableStateOf(false) }
    var isAnimationFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(250) // wait 250ms for sheet slide-up animation
        isAnimationFinished = true
    }

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
                !isAnimationFinished || state.isLoading -> {
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
                    val items = state.items
                    val completedCount = remember(items) { items.count { it.status == HistoryStatus.COMPLETED } }
                    val groups = remember(items) {
                        val excellent = mutableListOf<HistoryItemState>()
                        val good = mutableListOf<HistoryItemState>()
                        val average = mutableListOf<HistoryItemState>()
                        val weak = mutableListOf<HistoryItemState>()
                        val pending = mutableListOf<HistoryItemState>()

                        items.forEach { item ->
                            val uiState = HistoryItemState(
                                id = item.id,
                                topic = item.topic,
                                level = item.level,
                                score = item.score,
                                status = item.status,
                                createdAt = item.createdAt,
                                completedCount = item.completedCount,
                                totalCount = item.totalCount,
                                formattedDate = formatDate(item.createdAt),
                                hasLessonData = item.totalCount > 0,
                                rawItem = item
                            )
                            if (item.status == HistoryStatus.COMPLETED) {
                                val score = item.score ?: 0
                                when {
                                    score >= 9 -> excellent.add(uiState)
                                    score >= 7 -> good.add(uiState)
                                    score >= 5 -> average.add(uiState)
                                    else -> weak.add(uiState)
                                }
                            } else {
                                pending.add(uiState)
                            }
                        }
                        listOf(
                            GroupData("Xuất sắc (9 - 10 điểm)", excellent, colors.correct),
                            GroupData("Tốt (7 - 8 điểm)", good, colors.accent),
                            GroupData("Trung bình (5 - 6 điểm)", average, Color(0xFFFFA726)),
                            GroupData("Yếu (Dưới 5 điểm)", weak, colors.error),
                            GroupData("Chưa hoàn thành", pending, colors.mutedText)
                        )
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .navigationBarsPadding(),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Summary chip
                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SummaryChip("${items.size} bài", Icons.AutoMirrored.Rounded.MenuBook, colors.accent)
                                SummaryChip("$completedCount hoàn thành", Icons.Rounded.CheckCircle, colors.correct)
                            }
                        }

                        item {
                            Spacer(Modifier.height(4.dp))
                        }

                        groups.forEach { group ->
                            if (group.items.isNotEmpty()) {
                                item(key = "header_${group.title}") {
                                    Spacer(Modifier.height(10.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(width = 4.dp, height = 16.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(group.color)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = group.title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = group.color
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = "(${group.items.size})",
                                            fontSize = 12.sp,
                                            color = colors.mutedText,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                items(
                                    items = group.items,
                                    key = { it.id }
                                ) { itemState ->
                                    HistoryItem(
                                        itemState = itemState,
                                        colors = colors,
                                        onReview = handleReview,
                                        onReTest = handleReTest,
                                        onContinue = handleContinue
                                    )
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(16.dp))
                        }
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
    color: Color
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
private fun HistoryItem(
    itemState: HistoryItemState,
    colors: AppColors,
    onReview: (LessonHistory) -> Unit,
    onReTest: (LessonHistory) -> Unit,
    onContinue: (LessonHistory) -> Unit
) {
    val item = itemState.rawItem
    val isCompleted = itemState.status == HistoryStatus.COMPLETED
    val completed = itemState.completedCount
    val total = itemState.totalCount

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colors.surface)
            .border(1.dp, colors.border, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left Status Icon (Refined Squircle style)
            val iconColor = if (isCompleted) colors.correct else Color(0xFFFFA726)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconColor.copy(0.08f))
                    .border(0.5.dp, iconColor.copy(0.15f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(
                        Icons.Rounded.Check, null,
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Icon(
                        Icons.Rounded.HourglassBottom, null,
                        tint = iconColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Topic + Level badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = itemState.topic,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colors.mainText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    // Level badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(colors.accent.copy(0.15f))
                            .padding(horizontal = 7.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = itemState.level,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.accent
                        )
                    }
                }

                // Status + date
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val statusText = if (isCompleted) "✓ Hoàn thành" else "⏳ Đang học ($completed/$total câu)"
                    Text(
                        text = statusText,
                        fontSize = 12.sp,
                        color = iconColor,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text("·", fontSize = 12.sp, color = colors.border)
                    Text(
                        text = itemState.formattedDate,
                        fontSize = 12.sp,
                        color = colors.hintText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(colors.border.copy(alpha = 0.5f))
        )
        Spacer(Modifier.height(8.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Score Badge (căn lề bên trái)
            if (isCompleted) {
                val scoreVal = itemState.score ?: 0
                val scoreColor = when {
                    scoreVal >= 9 -> colors.correct
                    scoreVal >= 5 -> colors.warning
                    else -> colors.error
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(scoreColor.copy(0.08f))
                        .border(0.5.dp, scoreColor.copy(0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "$scoreVal/10 điểm",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = scoreColor
                    )
                }
            } else {
                Spacer(Modifier.width(1.dp))
            }

            Spacer(Modifier.weight(1f))

            // Action buttons on the right
            if (itemState.status == HistoryStatus.IN_PROGRESS && itemState.hasLessonData) {
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onContinue(item) }
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Rounded.PlayArrow, null, modifier = Modifier.size(14.dp), tint = colors.accent)
                        Spacer(Modifier.width(4.dp))
                        Text("Tiếp tục", fontSize = 12.sp, color = colors.accent, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(8.dp))
            }

            if (itemState.status == HistoryStatus.COMPLETED && itemState.hasLessonData) {
                Box(
                    modifier = Modifier
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onReview(item) }
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Rounded.Visibility, null, modifier = Modifier.size(14.dp), tint = colors.accent)
                        Spacer(Modifier.width(4.dp))
                        Text("Xem lại", fontSize = 12.sp, color = colors.accent, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(8.dp))
            }

            Box(
                modifier = Modifier
                    .height(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.accent.copy(alpha = 0.08f))
                    .clickable { onReTest(item) }
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.Rounded.Replay, null, modifier = Modifier.size(14.dp), tint = colors.accent)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (itemState.status == HistoryStatus.COMPLETED) "Luyện tập lại" else "Làm lại",
                        fontSize = 12.sp,
                        color = colors.accent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private fun formatDate(epochMs: Long): String {
    return try {
        val now = Clock.System.now().toEpochMilliseconds()
        val diffMs = now - epochMs
        val diffSec = diffMs / 1000
        val diffMin = diffSec / 60
        val diffHour = diffMin / 60

        when {
            diffMs < 0L -> "Vừa xong"
            diffSec < 60L -> "Vừa xong"
            diffMin < 60L -> "$diffMin phút trước"
            diffHour < 24L -> "$diffHour giờ trước"
            else -> {
                val instant = Instant.fromEpochMilliseconds(epochMs)
                val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
                val month = localDateTime.monthNumber.toString().padStart(2, '0')
                val hour = localDateTime.hour.toString().padStart(2, '0')
                val minute = localDateTime.minute.toString().padStart(2, '0')
                "$day/$month $hour:$minute"
            }
        }
    } catch (e: Exception) {
        "Vừa xong"
    }
}

private data class GroupData(
    val title: String,
    val items: List<HistoryItemState>,
    val color: Color
)

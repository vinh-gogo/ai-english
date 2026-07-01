package com.aienglish.feature.aiteacher.ui.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun LessonActionButtons(
    hasChecked: Boolean,
    idx: Int,
    total: Int,
    isExplaining: Boolean,
    isEvaluating: Boolean,
    colors: AppColors,
    onCheckAnswer: () -> Unit,
    onPreviousSentence: () -> Unit,
    onNextSentence: () -> Unit,
    onSubmitLesson: () -> Unit,
    onOpenNotes: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (hasChecked) {
            IconButton(
                onClick = onOpenNotes,
                modifier = Modifier
                    .background(colors.surfaceElevated, CircleShape)
                    .border(1.dp, colors.border, CircleShape)
                    .size(48.dp)
            ) {
                Icon(Icons.Default.Book, contentDescription = "Sổ tay", tint = colors.accent)
            }
            Spacer(Modifier.height(12.dp))
        }

        if (!hasChecked) {
            Row(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (idx > 0) {
                    IconButton(
                        onClick = onPreviousSentence,
                        modifier = Modifier
                            .size(48.dp)
                            .border(2.dp, colors.border, CircleShape)
                            .background(colors.surface, CircleShape)
                    ) {
                        Icon(Icons.Default.ChevronLeft, "Quay lại", tint = colors.mainText)
                    }
                }
                // Check button
                Button(
                    onClick = onCheckAnswer,
                    colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                    border = BorderStroke(2.dp, colors.border),
                    shape = CircleShape,
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text("Kiểm tra", color = Color.White)
                }
            }
        } else {
            if (idx < total - 1) {
                // Next sentence
                Row(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (idx > 0) {
                        IconButton(
                            onClick = onPreviousSentence,
                            modifier = Modifier
                                .size(48.dp)
                                .border(2.dp, colors.border, CircleShape)
                                .background(colors.surface, CircleShape)
                        ) {
                            Icon(Icons.Default.ChevronLeft, "Quay lại", tint = colors.mainText)
                        }
                    }
                    Button(
                        onClick = onNextSentence,
                        enabled = !isExplaining,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.accent,
                            disabledContainerColor = colors.accent.copy(alpha = 0.5f)
                        ),
                        border = BorderStroke(2.dp, colors.border),
                        shape = CircleShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    ) {
                        if (isExplaining) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(Modifier.width(8.dp))
                            Text("AI đang giải thích...", color = Color.White)
                        } else {
                            Text("Câu tiếp theo", color = Color.White)
                            Icon(Icons.Default.ChevronRight, null)
                        }
                    }
                }
            } else {
                // Last sentence — submit directly since explanation is auto-triggered
                Row(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (idx > 0) {
                        IconButton(
                            onClick = onPreviousSentence,
                            modifier = Modifier
                                .size(48.dp)
                                .border(2.dp, colors.border, CircleShape)
                                .background(colors.surface, CircleShape)
                        ) {
                            Icon(Icons.Default.ChevronLeft, "Quay lại", tint = colors.mainText)
                        }
                    }
                    Button(
                        onClick = onSubmitLesson,
                        enabled = !isExplaining && !isEvaluating,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colors.correct,
                            disabledContainerColor = colors.correct.copy(alpha = 0.5f)
                        ),
                        border = BorderStroke(2.dp, colors.border),
                        shape = CircleShape,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    ) {
                        if (isEvaluating) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(Modifier.width(8.dp))
                            Text("Đang đánh giá...", color = Color.White)
                        } else if (isExplaining) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(Modifier.width(8.dp))
                            Text("AI đang giải thích...", color = Color.White)
                        } else {
                            Text("Xem đánh giá", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.domain.model.EnglishSentence
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel

@Composable
fun FeedbackContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    val feedback = state.feedback ?: return
    val lesson = state.lesson

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(78.dp)) // 18dp top pad + 48dp header + 12dp bottom gap = 78dp

            Text("Kết quả của bạn", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
            Text(
                "${state.selectedTopic} · CEFR ${state.selectedCefrLevel}",
                fontSize = 12.sp,
                color = colors.hintText
            )

            Spacer(Modifier.height(14.dp))

            // Review card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Nhận xét từ AI Lingua", color = colors.accent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    MarkdownText(feedback.review, colors)
                }
            }

            // Per-sentence review
            if (lesson != null) {
                Spacer(Modifier.height(14.dp))
                Text("Chi tiết từng câu", color = colors.mutedText, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start))
                Spacer(Modifier.height(8.dp))
                lesson.sentences.forEachIndexed { i, sentence ->
                    val userAnswer = state.userAnswers[i]
                    SentenceReviewCard(i, sentence, userAnswer, state, viewModel, colors)
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(80.dp))
            Spacer(Modifier.navigationBarsPadding())
        }

        // Reset and Retry buttons
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp)
                .navigationBarsPadding()
                .padding(bottom = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { viewModel.onRetryCurrentLesson() },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.surface,
                        contentColor = colors.mainText
                    ),
                    border = BorderStroke(1.dp, colors.border),
                    shape = CircleShape
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = "Làm lại")
                    Spacer(Modifier.width(8.dp))
                    Text("Làm lại", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { viewModel.onReset() },
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                    border = BorderStroke(1.dp, colors.border),
                    shape = CircleShape
                ) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "Bài mới")
                    Spacer(Modifier.width(8.dp))
                    Text("Bài mới", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun SentenceReviewCard(
    index: Int,
    sentence: EnglishSentence,
    userAnswer: String?,
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showAlternatives by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Câu ${index + 1}", color = colors.hintText, fontSize = 11.sp)
            }
            Spacer(Modifier.height(6.dp))
            Text(sentence.vietnamese, color = colors.mainText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(6.dp))
            Text(
                "Các đáp án chuẩn:",
                color = colors.mutedText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Rounded.CheckCircle, null, tint = colors.correct, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("1) ${sentence.english}", color = colors.correct, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                    Row {
                        IconButton(onClick = { /* TODO TTS */ }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.VolumeUp, "Nghe", tint = colors.mutedText, modifier = Modifier.size(18.dp))
                        }
                        IconButton(onClick = { clipboardManager.setText(AnnotatedString(sentence.english)) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.ContentCopy, "Copy", tint = colors.mutedText, modifier = Modifier.size(16.dp))
                        }
                    }
                }
                
                if (sentence.alternatives.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAlternatives = !showAlternatives }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            if (showAlternatives) "Thu gọn" else "+ Xem thêm ${sentence.alternatives.size} cách diễn đạt khác",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.accent
                        )
                    }

                    if (showAlternatives) {
                        sentence.alternatives.forEachIndexed { altIndex, alt ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, bottom = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = highlightDifferences(sentence.english, alt, colors),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                Row {
                                    IconButton(onClick = { /* TODO TTS */ }, modifier = Modifier.size(32.dp)) {
                                        Icon(Icons.Default.VolumeUp, "Nghe", tint = colors.mutedText, modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(onClick = { clipboardManager.setText(AnnotatedString(alt)) }, modifier = Modifier.size(32.dp)) {
                                        Icon(Icons.Default.ContentCopy, "Copy", tint = colors.mutedText, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (!userAnswer.isNullOrBlank()) {
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, null, tint = colors.mutedText, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Câu trả lời của bạn: $userAnswer", color = colors.mutedText, fontSize = 14.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    isExpanded = !isExpanded
                    if (isExpanded) {
                        viewModel.onExplainSentence(index)
                    }
                },
                modifier = Modifier.height(30.dp),
                border = BorderStroke(1.dp, colors.accent.copy(0.5f)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Icon(
                    if (isExpanded) Icons.Default.ExpandLess else Icons.Default.Info,
                    null,
                    tint = colors.accent,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    if (isExpanded) "Thu gọn" else if (state.explanations.containsKey(index)) "Xem lại" else "Giải thích",
                    color = colors.accent,
                    fontSize = 11.sp
                )
            }

            if (isExpanded) {
                val explanation = state.explanations[index]
                if (state.explainingSentenceIndex == index) {
                    Spacer(Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(14.dp), color = colors.accent, strokeWidth = 1.5.dp)
                        Text("AI đang tải giải thích...", fontSize = 12.sp, color = colors.mutedText)
                    }
                } else if (explanation != null) {
                    Spacer(Modifier.height(10.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
                        border = BorderStroke(0.5.dp, colors.border)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            MarkdownText(explanation, colors)
                        }
                    }
                }
            }
        }
    }
}

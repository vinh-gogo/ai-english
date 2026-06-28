package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Score badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            feedback.score >= 8 -> colors.correct
                            feedback.score >= 5 -> colors.warning
                            else -> colors.error
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("${feedback.score}/10", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(10.dp))
            Text("Kết quả của bạn", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
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
                    Text("Nhận xét từ AI Teacher", color = colors.accent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
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
        }

        // Reset button
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding()) {
            Button(
                onClick = { viewModel.onReset() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Rounded.Refresh, null)
                Spacer(Modifier.width(8.dp))
                Text("Luyện tập bài mới", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Câu ${index + 1}", color = colors.hintText, fontSize = 11.sp)
                val sentenceScore = state.feedback?.sentenceScores?.getOrNull(index)
                if (sentenceScore != null) {
                    Text(
                        text = "Điểm: $sentenceScore/10",
                        color = if (sentenceScore >= 8) colors.correct else if (sentenceScore >= 5) colors.warning else colors.error,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(sentence.vietnamese, color = colors.mainText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.CheckCircle, null, tint = colors.correct, modifier = Modifier.size(13.dp))
                Spacer(Modifier.width(4.dp))
                Text(sentence.english, color = colors.correct, fontSize = 12.sp, fontStyle = FontStyle.Italic)
            }
            if (!userAnswer.isNullOrBlank()) {
                Spacer(Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, null, tint = colors.mutedText, modifier = Modifier.size(11.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(userAnswer, color = colors.mutedText, fontSize = 12.sp)
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

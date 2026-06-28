package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LessonContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    val lesson = state.lesson ?: return
    val total = lesson.sentences.size
    val idx = state.currentSentenceIndex
    val sentence = lesson.sentences.getOrNull(idx) ?: return
    val userAnswer = state.userAnswers[idx] ?: ""
    val hasChecked = state.checkedAnswers.contains(idx)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Progress row (giống Sensei AI)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "CÂU HỎI ${idx + 1} / $total",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.accent
                )
                LinearProgressIndicator(
                    progress = { (idx + 1).toFloat() / total },
                    modifier = Modifier.width(120.dp).height(6.dp).clip(CircleShape),
                    color = colors.accent,
                    trackColor = colors.border
                )
            }

            Spacer(Modifier.height(12.dp))

            // Prompt card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "Dịch sang tiếng Anh:",
                        fontSize = 12.sp,
                        color = colors.mutedText
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        sentence.vietnamese,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mainText,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Input field (OutlinedTextField like Sensei AI)
            OutlinedTextField(
                value = userAnswer,
                onValueChange = { if (!hasChecked) viewModel.onAnswerChanged(idx, it) },
                placeholder = { Text("Nhập câu dịch tiếng Anh của bạn...", color = colors.hintText, fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.accent,
                    unfocusedBorderColor = colors.border,
                    cursorColor = colors.accent,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface
                ),
                singleLine = false,
                maxLines = 3,
                enabled = !hasChecked
            )

            // After checking — show answer and tokens
            if (hasChecked) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Đáp án chuẩn:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText
                )
                Spacer(Modifier.height(6.dp))

                // Token chips in FlowRow (no cards/borders, clean wraps like Sensei AI)
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val isJapanese = state.selectedLevelText.startsWith("N")
                    val isChinese = state.selectedLevelText.startsWith("HSK")
                    sentence.tokens.forEach { token ->
                        Column(
                            modifier = Modifier.padding(vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isJapanese) {
                                // Japanese layout: Furigana (Top), Kanji (Middle), Romaji (Bottom)
                                if (token.partOfSpeech.isNotEmpty()) {
                                    Text(
                                        text = token.partOfSpeech,
                                        color = colors.hintText,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                                Text(
                                    text = token.word,
                                    color = colors.mainText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (token.phonetic.isNotEmpty()) {
                                    Text(
                                        text = token.phonetic,
                                        color = colors.hintText,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Light
                                    )
                                }
                            } else if (isChinese) {
                                // Chinese layout: Pinyin (Top), Hanzi (Middle), Vietnamese Meaning/POS (Bottom)
                                if (token.phonetic.isNotEmpty()) {
                                    Text(
                                        text = token.phonetic,
                                        color = colors.accent,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                                Text(
                                    text = token.word,
                                    color = colors.mainText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (token.partOfSpeech.isNotEmpty()) {
                                    Text(
                                        text = token.partOfSpeech,
                                        color = colors.hintText,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            } else {
                                // English layout: Word (Top), IPA (Middle), Part of Speech (Bottom)
                                Text(
                                    text = token.word,
                                    color = colors.mainText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                if (token.phonetic.isNotEmpty()) {
                                    Text(
                                        text = token.phonetic,
                                        color = colors.accent,
                                        fontSize = 10.sp
                                    )
                                }
                                if (token.partOfSpeech.isNotEmpty()) {
                                    Text(
                                        text = token.partOfSpeech,
                                        color = colors.hintText,
                                        fontSize = 9.sp,
                                        fontStyle = FontStyle.Italic
                                    )
                                }
                            }
                        }
                    }
                }



                // Explanation card if available
                val explanation = state.explanations[idx]
                if (state.explainingSentenceIndex == idx) {
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = colors.accent, strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("AI đang giải thích...", fontSize = 13.sp, color = colors.mutedText)
                    }
                } else if (explanation != null) {
                    Spacer(Modifier.height(10.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
                        border = BorderStroke(0.5.dp, colors.border)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("💡 Giải thích chi tiết:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.accent)
                            Spacer(Modifier.height(4.dp))
                            MarkdownText(explanation, colors)
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        // Pinned bottom action
        Box(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
            if (!hasChecked) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Skip button
                    Button(
                        onClick = { viewModel.onCheckAnswer(idx) },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.surface),
                        border = BorderStroke(1.dp, colors.border),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Bỏ qua", color = colors.mutedText)
                    }
                    // Check button
                    Button(
                        onClick = { viewModel.onCheckAnswer(idx) },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Kiểm tra", color = Color.White)
                    }
                }
            } else {
                if (idx < total - 1) {
                    // Next sentence
                    Button(
                        onClick = { viewModel.onNextSentence() },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Câu tiếp theo", color = Color.White)
                        Icon(Icons.Default.ChevronRight, null)
                    }
                } else {
                    // Last sentence — submit directly since explanation is auto-triggered
                    Button(
                        onClick = { viewModel.onSubmitLesson() },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.correct),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        if (state.isEvaluating) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Xem đánh giá", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

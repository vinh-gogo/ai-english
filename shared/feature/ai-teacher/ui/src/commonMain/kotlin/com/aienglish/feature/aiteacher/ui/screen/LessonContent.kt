package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
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
import com.aienglish.feature.aiteacher.ui.viewmodel.LevelType

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
    val languageLabel = when (state.levelType) {
        LevelType.CEFR, LevelType.TOEIC -> "tiếng Anh"
        LevelType.JLPT -> "tiếng Nhật"
        LevelType.HSK -> "tiếng Trung"
        LevelType.DELE, LevelType.SIELE -> "tiếng Tây Ban Nha"
        LevelType.HINDI -> "tiếng Hindi"
        LevelType.DELF, LevelType.DALF -> "tiếng Pháp"
        LevelType.GOETHE, LevelType.TESTDAF -> "tiếng Đức"
        LevelType.TOPIK -> "tiếng Hàn"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(78.dp)) // 18dp top pad + 48dp header + 12dp bottom gap = 78dp
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
                        "Dịch sang $languageLabel:",
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
                placeholder = { Text("Nhập câu dịch $languageLabel của bạn...", color = colors.hintText, fontSize = 13.sp) },
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
                    "Các đáp án chuẩn:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "1) ${sentence.english}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.mainText
                )
                sentence.alternatives.forEachIndexed { altIndex, alt ->
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${altIndex + 2}) $alt",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.mainText
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    "Từ vựng chi tiết:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText
                )
                Spacer(Modifier.height(6.dp))

                // Token chips in FlowRow (no cards/borders, clean wraps like Sensei AI)
                // Filter out pure punctuation tokens (e.g. ",", ".", "!", "?") — they add no vocabulary value
                val isPunctuation: (String) -> Boolean = { w ->
                    w.all { c -> !c.isLetterOrDigit() } && w.isNotEmpty()
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val isJapanese = state.selectedLevelText.startsWith("N")
                    val isChinese = state.selectedLevelText.startsWith("HSK")
                    sentence.tokens
                        .filter { !isPunctuation(it.word) }
                        .forEach { token ->
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
                                if (token.phonetic.isNotEmpty()) {
                                    Text(
                                        text = token.phonetic,
                                        color = colors.hintText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Light
                                    )
                                }
                            } else if (isChinese) {
                                // Chinese layout: Pinyin (Top), Hanzi (Middle), Vietnamese Meaning/POS (Bottom)
                                if (token.phonetic.isNotEmpty()) {
                                    Text(
                                        text = token.phonetic,
                                        // IPA/phonetic as subtle supporting role, not competing with main word
                                        color = colors.mutedText,
                                        fontSize = 11.sp,
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
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            } else {
                                // English layout: IPA (Top), Word (Middle), Part of Speech (Bottom)
                                if (token.phonetic.isNotEmpty()) {
                                    Text(
                                        text = token.phonetic,
                                        // IPA is supporting info — use muted tone so main word stays dominant
                                        color = colors.mutedText,
                                        fontSize = 11.sp
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
                                        fontSize = 10.sp,
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
                    ExplanationSkeleton(colors)
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
            Spacer(Modifier.height(80.dp)) // Extra space so content can scroll past the floating footer
            Spacer(Modifier.navigationBarsPadding())
        }

        // Pinned bottom action
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 14.dp)
        ) {
            if (!hasChecked) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (idx > 0) {
                        IconButton(
                            onClick = { viewModel.onPreviousSentence() },
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
                        onClick = { viewModel.onCheckAnswer(idx) },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                        border = BorderStroke(2.dp, colors.border),
                        shape = CircleShape,
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Kiểm tra", color = Color.White)
                    }
                }
            } else {
                val isExplaining = state.explainingSentenceIndex == idx
                val sentenceScore = state.sentenceScores[idx]
                if (idx < total - 1) {
                    // Next sentence
                    Row(
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (idx > 0) {
                            IconButton(
                                onClick = { viewModel.onPreviousSentence() },
                                modifier = Modifier
                                    .size(48.dp)
                                    .border(2.dp, colors.border, CircleShape)
                                    .background(colors.surface, CircleShape)
                            ) {
                                Icon(Icons.Default.ChevronLeft, "Quay lại", tint = colors.mainText)
                            }
                        }
                        Button(
                            onClick = { viewModel.onNextSentence() },
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

                        if (!isExplaining && sentenceScore != null) {
                            Card(
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(
                                    containerColor = colors.surface
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    when {
                                        sentenceScore >= 9 -> colors.correct
                                        sentenceScore >= 5 -> colors.warning
                                        else -> colors.error
                                    }
                                ),
                                modifier = Modifier.height(48.dp).padding(horizontal = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxHeight().padding(horizontal = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$sentenceScore/10",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = when {
                                            sentenceScore >= 9 -> colors.correct
                                            sentenceScore >= 5 -> colors.warning
                                            else -> colors.error
                                        }
                                    )
                                }
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
                                onClick = { viewModel.onPreviousSentence() },
                                modifier = Modifier
                                    .size(48.dp)
                                    .border(2.dp, colors.border, CircleShape)
                                    .background(colors.surface, CircleShape)
                            ) {
                                Icon(Icons.Default.ChevronLeft, "Quay lại", tint = colors.mainText)
                            }
                        }
                        Button(
                            onClick = { viewModel.onSubmitLesson() },
                            enabled = !isExplaining && !state.isEvaluating,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colors.correct,
                                disabledContainerColor = colors.correct.copy(alpha = 0.5f)
                            ),
                            border = BorderStroke(2.dp, colors.border),
                            shape = CircleShape,
                            modifier = Modifier.weight(1f).fillMaxHeight()
                        ) {
                            if (state.isEvaluating) {
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

                        if (!isExplaining && !state.isEvaluating && sentenceScore != null) {
                            Card(
                                shape = CircleShape,
                                colors = CardDefaults.cardColors(
                                    containerColor = colors.surface
                                ),
                                border = BorderStroke(
                                    2.dp,
                                    when {
                                        sentenceScore >= 9 -> colors.correct
                                        sentenceScore >= 5 -> colors.warning
                                        else -> colors.error
                                    }
                                ),
                                modifier = Modifier.height(48.dp).padding(horizontal = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxHeight().padding(horizontal = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$sentenceScore/10",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = when {
                                            sentenceScore >= 9 -> colors.correct
                                            sentenceScore >= 5 -> colors.warning
                                            else -> colors.error
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExplanationSkeleton(colors: AppColors) {
    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        modifier = Modifier.alpha(alpha).fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
        border = BorderStroke(0.5.dp, colors.border)
    ) {
        Column(modifier = Modifier.padding(12.dp).fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.accent.copy(alpha = 0.3f))
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.border)
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.border)
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(12.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(colors.border)
            )
        }
    }
}

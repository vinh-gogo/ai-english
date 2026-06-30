package com.aienglish.feature.aiteacher.ui.screen

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import com.aienglish.core.common.tts.ExplanationParser
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.domain.model.EnglishSentence
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import com.aienglish.feature.aiteacher.ui.viewmodel.LevelType
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FeedbackContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    val feedback = state.feedback ?: return
    val lesson = state.lesson

    val levelSystemAndValue = remember(state.levelType, state) {
        val system = when (state.levelType) {
            LevelType.CEFR -> "CEFR"
            LevelType.TOEIC -> "TOEIC"
            LevelType.JLPT -> "JLPT"
            LevelType.HSK -> "HSK"
            LevelType.DELE -> "DELE"
            LevelType.SIELE -> "SIELE"
            LevelType.HINDI -> "Hindi"
            LevelType.DELF -> "DELF"
            LevelType.DALF -> "DALF"
            LevelType.GOETHE -> "Goethe"
            LevelType.TESTDAF -> "TestDaF"
            LevelType.TOPIK -> "TOPIK"
            LevelType.VIET -> "VIET"
        }
        val value = when (state.levelType) {
            LevelType.CEFR -> state.selectedCefrLevel
            LevelType.TOEIC -> state.selectedToeicLevel
            LevelType.JLPT -> state.selectedJlptLevel
            LevelType.HSK -> state.selectedHskLevel.removePrefix("HSK")
            LevelType.DELE -> state.selectedDeleLevel
            LevelType.SIELE -> state.selectedSieleLevel
            LevelType.HINDI -> state.selectedHindiLevel
            LevelType.DELF -> state.selectedDelfLevel
            LevelType.DALF -> state.selectedDalfLevel
            LevelType.GOETHE -> state.selectedGoetheLevel
            LevelType.TESTDAF -> state.selectedTestdafLevel
            LevelType.TOPIK -> state.selectedTopikLevel.removePrefix("TOPIK")
            LevelType.VIET -> when (state.selectedVietLevel) {
                "TIEU_HOC" -> "Tiểu học"
                "TRUNG_HOC" -> "Trung học"
                "DAI_HOC" -> "Đại học"
                else -> state.selectedVietLevel
            }
        }
        Pair(system, value)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(78.dp)) // 18dp top pad + 48dp header + 12dp bottom gap = 78dp

            Text(
                text = "Kết quả của bạn",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mainText
            )
            
            Spacer(Modifier.height(10.dp))

            // Subtitle Tags (Topic & Level Chips stretched to match full width of the card below)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Topic Chip
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(colors.accent.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .border(1.dp, colors.accent.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = colors.accent,
                            modifier = Modifier
                                .size(13.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = buildInlineAnnotatedString(state.selectedTopic, colors),
                            color = colors.accent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Level Chip (Vertical Layout)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(colors.mutedText.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .border(1.dp, colors.mutedText.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = colors.mutedText,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = levelSystemAndValue.first,
                            color = colors.mutedText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = levelSystemAndValue.second,
                            color = colors.mutedText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }


            Spacer(Modifier.height(24.dp))


            // Review card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
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

        // ── Gradient Overlay behind the button ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            colors.background.copy(alpha = 0.8f),
                            colors.background
                        )
                    )
                )
        )

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
        Column(modifier = Modifier.padding(16.dp)) {
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
                AnswerRow(
                    number = 1,
                    text = AnnotatedString(sentence.english),
                    colors = colors,
                    clipboardManager = clipboardManager,
                    plainText = sentence.english,
                    onPlayTts = { viewModel.speak(sentence.english, state.targetLanguage) }
                )
                
                if (sentence.alternatives.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAlternatives = !showAlternatives }
                            .padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (showAlternatives) "Thu gọn" else "+ Xem thêm ${sentence.alternatives.size} cách diễn đạt khác",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.accent
                        )
                    }

                    if (showAlternatives) {
                        sentence.alternatives.forEachIndexed { altIndex, alt ->
                            val annotatedAlt = remember(sentence.english, alt, colors) {
                                highlightDifferences(sentence.english, alt, colors)
                            }
                            AnswerRow(
                                number = altIndex + 2,
                                text = annotatedAlt,
                                colors = colors,
                                clipboardManager = clipboardManager,
                                plainText = alt,
                                onPlayTts = { viewModel.speak(alt, state.targetLanguage) }
                            )
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
                    val isTtsLoading by viewModel.isTtsLoading.collectAsState()
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "💡 Giải thích chi tiết:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.accent
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(getDisplayExplanation(explanation, state.targetLanguage)))
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Sao chep giai thich",
                                    tint = colors.accent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(Modifier.width(4.dp))
                            IconButton(
                                onClick = {
                                    viewModel.speakExplanation(explanation)
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                if (isTtsLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(14.dp),
                                        strokeWidth = 2.dp,
                                        color = colors.accent
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Nghe giai thich",
                                        tint = colors.accent,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
                        border = BorderStroke(0.5.dp, colors.border)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            MarkdownText(getDisplayExplanation(explanation, state.targetLanguage), colors)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnswerRow(
    number: Int,
    text: AnnotatedString,
    colors: AppColors,
    clipboardManager: androidx.compose.ui.platform.ClipboardManager,
    plainText: String,
    onPlayTts: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Rounded.CheckCircle,
                contentDescription = null,
                tint = colors.correct,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = remember(number, text) {
                    androidx.compose.ui.text.buildAnnotatedString {
                        append("$number) ")
                        append(text)
                    }
                },
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = colors.mainText
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = onPlayTts,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.VolumeUp,
                    contentDescription = "Nghe",
                    tint = colors.mutedText,
                    modifier = Modifier.size(18.dp)
                )
            }
            IconButton(
                onClick = { clipboardManager.setText(AnnotatedString(plainText)) },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = colors.mutedText,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun getDisplayExplanation(explanation: String, targetLanguage: String): String {
    if (explanation.trim().startsWith("[") && explanation.trim().endsWith("]")) {
        val list = mutableListOf<String>()
        try {
            val segments = ExplanationParser.parseSegments(explanation)
            for ((text, lang) in segments) {
                var textVal = text
                // Neu la ngon ngu muc tieu thi boc trong ngoac kep va in dam
                if (lang.lowercase() == targetLanguage.lowercase() && textVal.trim().isNotEmpty()) {
                    val leadingSpaces = textVal.takeWhile { it.isWhitespace() }
                    val trailingSpaces = textVal.takeLastWhile { it.isWhitespace() }
                    val body = textVal.trim()
                    val cleanBody = body.removeSurrounding("\"").removeSurrounding("'")
                    textVal = "$leadingSpaces**\"$cleanBody\"**$trailingSpaces"
                }
                list.add(textVal)
            }
            if (list.isNotEmpty()) {
                return list.joinToString("")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return explanation
}


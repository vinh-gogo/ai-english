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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import com.aienglish.core.common.tts.ExplanationParser
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import com.aienglish.feature.aiteacher.ui.viewmodel.LevelType
import com.aienglish.feature.aiteacher.ui.screen.components.AnswerRow
import com.aienglish.feature.aiteacher.ui.screen.components.LessonExplanationCard
import com.aienglish.feature.aiteacher.ui.screen.components.VocabularyTokens
import com.aienglish.feature.aiteacher.ui.screen.components.LessonActionButtons
import com.aienglish.feature.aiteacher.ui.screen.components.highlightDifferences

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
    
    var showDetailedTokens by remember { mutableStateOf(false) }
    val userAnswer = state.userAnswers[idx] ?: ""
    val hasChecked = state.checkedAnswers.contains(idx)

    LaunchedEffect(idx, hasChecked) {
        if (hasChecked) {
            viewModel.onExplainSentence(idx)
        }
    }

    val languageLabel = when (state.levelType) {
        LevelType.CEFR, LevelType.TOEIC -> "tiếng Anh"
        LevelType.JLPT -> "tiếng Nhật"
        LevelType.HSK -> "tiếng Trung"
        LevelType.DELE, LevelType.SIELE -> "tiếng Tây Ban Nha"
        LevelType.HINDI -> "tiếng Hindi"
        LevelType.DELF, LevelType.DALF -> "tiếng Pháp"
        LevelType.GOETHE, LevelType.TESTDAF -> "tiếng Đức"
        LevelType.TOPIK -> "tiếng Hàn"
        LevelType.VIET  -> "tiếng Việt"
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
                var showAlternatives by remember { mutableStateOf(false) }
                val clipboardManager = LocalClipboardManager.current

                Text(
                    "Các đáp án chuẩn:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText
                )
                Spacer(Modifier.height(6.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface),
                    border = BorderStroke(1.dp, colors.border)
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        AnswerRow(
                            number = 1,
                            text = AnnotatedString(sentence.english),
                            colors = colors,
                            clipboardManager = clipboardManager,
                            plainText = sentence.english,
                            onPlayTts = { viewModel.speak(sentence.english, state.targetLanguage) },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        if (sentence.alternatives.isNotEmpty()) {
                            HorizontalDivider(color = colors.border, thickness = 0.5.dp)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showAlternatives = !showAlternatives }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (showAlternatives) "Thu gọn" else "+ Xem thêm ${sentence.alternatives.size} cách diễn đạt khác",
                                    fontSize = 13.sp,
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
                                        onPlayTts = { viewModel.speak(alt, state.targetLanguage) },
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Từ vựng chi tiết:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mutedText
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { showDetailedTokens = !showDetailedTokens }
                            .padding(4.dp)
                    ) {
                        Text("Phân tích", fontSize = 12.sp, color = colors.mutedText)
                        Spacer(Modifier.width(4.dp))
                        Switch(
                            checked = showDetailedTokens,
                            onCheckedChange = { showDetailedTokens = it },
                            modifier = Modifier.scale(0.7f).height(24.dp)
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))

                // Token chips in FlowRow
                val isJapanese = state.selectedLevelText.startsWith("N")
                val isChinese = state.selectedLevelText.startsWith("HSK")
                VocabularyTokens(
                    tokens = sentence.tokens,
                    isDetailed = showDetailedTokens,
                    isJapanese = isJapanese,
                    isChinese = isChinese,
                    colors = colors
                )

                // Explanation card if available
                if (hasChecked) {
                    val isTtsLoading by viewModel.isTtsLoading.collectAsState()
                    val explanation = state.explanations[idx]
                    LessonExplanationCard(
                        explanation = explanation,
                        isExplaining = state.explainingSentenceIndex == idx,
                        isTtsLoading = isTtsLoading,
                        targetLanguage = state.targetLanguage,
                        colors = colors,
                        onPlayTts = { explanation?.let { viewModel.speakExplanation(it) } },
                        onSaveNote = { formula -> viewModel.saveQuestionNote(idx, formula) }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            Spacer(Modifier.height(80.dp)) // Extra space so content can scroll past the floating footer
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

        // Pinned bottom action
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(bottom = 14.dp)
        ) {
            LessonActionButtons(
                hasChecked = hasChecked,
                idx = idx,
                total = total,
                isExplaining = state.explainingSentenceIndex == idx,
                isEvaluating = state.isEvaluating,
                colors = colors,
                onCheckAnswer = { viewModel.onCheckAnswer(idx) },
                onPreviousSentence = { viewModel.onPreviousSentence() },
                onNextSentence = { viewModel.onNextSentence() },
                onSubmitLesson = { viewModel.onSubmitLesson() },
                onOpenNotes = { viewModel.openSavedNotes(idx) },
                noteCount = state.savedNoteCounts[idx] ?: 0
            )
        }
    }

    if (state.showSavedNotesBottomSheet) {
        NotesBottomSheet(
            notes = state.savedNotes,
            colors = colors,
            sentenceIndex = idx,
            onDismiss = { viewModel.closeSavedNotes() },
            onDeleteNote = { id, index -> viewModel.deleteQuestionNote(id, index) },
            onUpdateNote = { id, index, text -> viewModel.updateQuestionNote(id, index, text) }
        )
    }
}



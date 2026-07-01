package com.aienglish.feature.aiteacher.ui.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.domain.model.EnglishSentence
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import com.aienglish.feature.aiteacher.ui.screen.MarkdownText
import com.aienglish.feature.aiteacher.ui.screen.NotesBottomSheet

@Composable
fun SentenceReviewCard(
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
                                onClick = { viewModel.openSavedNotes(index) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Book,
                                    contentDescription = "Sổ tay",
                                    tint = colors.accent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(Modifier.width(4.dp))
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
                            MarkdownText(
                                markdown = getDisplayExplanation(explanation, state.targetLanguage),
                                colors = colors,
                                onFormulaClick = { formula ->
                                    viewModel.saveQuestionNote(index, formula)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

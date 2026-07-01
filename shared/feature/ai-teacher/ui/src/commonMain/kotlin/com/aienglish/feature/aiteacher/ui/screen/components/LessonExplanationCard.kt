package com.aienglish.feature.aiteacher.ui.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.ui.screen.MarkdownText

@Composable
fun LessonExplanationCard(
    explanation: String?,
    isExplaining: Boolean,
    isTtsLoading: Boolean,
    targetLanguage: String,
    colors: AppColors,
    onPlayTts: () -> Unit,
    onSaveNote: (String) -> Unit
) {
    if (isExplaining) {
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
                // Header: nhan + nut audio
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        val clipboardManager = LocalClipboardManager.current
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(getDisplayExplanation(explanation, targetLanguage)))
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
                            onClick = onPlayTts,
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
                Spacer(Modifier.height(4.dp))
                MarkdownText(
                    markdown = getDisplayExplanation(explanation, targetLanguage),
                    colors = colors,
                    onFormulaClick = onSaveNote
                )
            }
        }
    }
}

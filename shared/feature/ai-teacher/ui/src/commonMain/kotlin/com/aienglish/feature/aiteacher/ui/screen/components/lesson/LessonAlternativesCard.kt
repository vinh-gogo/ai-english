package com.aienglish.feature.aiteacher.ui.screen.components.lesson

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.ui.screen.components.AnswerRow
import com.aienglish.feature.aiteacher.ui.screen.components.highlightDifferences

@Composable
fun LessonAlternativesCard(
    english: String,
    alternatives: List<String>,
    targetLanguage: String,
    colors: AppColors,
    clipboardManager: ClipboardManager,
    onSpeak: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAlternatives by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            AnswerRow(
                number = 1,
                text = AnnotatedString(english),
                colors = colors,
                clipboardManager = clipboardManager,
                plainText = english,
                onPlayTts = { onSpeak(english, targetLanguage) },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (alternatives.isNotEmpty()) {
                HorizontalDivider(color = colors.border, thickness = 0.5.dp)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAlternatives = !showAlternatives }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (showAlternatives) "Thu gọn" else "+ Xem thêm ${alternatives.size} cách diễn đạt khác",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.accent
                    )
                }

                if (showAlternatives) {
                    alternatives.forEachIndexed { altIndex, alt ->
                        val annotatedAlt = remember(english, alt, colors) {
                            highlightDifferences(english, alt, colors)
                        }
                        AnswerRow(
                            number = altIndex + 2,
                            text = annotatedAlt,
                            colors = colors,
                            clipboardManager = clipboardManager,
                            plainText = alt,
                            onPlayTts = { onSpeak(alt, targetLanguage) },
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

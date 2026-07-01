package com.aienglish.feature.aiteacher.ui.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.domain.model.EnglishToken

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VocabularyTokens(
    tokens: List<EnglishToken>,
    isDetailed: Boolean,
    isJapanese: Boolean,
    isChinese: Boolean,
    colors: AppColors
) {
    // Filter out pure punctuation tokens (e.g. ",", ".", "!", "?")
    val isPunctuation: (String) -> Boolean = { w ->
        w.all { c -> !c.isLetterOrDigit() } && w.isNotEmpty()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface.copy(alpha = 0.95f)),
        border = BorderStroke(1.dp, colors.border)
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tokens
                .filter { !isPunctuation(it.word) }
                .forEach { token ->
                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (isJapanese) {
                            // Japanese layout: Furigana (Top), Kanji (Middle), Romaji (Bottom)
                            if (isDetailed) {
                                Text(
                                    text = token.partOfSpeech.ifEmpty { " " },
                                    color = if (token.partOfSpeech.isEmpty()) Color.Transparent else colors.hintText,
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
                            if (isDetailed) {
                                Text(
                                    text = token.phonetic.ifEmpty { " " },
                                    color = if (token.phonetic.isEmpty()) Color.Transparent else colors.hintText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        } else if (isChinese) {
                            // Chinese layout: Pinyin (Top), Hanzi (Middle), Vietnamese Meaning/POS (Bottom)
                            if (isDetailed) {
                                Text(
                                    text = token.phonetic.ifEmpty { " " },
                                    color = if (token.phonetic.isEmpty()) Color.Transparent else colors.mutedText,
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
                            if (isDetailed) {
                                Text(
                                    text = token.partOfSpeech.ifEmpty { " " },
                                    color = if (token.partOfSpeech.isEmpty()) Color.Transparent else colors.hintText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        } else {
                            // English layout: IPA (Top), Word (Middle), Part of Speech (Bottom)
                            if (isDetailed) {
                                Text(
                                    text = token.phonetic.ifEmpty { " " },
                                    color = if (token.phonetic.isEmpty()) Color.Transparent else colors.mutedText,
                                    fontSize = 11.sp
                                )
                            }
                            Text(
                                text = token.word,
                                color = colors.mainText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (isDetailed) {
                                Text(
                                    text = token.partOfSpeech.ifEmpty { " " },
                                    color = if (token.partOfSpeech.isEmpty()) Color.Transparent else colors.hintText,
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }
                    }
                }
        }
    }
}

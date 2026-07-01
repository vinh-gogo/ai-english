package com.aienglish.feature.aiteacher.ui.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun AnswerRow(
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

fun highlightDifferences(main: String, alt: String, colors: AppColors): AnnotatedString {
    val mainWords = main.split(" ")
    val altWords = alt.split(" ")
    return buildAnnotatedString {
        altWords.forEachIndexed { index, word ->
            val cleanWord = word.trim(',', '.', '?', '!', ';', ':')
            val isDifferent = mainWords.none { it.contains(cleanWord, ignoreCase = true) }
            if (isDifferent) {
                withStyle(SpanStyle(color = colors.accent, fontWeight = FontWeight.Bold)) {
                    append(word)
                }
            } else {
                withStyle(SpanStyle(color = colors.mainText)) {
                    append(word)
                }
            }
            if (index < altWords.size - 1) append(" ")
        }
    }
}

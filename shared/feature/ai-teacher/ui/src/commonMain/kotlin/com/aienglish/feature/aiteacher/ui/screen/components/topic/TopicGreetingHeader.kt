package com.aienglish.feature.aiteacher.ui.screen.components.topic

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun TopicGreetingHeader(
    targetLanguage: String,
    colors: AppColors
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Rounded.School,
            contentDescription = null,
            tint = colors.accent,
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.width(10.dp))
        val greetingText = when (targetLanguage) {
            "en" -> "Trợ lý Tiếng Anh"
            "ja" -> "Trợ lý Tiếng Nhật"
            "zh" -> "Trợ lý Tiếng Trung"
            "es" -> "Trợ lý Tiếng Tây Ban Nha"
            "hi" -> "Trợ lý Tiếng Hindi"
            "fr" -> "Trợ lý Tiếng Pháp"
            "de" -> "Trợ lý Tiếng Đức"
            "ko" -> "Trợ lý Tiếng Hàn"
            "vi" -> "Trợ lý Tiếng Việt"
            else -> "Trợ lý Tiếng Anh"
        }
        Text(
            greetingText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = colors.mainText
        )
    }
}

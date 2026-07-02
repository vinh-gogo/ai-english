package com.aienglish.feature.aiteacher.ui.screen.components.topic

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun TopicGreetingHeader(
    targetLanguage: String,
    colors: AppColors
) {
    Text(
        "AI Lingua",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = colors.mainText,
        modifier = Modifier.fillMaxWidth()
    )
}

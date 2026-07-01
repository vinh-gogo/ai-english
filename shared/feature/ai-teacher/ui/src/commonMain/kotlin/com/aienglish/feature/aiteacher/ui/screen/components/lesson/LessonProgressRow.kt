package com.aienglish.feature.aiteacher.ui.screen.components.lesson

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun LessonProgressRow(
    currentIndex: Int,
    total: Int,
    colors: AppColors,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "CÂU HỎI ${currentIndex + 1} / $total",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = colors.accent
        )
        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / total },
            modifier = Modifier.width(120.dp).height(6.dp).clip(CircleShape),
            color = colors.accent,
            trackColor = colors.border
        )
    }
}

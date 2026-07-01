package com.aienglish.feature.aiteacher.ui.screen.components.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.ui.screen.MarkdownText

@Composable
fun FeedbackReviewCard(
    review: String,
    colors: AppColors,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Nhận xét từ AI Lingua", 
                color = colors.accent, 
                fontSize = 13.sp, 
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            MarkdownText(review, colors)
        }
    }
}

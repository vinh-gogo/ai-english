package com.aienglish.feature.aiteacher.ui.screen.components.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.ui.screen.buildInlineAnnotatedString

@Composable
fun FeedbackHeaderChips(
    selectedTopic: String,
    levelSystem: String,
    levelValue: String,
    colors: AppColors
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Topic Chip
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(colors.accent.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                .border(1.dp, colors.accent.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier
                        .size(13.dp)
                        .padding(top = 2.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = buildInlineAnnotatedString(selectedTopic, colors),
                    color = colors.accent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Level Chip
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(colors.mutedText.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                .border(1.dp, colors.mutedText.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = colors.mutedText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = levelSystem,
                    color = colors.mutedText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = levelValue,
                    color = colors.mutedText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

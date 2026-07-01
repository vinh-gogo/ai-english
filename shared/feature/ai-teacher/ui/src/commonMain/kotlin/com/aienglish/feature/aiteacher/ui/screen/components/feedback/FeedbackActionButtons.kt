package com.aienglish.feature.aiteacher.ui.screen.components.feedback

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun FeedbackActionButtons(
    onRetryClick: () -> Unit,
    onResetClick: () -> Unit,
    colors: AppColors
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onRetryClick,
            modifier = Modifier.weight(1f).fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.surface,
                contentColor = colors.mainText
            ),
            border = BorderStroke(1.dp, colors.border),
            shape = CircleShape
        ) {
            Icon(Icons.Rounded.Refresh, contentDescription = "Làm lại")
            Spacer(Modifier.width(8.dp))
            Text("Làm lại", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }

        Button(
            onClick = onResetClick,
            modifier = Modifier.weight(1f).fillMaxHeight(),
            colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
            border = BorderStroke(1.dp, colors.border),
            shape = CircleShape
        ) {
            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = "Bài mới")
            Spacer(Modifier.width(8.dp))
            Text("Bài mới", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

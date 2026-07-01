package com.aienglish.feature.aiteacher.ui.screen.components.topic

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun SaveTopicDialog(
    showDialog: Boolean,
    customTopicInput: String,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    colors: AppColors
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Lưu chủ đề",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText
                )
            },
            text = {
                Text(
                    text = "Bạn có muốn lưu chủ đề \"$customTopicInput\" vào danh sách để sử dụng lại sau này không?",
                    fontSize = 14.sp,
                    color = colors.mainText
                )
            },
            confirmButton = {
                TextButton(onClick = onSave) {
                    Text("Lưu", color = colors.accent, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Hủy", color = colors.mutedText)
                }
            },
            containerColor = colors.surface,
            textContentColor = colors.mainText,
            titleContentColor = colors.mainText
        )
    }
}

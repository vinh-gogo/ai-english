package com.aienglish.feature.aiteacher.ui.screen.components.lesson

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun LessonInputField(
    value: String,
    onValueChange: (String) -> Unit,
    languageLabel: String,
    hasChecked: Boolean,
    colors: AppColors,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (!hasChecked) onValueChange(it) },
        placeholder = { Text("Nhập câu dịch $languageLabel của bạn...", color = colors.hintText, fontSize = 13.sp) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.accent,
            unfocusedBorderColor = colors.border,
            cursorColor = colors.accent,
            focusedContainerColor = colors.surface,
            unfocusedContainerColor = colors.surface,
            focusedTextColor = colors.mainText,
            unfocusedTextColor = colors.mainText
        ),
        singleLine = false,
        maxLines = 3,
        enabled = !hasChecked
    )
}

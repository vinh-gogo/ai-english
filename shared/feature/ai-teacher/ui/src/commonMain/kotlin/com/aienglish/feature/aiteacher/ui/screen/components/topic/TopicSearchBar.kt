package com.aienglish.feature.aiteacher.ui.screen.components.topic

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@Composable
fun TopicSearchBar(
    query: String,
    isFocused: Boolean,
    onQueryChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    colors: AppColors
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        textStyle = androidx.compose.ui.text.TextStyle(
            color = colors.mainText,
            fontSize = 13.sp
        ),
        singleLine = true,
        cursorBrush = SolidColor(colors.accent),
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp)
            .onFocusChanged { onFocusChanged(it.isFocused) }
            .background(colors.surface, RoundedCornerShape(8.dp))
            .border(
                1.dp,
                if (isFocused) colors.accent else colors.border,
                RoundedCornerShape(8.dp)
            ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = colors.hintText
                )
                Spacer(Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Tìm kiếm chủ đề...",
                            color = colors.hintText,
                            fontSize = 13.sp
                        )
                    }
                    innerTextField()
                }
                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { onQueryChange("") },
                        tint = colors.hintText
                    )
                }
            }
        }
    )
}

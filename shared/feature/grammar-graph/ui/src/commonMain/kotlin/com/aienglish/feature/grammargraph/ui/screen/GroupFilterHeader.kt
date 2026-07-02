package com.aienglish.feature.grammargraph.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun GroupFilterHeader(
    groups: List<String>,
    selectedGroup: String,
    editingGroup: String?,
    colors: AppColors,
    onSelectGroup: (String) -> Unit,
    onAddGroup: () -> Unit,
    onStartEditingGroup: (String) -> Unit,
    onRenameGroup: (String, String) -> Unit,
    onConfigClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Custom Drag Handle at the top of the header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 4.dp)
                    .background(colors.border, CircleShape)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp)
                .padding(horizontal = 20.dp)
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Hub,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Đồ thị Ngữ pháp",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mainText,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = onConfigClick,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Configuration",
                    tint = colors.mutedText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Carousel to categorize grammar groups
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val carouselItems = remember(groups) { groups + "+" }
            
            carouselItems.forEach { groupName ->
                val isSelected = selectedGroup == groupName
                val isPlus = groupName == "+"
                val isEditing = editingGroup == groupName
                
                Surface(
                    modifier = if (isPlus) {
                        Modifier.clickable { onAddGroup() }
                    } else if (isEditing) {
                        Modifier
                    } else {
                        Modifier.combinedClickable(
                            onClick = { onSelectGroup(groupName) },
                            onLongClick = { onStartEditingGroup(groupName) }
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) colors.accent.copy(alpha = 0.12f) else colors.background,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) colors.accent else colors.border.copy(alpha = 0.5f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .height(28.dp)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when {
                                isPlus -> Icons.Rounded.Add
                                groupName == "Cơ bản" -> Icons.Rounded.AutoAwesome
                                else -> Icons.Rounded.Hub
                            },
                            contentDescription = null,
                            tint = if (isSelected) colors.accent else colors.mutedText,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        
                        if (isEditing) {
                            var editName by remember { mutableStateOf(groupName) }
                            val focusRequester = remember { FocusRequester() }
                            var hasBeenFocused by remember { mutableStateOf(false) }
                            
                            LaunchedEffect(Unit) {
                                focusRequester.requestFocus()
                            }
                            
                            BasicTextField(
                                value = editName,
                                onValueChange = { editName = it },
                                textStyle = androidx.compose.ui.text.TextStyle(
                                    color = colors.accent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                cursorBrush = SolidColor(colors.accent),
                                modifier = Modifier
                                    .width(IntrinsicSize.Min)
                                    .widthIn(min = 60.dp, max = 150.dp)
                                    .focusRequester(focusRequester)
                                    .onFocusChanged { 
                                        if (it.isFocused) {
                                            hasBeenFocused = true
                                        } else if (hasBeenFocused) {
                                            onRenameGroup(groupName, editName)
                                        }
                                    },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        onRenameGroup(groupName, editName)
                                    }
                                )
                            )
                        } else {
                            Text(
                                text = groupName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) colors.accent else colors.mainText
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(color = colors.border.copy(alpha = 0.35f), thickness = 1.dp)
    }
}

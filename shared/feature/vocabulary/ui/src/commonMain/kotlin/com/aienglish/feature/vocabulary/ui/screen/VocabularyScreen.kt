package com.aienglish.feature.vocabulary.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.vocabulary.domain.model.VocabularyItem
import com.aienglish.feature.vocabulary.ui.viewmodel.VocabularyViewModel
import org.koin.compose.koinInject

@Composable
fun VocabularyScreen(
    onBack: () -> Unit,
    colors: AppColors,
    viewModel: VocabularyViewModel = koinInject()
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // ── 1. Top Navigation Bar ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 18.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button (Matching app's 48dp circular style)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(colors.surface)
                        .border(2.dp, colors.border, CircleShape)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = colors.mainText,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Title + Count
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Sổ từ vựng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mainText
                    )
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = colors.accent.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${state.items.size} từ",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.accent,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                // Add Word Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(colors.accent)
                        .clickable { viewModel.onOpenAddDialog() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Thêm từ vựng",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // ── 2. Search Bar ──
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(14.dp),
                color = colors.surface,
                border = BorderStroke(1.dp, colors.border.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = null,
                        tint = colors.mutedText,
                        modifier = Modifier.size(18.dp)
                    )
                    TextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = {
                            Text(
                                "Tìm kiếm từ vựng, nghĩa...",
                                fontSize = 13.sp,
                                color = colors.hintText
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = colors.accent,
                            focusedTextColor = colors.mainText,
                            unfocusedTextColor = colors.mainText
                        ),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    if (state.searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.onSearchQueryChanged("") },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Xóa tìm kiếm",
                                tint = colors.mutedText,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── 3. List of Vocabulary Items ──
            if (state.filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                            contentDescription = null,
                            tint = colors.mutedText.copy(alpha = 0.5f),
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = if (state.searchQuery.isEmpty()) "Chưa có từ vựng nào trong sổ" else "Không tìm thấy từ phù hợp",
                            fontSize = 14.sp,
                            color = colors.mutedText
                        )
                        if (state.searchQuery.isEmpty()) {
                            OutlinedButton(
                                onClick = { viewModel.onOpenAddDialog() },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.accent),
                                border = BorderStroke(1.dp, colors.accent)
                            ) {
                                Text("Thêm từ đầu tiên")
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(state.filteredItems, key = { it.id }) { item ->
                        VocabularyCard(
                            item = item,
                            colors = colors,
                            onDelete = { viewModel.onDeleteWord(item.id) }
                        )
                    }
                }
            }
        }

        // ── 4. Dialog thêm từ mới ──
        if (state.showAddDialog) {
            AddVocabularyDialog(
                colors = colors,
                onDismiss = { viewModel.onDismissAddDialog() },
                onAdd = { word, phonetic, meaning, example ->
                    viewModel.onAddWord(word, phonetic, meaning, example)
                }
            )
        }
    }
}

@Composable
private fun VocabularyCard(
    item: VocabularyItem,
    colors: AppColors,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.surface.copy(alpha = 0.85f),
        border = BorderStroke(1.dp, colors.border.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = item.word,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mainText
                    )
                    if (item.phonetic.isNotBlank()) {
                        Text(
                            text = item.phonetic,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            color = colors.accent
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DeleteOutline,
                        contentDescription = "Xóa từ",
                        tint = colors.mutedText.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = item.meaning,
                fontSize = 13.sp,
                color = colors.mainText,
                lineHeight = 18.sp
            )

            if (item.example.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colors.surfaceContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\"${item.example}\"",
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = colors.mutedText,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AddVocabularyDialog(
    colors: AppColors,
    onDismiss: () -> Unit,
    onAdd: (word: String, phonetic: String, meaning: String, example: String) -> Unit
) {
    var word by remember { mutableStateOf("") }
    var phonetic by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var example by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        title = {
            Text(
                text = "Thêm từ vựng mới",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = colors.mainText
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = word,
                    onValueChange = { word = it },
                    label = { Text("Từ vựng *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phonetic,
                    onValueChange = { phonetic = it },
                    label = { Text("Phiên âm (tùy chọn)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = meaning,
                    onValueChange = { meaning = it },
                    label = { Text("Nghĩa tiếng Việt *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = example,
                    onValueChange = { example = it },
                    label = { Text("Câu ví dụ (tùy chọn)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onAdd(word, phonetic, meaning, example) },
                enabled = word.isNotBlank() && meaning.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = colors.accent)
            ) {
                Text("Lưu vào sổ", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = colors.mutedText)
            }
        }
    )
}

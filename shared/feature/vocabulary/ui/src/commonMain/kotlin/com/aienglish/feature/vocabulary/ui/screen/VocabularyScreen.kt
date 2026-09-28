package com.aienglish.feature.vocabulary.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteOutline
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
import com.aienglish.feature.vocabulary.domain.model.VocabularyCategory
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
                            text = if (state.selectedCategory != null) "${state.filteredItems.size}/${state.items.size} từ" else "${state.items.size} từ",
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

            // ── 2. Category Filter Chips (Lĩnh vực) ──
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // "Tất cả" chip
                item {
                    CategoryFilterChip(
                        label = "Tất cả",
                        iconEmoji = "📚",
                        isSelected = state.selectedCategory == null,
                        onClick = { viewModel.onCategorySelected(null) },
                        colors = colors
                    )
                }

                items(VocabularyCategory.entries) { category ->
                    CategoryFilterChip(
                        label = category.displayName,
                        iconEmoji = category.iconEmoji,
                        isSelected = state.selectedCategory == category,
                        onClick = { viewModel.onCategorySelected(category) },
                        colors = colors
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            // ── 4. List of Vocabulary Items ──
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
                            text = if (state.selectedCategory == null)
                                "Chưa có từ vựng nào trong sổ"
                            else
                                "Chưa có từ vựng nào thuộc lĩnh vực này",
                            fontSize = 14.sp,
                            color = colors.mutedText
                        )
                        if (state.selectedCategory == null) {
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

        // ── 5. Dialog thêm từ mới ──
        if (state.showAddDialog) {
            AddVocabularyDialog(
                initialCategory = state.selectedCategory ?: VocabularyCategory.GENERAL,
                colors = colors,
                onDismiss = { viewModel.onDismissAddDialog() },
                onAdd = { word, phonetic, meaning, example, category ->
                    viewModel.onAddWord(word, phonetic, meaning, example, category)
                }
            )
        }
    }
}

@Composable
private fun CategoryFilterChip(
    label: String,
    iconEmoji: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    colors: AppColors
) {
    val bg = if (isSelected) colors.accent else colors.surface
    val border = if (isSelected) colors.accent else colors.border.copy(alpha = 0.5f)
    val textCol = if (isSelected) Color.White else colors.mutedText

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bg,
        border = BorderStroke(1.dp, border),
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = iconEmoji, fontSize = 12.sp)
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textCol
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Word + Category Tag + Delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f, fill = false)
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

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Category Badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = colors.accent.copy(alpha = 0.12f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = item.category.iconEmoji, fontSize = 10.sp)
                            Text(
                                text = item.category.displayName,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
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
    initialCategory: VocabularyCategory,
    colors: AppColors,
    onDismiss: () -> Unit,
    onAdd: (word: String, phonetic: String, meaning: String, example: String, category: VocabularyCategory) -> Unit
) {
    var word by remember { mutableStateOf("") }
    var phonetic by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var example by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(initialCategory) }

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
                // Category Picker
                Text(
                    text = "Lĩnh vực phân loại:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.mutedText
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    VocabularyCategory.entries.forEach { cat ->
                        val isCatSelected = selectedCategory == cat
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isCatSelected) colors.accent else colors.surfaceContainer,
                            border = BorderStroke(1.dp, if (isCatSelected) colors.accent else colors.border.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable { selectedCategory = cat }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(cat.iconEmoji, fontSize = 11.sp)
                                Text(
                                    cat.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCatSelected) Color.White else colors.mainText
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))

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
                onClick = { onAdd(word, phonetic, meaning, example, selectedCategory) },
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

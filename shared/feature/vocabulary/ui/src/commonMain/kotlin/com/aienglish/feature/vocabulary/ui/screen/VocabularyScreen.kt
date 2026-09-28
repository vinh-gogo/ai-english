package com.aienglish.feature.vocabulary.ui.screen

import androidx.compose.animation.*
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.vocabulary.domain.model.PartOfSpeech
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
                    .padding(top = 16.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button (Matching app's circular style)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(colors.surface)
                        .border(1.5.dp, colors.border, CircleShape)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = colors.mainText,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Title + Count (Responsive centered column/row)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Sổ từ vựng",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mainText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = colors.accent.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = if (state.selectedCategory != null) "${state.filteredItems.size}/${state.items.size}" else "${state.items.size}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.accent,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }

                // Add Word Button
                Box(
                    modifier = Modifier
                        .size(44.dp)
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

                // Dynamic Categories
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

            // ── 3. Danh sách từ vựng ──
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.accent)
                }
            } else if (state.filteredItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().weight(1f),
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
                            onPlayTts = { text -> viewModel.playTts(text) },
                            onDelete = { viewModel.onDeleteWord(item.id) }
                        )
                    }
                }
            }
        }

        // ── 4. Dialog thêm từ mới ──
        if (state.showAddDialog) {
            AddVocabularyDialog(
                initialCategory = state.selectedCategory ?: VocabularyCategory.GENERAL,
                colors = colors,
                onDismiss = { viewModel.onDismissAddDialog() },
                onAdd = { word, partOfSpeech, phonetic, meaning, collocations, example, category ->
                    viewModel.onAddWord(word, partOfSpeech, phonetic, meaning, collocations, example, category)
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
private fun PartOfSpeechBadge(
    partOfSpeechCode: String,
    colors: AppColors
) {
    val pos = PartOfSpeech.fromCode(partOfSpeechCode)
    val (badgeBg, badgeText) = when (pos) {
        PartOfSpeech.NOUN -> Color(0xFF2563EB).copy(alpha = 0.12f) to Color(0xFF2563EB)
        PartOfSpeech.VERB -> Color(0xFF16A34A).copy(alpha = 0.12f) to Color(0xFF16A34A)
        PartOfSpeech.ADJECTIVE -> Color(0xFFD97706).copy(alpha = 0.12f) to Color(0xFFD97706)
        PartOfSpeech.ADVERB -> Color(0xFF9333EA).copy(alpha = 0.12f) to Color(0xFF9333EA)
        PartOfSpeech.PHRASE -> Color(0xFF0D9488).copy(alpha = 0.12f) to Color(0xFF0D9488)
        PartOfSpeech.PREPOSITION -> Color(0xFFEA580C).copy(alpha = 0.12f) to Color(0xFFEA580C)
        PartOfSpeech.OTHER -> colors.mutedText.copy(alpha = 0.12f) to colors.mutedText
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = badgeBg
    ) {
        Text(
            text = "${pos.shortName} ${pos.displayName}",
            fontSize = 11.5.sp,
            fontWeight = FontWeight.Bold,
            color = badgeText,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VocabularyCard(
    item: VocabularyItem,
    colors: AppColors,
    onPlayTts: (String) -> Unit,
    onDelete: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        shape = RoundedCornerShape(16.dp),
        color = colors.surface.copy(alpha = 0.90f),
        border = BorderStroke(1.dp, if (isExpanded) colors.accent.copy(alpha = 0.45f) else colors.border.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: Top Meta Bar (Category badge on Left, Action buttons on Right: Expand Chevron + Delete)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Badge (e.g. 💻 Công nghệ thông tin)
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = colors.accent.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = item.category.iconEmoji, fontSize = 11.sp)
                        Text(
                            text = item.category.displayName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.accent
                        )
                    }
                }

                // Right Actions: Nút mở rộng/thu gọn + Nút xóa từ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Nút mở rộng / thu gọn
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(if (isExpanded) colors.accent.copy(alpha = 0.15f) else colors.surfaceContainer)
                            .clickable { isExpanded = !isExpanded },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Thu gọn" else "Mở rộng",
                            tint = if (isExpanded) colors.accent else colors.mutedText,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Nút xóa từ
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .clickable { onDelete() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DeleteOutline,
                            contentDescription = "Xóa từ",
                            tint = colors.mutedText.copy(alpha = 0.65f),
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

            // Row 2: Khối Từ vựng & IPA (gom chung để khoảng cách giữa từ và IPA cực kỳ khít và tự nhiên)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(1.5.dp)
            ) {
                // Từ chính căn lề trái, Loại từ căn lề phải
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Từ chính căn lề trái cùng loa phát âm
                    Row(
                        modifier = Modifier.weight(1f, fill = false),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = item.word,
                            fontSize = 17.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.mainText
                        )

                        // Speaker Button for word pronunciation
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(colors.accent.copy(alpha = 0.12f))
                                .clickable { onPlayTts(item.word) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Phát âm từ",
                                tint = colors.accent,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    // Label loại từ căn lề phải
                    PartOfSpeechBadge(item.partOfSpeech, colors)
                }

                // IPA phiên âm xếp ngay bên dưới từ chính (khoảng cách 1.5dp siêu khít)
                if (item.phonetic.isNotBlank()) {
                    Text(
                        text = item.phonetic,
                        fontSize = 12.5.sp,
                        fontStyle = FontStyle.Italic,
                        color = colors.accent,
                        modifier = Modifier.padding(start = 1.dp)
                    )
                }
            }

            // Row 4: Nghĩa tiếng Việt
            Text(
                text = item.meaning,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = colors.mainText,
                lineHeight = 20.sp
            )

            // Row 5: Nút nhấn mở rộng / thu gọn đầy đủ thông tin ở chân card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = if (isExpanded) "Thu gọn chi tiết" else "Mở rộng chi tiết (collocations, ví dụ)",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.accent
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(16.dp)
                )
            }

            // Row 6: Phần mở rộng đầy đủ thông tin (AnimatedVisibility)
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HorizontalDivider(color = colors.border.copy(alpha = 0.35f))

                    // Collocations Section
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Link,
                                contentDescription = null,
                                tint = colors.accent,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Collocations:",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.mutedText
                            )
                        }

                        if (item.collocations.isNotEmpty()) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                item.collocations.forEach { col ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = colors.surfaceContainer.copy(alpha = 0.65f),
                                        border = BorderStroke(1.dp, colors.border.copy(alpha = 0.35f)),
                                        modifier = Modifier.clickable { onPlayTts(col) }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = col,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = colors.mainText
                                            )
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                                contentDescription = "Nghe collocation",
                                                tint = colors.accent.copy(alpha = 0.8f),
                                                modifier = Modifier.size(11.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            Text(
                                text = "Chưa có collocation cho từ này",
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                color = colors.mutedText.copy(alpha = 0.7f),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }

                    // Example Sentence Section
                    if (item.example.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = colors.surfaceContainer.copy(alpha = 0.45f),
                            border = BorderStroke(1.dp, colors.border.copy(alpha = 0.25f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "\"${item.example}\"",
                                    fontSize = 12.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = colors.mutedText,
                                    lineHeight = 17.sp,
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(Modifier.width(8.dp))

                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(colors.accent.copy(alpha = 0.08f))
                                        .clickable { onPlayTts(item.example) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Đọc câu ví dụ",
                                        tint = colors.accent,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AddVocabularyDialog(
    initialCategory: VocabularyCategory,
    colors: AppColors,
    onDismiss: () -> Unit,
    onAdd: (
        word: String,
        partOfSpeech: String,
        phonetic: String,
        meaning: String,
        collocations: List<String>,
        example: String,
        category: VocabularyCategory
    ) -> Unit
) {
    var word by remember { mutableStateOf("") }
    var selectedPartOfSpeech by remember { mutableStateOf(PartOfSpeech.NOUN) }
    var phonetic by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var collocationsInput by remember { mutableStateOf("") }
    var example by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(initialCategory) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        title = {
            Text(
                text = "Thêm từ vựng mới",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = colors.mainText
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Category Picker
                Text(
                    text = "Lĩnh vực phân loại:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
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
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(cat.iconEmoji, fontSize = 11.sp)
                                Text(
                                    cat.displayName,
                                    fontSize = 11.5.sp,
                                    fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCatSelected) Color.White else colors.mainText
                                )
                            }
                        }
                    }
                }

                // Part of Speech Picker (FlowRow: all options directly visible on small screens)
                Text(
                    text = "Loại từ:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.mutedText
                )
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PartOfSpeech.entries.filter { it != PartOfSpeech.OTHER }.forEach { pos ->
                        val isPosSelected = selectedPartOfSpeech == pos
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isPosSelected) colors.accent else colors.surfaceContainer,
                            border = BorderStroke(1.dp, if (isPosSelected) colors.accent else colors.border.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable { selectedPartOfSpeech = pos }
                        ) {
                            Text(
                                text = "${pos.shortName} ${pos.displayName}",
                                fontSize = 11.sp,
                                fontWeight = if (isPosSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isPosSelected) Color.White else colors.mainText,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(2.dp))

                OutlinedTextField(
                    value = word,
                    onValueChange = { word = it },
                    label = { Text("Từ vựng *") },
                    placeholder = { Text("Ví dụ: Integration") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phonetic,
                    onValueChange = { phonetic = it },
                    label = { Text("Phiên âm IPA (tùy chọn)") },
                    placeholder = { Text("Ví dụ: /ˌɪntɪˈɡreɪʃn/") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = meaning,
                    onValueChange = { meaning = it },
                    label = { Text("Nghĩa tiếng Việt *") },
                    placeholder = { Text("Ví dụ: Sự tích hợp, hợp nhất") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = collocationsInput,
                    onValueChange = { collocationsInput = it },
                    label = { Text("Collocations (tùy chọn, cách bởi dấu phẩy)") },
                    placeholder = { Text("Ví dụ: CI pipeline, seamless integration") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = example,
                    onValueChange = { example = it },
                    label = { Text("Câu ví dụ (tùy chọn)") },
                    placeholder = { Text("Ví dụ: Our team sets up automated testing.") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val collList = collocationsInput.split(",", "•").map { it.trim() }.filter { it.isNotBlank() }
                    onAdd(
                        word,
                        selectedPartOfSpeech.code,
                        phonetic,
                        meaning,
                        collList,
                        example,
                        selectedCategory
                    )
                },
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

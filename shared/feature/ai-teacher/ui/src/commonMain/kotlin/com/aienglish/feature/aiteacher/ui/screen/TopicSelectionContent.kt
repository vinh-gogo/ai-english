package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.List
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.domain.model.TopicItem
import com.aienglish.feature.aiteacher.domain.model.getCompatibleLevels
import com.aienglish.feature.aiteacher.ui.viewmodel.LevelType
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel

fun getLevelColor(levelCode: String, colors: AppColors): Color {
    return when (levelCode) {
        "A1", "300", "N5", "HSK1" -> Color(0xFF0284C7)      // Xanh dương Sky Blue
        "A2", "N4", "HSK2" -> Color(0xFF10B981)             // Xanh lá Emerald
        "B1", "500", "N3", "HSK3" -> Color(0xFFF59E0B)      // Vàng hổ phách Amber
        "B2", "700", "N2", "HSK4", "TDN3" -> Color(0xFFEF4444) // Đỏ Coral
        "C1", "900", "N1", "HSK5", "TDN4" -> colors.accent  // Màu accent chính
        "C2", "HSK6", "TDN5" -> Color(0xFF8B5CF6)           // Tím Violet
        else -> colors.accent
    }
}

@Composable
fun TopicSelectionContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()
    val selectedIdx = state.selectedTopicIndex
    var isSearchFocused by remember { mutableStateOf(false) }
    var isCustomTopicFocused by remember { mutableStateOf(false) }

    // Scroll to bottom when keyboard resizes the screen or either text field is focused
    LaunchedEffect(isSearchFocused, isCustomTopicFocused, scrollState.maxValue) {
        if (isSearchFocused || isCustomTopicFocused) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    // Filter topics: show topics containing search query (pre-filtered by compatible levels in ViewModel)
    val filteredTopics = remember(state.topics, state.topicSearchQuery) {
        state.topics.mapIndexed { i, t -> i to t }
            .filter { pair ->
                pair.second.name.contains(state.topicSearchQuery, ignoreCase = true)
            }
    }

    // Determine current selected topic details
    val currentTopic = remember(state.topics, selectedIdx) { state.topics.getOrNull(selectedIdx) }

    // Auto-scroll when selection changes
    LaunchedEffect(selectedIdx) {
        val fi = filteredTopics.indexOfFirst { it.first == selectedIdx }
        if (fi != -1) listState.animateScrollToItem(fi)
    }

    if (state.isGeneratingLesson) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = colors.accent)
                Spacer(Modifier.height(16.dp))
                Text("AI đang soạn bài luyện tập...", color = colors.mutedText, fontSize = 14.sp)
            }
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(78.dp)) // 48dp header + 12dp bottom gap = 60dp

            // Header icon + title side-by-side
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.School,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(10.dp))
                val greetingText = when (state.targetLanguage) {
                    "en" -> "Trợ lý Tiếng Anh"
                    "ja" -> "Trợ lý Tiếng Nhật"
                    "zh" -> "Trợ lý Tiếng Trung"
                    "es" -> "Trợ lý Tiếng Tây Ban Nha"
                    "hi" -> "Trợ lý Tiếng Hindi"
                    "fr" -> "Trợ lý Tiếng Pháp"
                    "de" -> "Trợ lý Tiếng Đức"
                    "ko" -> "Trợ lý Tiếng Hàn"
                    else -> "Trợ lý Tiếng Anh"
                }
                Text(
                    greetingText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Target Level System (CEFR / TOEIC / JLPT Selector) ──
            TargetLevelSystem(state, viewModel, colors)

            Spacer(Modifier.height(16.dp))

            // ── Carousel Card (styled with uniform accent color) ──
            TopicCarouselCard(state, viewModel, colors, selectedIdx, filteredTopics, currentTopic)

            Spacer(Modifier.height(16.dp))

            // ── Search Bar (Custom compact styling) ──
            BasicTextField(
                value = state.topicSearchQuery,
                onValueChange = { viewModel.onTopicSearchChange(it) },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = colors.mainText,
                    fontSize = 13.sp
                ),
                singleLine = true,
                cursorBrush = androidx.compose.ui.graphics.SolidColor(colors.accent),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .onFocusChanged { isSearchFocused = it.isFocused }
                    .background(colors.surface, RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        if (isSearchFocused) colors.accent else colors.border,
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
                            if (state.topicSearchQuery.isEmpty()) {
                                Text(
                                    text = "Tìm kiếm chủ đề...",
                                    color = colors.hintText,
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                        if (state.topicSearchQuery.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable { viewModel.onTopicSearchChange("") },
                                tint = colors.hintText
                            )
                        }
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            // Chips label + Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (state.isCustomTopicMode) "Nhập chủ đề tùy chỉnh"
                    else if (state.topicSearchQuery.isEmpty()) "Danh sách chủ đề"
                    else "Kết quả tìm kiếm (${filteredTopics.size})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText
                )
                
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { viewModel.setCustomTopicMode(!state.isCustomTopicMode) }
                        .padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (state.isCustomTopicMode) Icons.Rounded.List else Icons.Rounded.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = colors.accent
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        if (state.isCustomTopicMode) "Chọn từ danh sách" else "Tự nhập chủ đề",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.accent
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            if (state.isCustomTopicMode) {
                OutlinedTextField(
                    value = state.customTopicInput,
                    onValueChange = { viewModel.onCustomTopicInputChanged(it) },
                    modifier = Modifier.fillMaxWidth().onFocusChanged { isCustomTopicFocused = it.isFocused },
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("Nhập chủ đề bạn tùy chỉnh", color = colors.hintText, fontSize = 13.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.accent,
                        unfocusedBorderColor = colors.border,
                        focusedContainerColor = colors.surface,
                        unfocusedContainerColor = colors.surface,
                        focusedTextColor = colors.mainText,
                        unfocusedTextColor = colors.mainText
                    )
                )
            } else {
                // ── LazyRow topic chips (styled with their own level color) ──
                if (filteredTopics.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text("Không tìm thấy chủ đề phù hợp.", color = colors.hintText, fontSize = 13.sp)
                    }
                } else {
                    LazyRow(
                        state = listState,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(filteredTopics) { i, pair ->
                            val idx = pair.first
                            val topic = pair.second
                            val isSelected = idx == selectedIdx
                            Surface(
                                modifier = Modifier.clip(RoundedCornerShape(12.dp)).clickable { viewModel.onTopicIndexSelected(idx) },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) colors.surfaceElevated else colors.surface,
                                border = BorderStroke(1.dp, if (isSelected) colors.mainText else colors.border)
                            ) {
                                Text(
                                    "${i + 1}. ${topic.name}",
                                    color = colors.mainText,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Random topic button
            Button(
                onClick = {
                    if (filteredTopics.isNotEmpty()) {
                        viewModel.onTopicIndexSelected(filteredTopics.random().first)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Icon(Icons.Rounded.AutoAwesome, null, tint = colors.accent, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text("Chọn ngẫu nhiên", color = colors.mainText, fontSize = 12.sp)
            }

            if (state.error != null) {
                Spacer(Modifier.height(12.dp))
                Text(state.error, color = colors.error, fontSize = 13.sp, textAlign = TextAlign.Center)
            }

            Spacer(Modifier.height(16.dp))
            Spacer(Modifier.height(80.dp))
            Spacer(Modifier.navigationBarsPadding())
        }

        val isStartEnabled = state.selectedTopic.isNotBlank() && !state.isGeneratingLesson

        // ── Pinned start button (styled with uniform Accent Color) ──
        Button(
            onClick = { viewModel.onStartLesson() },
            enabled = isStartEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 14.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accent,
                disabledContainerColor = colors.accent.copy(alpha = 0.5f)
            ),
            border = BorderStroke(2.dp, colors.border),
            shape = CircleShape
        ) {
            Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Bắt đầu bài học", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
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



@Composable
fun TopicSelectionContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    val lazyListState = rememberLazyListState()
    val selectedIdx = state.selectedTopicIndex
    var isSearchFocused by remember { mutableStateOf(false) }
    var isCustomTopicFocused by remember { mutableStateOf(false) }

    // Scroll to bottom when custom topic text field is focused
    LaunchedEffect(isCustomTopicFocused, lazyListState.layoutInfo.totalItemsCount) {
        if (isCustomTopicFocused) {
            val totalItems = lazyListState.layoutInfo.totalItemsCount
            if (totalItems > 0) {
                lazyListState.animateScrollToItem(totalItems - 1)
            }
        }
    }

    // Scroll to show search bar when search is focused
    LaunchedEffect(isSearchFocused) {
        if (isSearchFocused) {
            lazyListState.animateScrollToItem(index = 1)
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
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Spacer(Modifier.height(78.dp)) // 48dp header + 12dp bottom gap = 60dp
            }

            // Header icon + title side-by-side
            item {
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
                        "vi" -> "Trợ lý Tiếng Việt"
                        else -> "Trợ lý Tiếng Anh"
                    }
                    Text(
                        greetingText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mainText
                    )
                }
            }

            item {
                Spacer(Modifier.height(12.dp))
            }

            // ── Target Level System (CEFR / TOEIC / JLPT Selector) ──
            item {
                TargetLevelSystem(state, viewModel, colors)
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            // ── Carousel Card (styled with uniform accent color) ──
            item {
                TopicCarouselCard(state, colors, currentTopic)
            }

            item {
                Spacer(Modifier.height(16.dp))
            }

            // ── Search Bar (Custom compact styling) ──
            item {
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
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

            // Section Header: Title + Quick actions (Ngẫu nhiên & Switcher)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (state.isCustomTopicMode) "Nhập chủ đề tùy chỉnh"
                        else if (state.topicSearchQuery.isEmpty()) "Các chủ đề"
                        else "Kết quả tìm kiếm (${filteredTopics.size})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mutedText
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (!state.isCustomTopicMode && filteredTopics.isNotEmpty()) {
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { viewModel.onTopicIndexSelected(filteredTopics.random().first) }
                                    .padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = colors.accent
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "Ngẫu nhiên",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = colors.accent
                                )
                            }
                        }

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
                                text = if (state.isCustomTopicMode) "Chọn từ danh sách" else "Tự nhập",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colors.accent
                            )
                        }
                    }
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

            if (state.isCustomTopicMode) {
                item {
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
                }
            } else {
                // ── Vertical list of topic cards (fixes truncation completely) ──
                if (filteredTopics.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text("Không tìm thấy chủ đề phù hợp.", color = colors.hintText, fontSize = 13.sp)
                        }
                    }
                } else {
                    itemsIndexed(
                        items = filteredTopics,
                        key = { _, pair -> pair.second.name }
                    ) { i, pair ->
                        val idx = pair.first
                        val topic = pair.second
                        val isSelected = idx == selectedIdx

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.onTopicIndexSelected(idx) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) colors.surfaceElevated else colors.surface
                            ),
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) colors.accent else colors.border
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Circular badge on the left showing index
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(
                                            color = if (isSelected) colors.accent else colors.border.copy(alpha = 0.5f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${i + 1}",
                                        color = if (isSelected) Color.White else colors.mutedText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                // Full topic name wrapping to multiple lines if needed
                                Text(
                                    text = buildInlineAnnotatedString(topic.name, colors),
                                    color = colors.mainText,
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                    fontSize = 13.5.sp,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            if (state.error != null) {
                item {
                    Spacer(Modifier.height(12.dp))
                    Text(state.error, color = colors.error, fontSize = 13.sp, textAlign = TextAlign.Center)
                }
            }

            item {
                Spacer(Modifier.height(16.dp))
            }
            item {
                Spacer(Modifier.height(80.dp))
            }
            item {
                Spacer(Modifier.navigationBarsPadding())
            }
        }

        val isStartEnabled = state.selectedTopic.isNotBlank() && !state.isGeneratingLesson

        // ── Gradient Overlay behind the button ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            colors.background.copy(alpha = 0.8f),
                            colors.background
                        )
                    )
                )
        )

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

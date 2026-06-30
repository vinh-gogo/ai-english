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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
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
    var longPressedTopicId by remember { mutableStateOf<Int?>(null) }

    // Scroll to show custom topic card when focused
    LaunchedEffect(isCustomTopicFocused) {
        if (isCustomTopicFocused) {
            lazyListState.animateScrollToItem(index = 3)
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
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
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
                TopicCarouselCard(
                    state = state,
                    colors = colors,
                    currentTopic = currentTopic,
                    onCustomTopicInputChanged = { viewModel.onCustomTopicInputChanged(it) },
                    onFocusChanged = { isCustomTopicFocused = it },
                    onAddClick = { viewModel.showSaveToDbDialog() }
                )
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

            if (!state.isCustomTopicMode) {
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
                                .combinedClickable(
                                    onClick = { 
                                        longPressedTopicId = null
                                        viewModel.onTopicIndexSelected(idx) 
                                    },
                                    onLongClick = { 
                                        longPressedTopicId = topic.id 
                                    }
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) colors.surfaceElevated else colors.surface
                            ),
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) colors.accent else colors.border
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Rectangular index badge at the top-left edge (Restored refined user style)
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = if (isSelected) colors.accent else colors.border.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(
                                                topStart = 12.dp,
                                                topEnd = 4.dp,
                                                bottomEnd = 4.dp,
                                                bottomStart = 4.dp
                                            )
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${i + 1}",
                                        color = if (isSelected) Color.White else colors.mutedText,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Play audio button vertically centered on the left
                                IconButton(
                                    onClick = { viewModel.speakTopic(topic.name) },
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(start = 12.dp)
                                        .size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Nghe chủ đề",
                                        tint = if (isSelected) colors.accent else colors.mutedText,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                // Full topic name wrapping to multiple lines if needed (Padded to clear controls)
                                Text(
                                    text = buildInlineAnnotatedString(topic.name, colors),
                                    color = colors.mainText,
                                    fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                    fontSize = 13.5.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 48.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                                )

                                // Overlay with Remove button in the center
                                if (longPressedTopicId == topic.id) {
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .background(colors.background.copy(alpha = 0.85f))
                                            .clickable { longPressedTopicId = null },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Button(
                                            onClick = {
                                                viewModel.onRemoveTopic(topic.id)
                                                longPressedTopicId = null
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = colors.error
                                            ),
                                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.height(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Remove",
                                                modifier = Modifier.size(16.dp),
                                                tint = Color.White
                                            )
                                            Spacer(Modifier.width(6.dp))
                                            Text(
                                                text = "Xóa",
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
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
                .padding(horizontal = 16.dp)
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

        val showScrollToTop by remember {
            derivedStateOf {
                lazyListState.firstVisibleItemIndex >= 10
            }
        }
        val coroutineScope = rememberCoroutineScope()

        if (showScrollToTop) {
            SmallFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        if (lazyListState.firstVisibleItemIndex > 10) {
                            lazyListState.scrollToItem(10)
                        }
                        lazyListState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(end = 16.dp, bottom = 76.dp),
                containerColor = colors.accent,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowUpward,
                    contentDescription = "Scroll to top",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        if (state.showSaveToDbDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissSaveToDbDialog() },
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
                        text = "Bạn có muốn lưu chủ đề \"${state.customTopicInput}\" vào danh sách để sử dụng lại sau này không?",
                        fontSize = 14.sp,
                        color = colors.mainText
                    ) 
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.saveCustomTopicToDb(state.customTopicInput) }
                    ) {
                        Text("Lưu", color = colors.accent, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { viewModel.dismissSaveToDbDialog() }
                    ) {
                        Text("Hủy", color = colors.mutedText)
                    }
                },
                containerColor = colors.surface,
                textContentColor = colors.mainText,
                titleContentColor = colors.mainText
            )
        }
    }
}


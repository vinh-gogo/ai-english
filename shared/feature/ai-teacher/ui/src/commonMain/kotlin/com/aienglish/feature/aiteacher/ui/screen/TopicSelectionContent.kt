package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import com.aienglish.feature.aiteacher.ui.screen.components.topic.TopicRowItem
import com.aienglish.feature.aiteacher.ui.screen.components.topic.TopicActionHeader
import com.aienglish.feature.aiteacher.ui.screen.components.topic.TopicGreetingHeader
import com.aienglish.feature.aiteacher.ui.screen.components.topic.SaveTopicDialog

@Composable
fun TopicSelectionContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    val lazyListState = rememberLazyListState()
    val selectedIdx = state.selectedTopicIndex
    val isTtsLoading by viewModel.isTtsLoading.collectAsState()
    var isCustomTopicFocused by remember { mutableStateOf(false) }
    var longPressedTopicId by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Scroll to show custom topic card when focused
    LaunchedEffect(isCustomTopicFocused) {
        if (isCustomTopicFocused) {
            lazyListState.animateScrollToItem(index = 3)
        }
    }

    // List of topics with indices
    val topicsWithIndex = remember(state.topics) {
        state.topics.mapIndexed { i, t -> i to t }
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
                TopicGreetingHeader(state.targetLanguage, colors)
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

            // Section Header: Title + Quick actions (Ngẫu nhiên & Switcher)
            item {
                TopicActionHeader(
                    isCustomTopicMode = state.isCustomTopicMode,
                    filteredTopicsCount = topicsWithIndex.size,
                    onRandomClick = {
                        if (topicsWithIndex.isNotEmpty()) {
                            viewModel.onTopicIndexSelected(topicsWithIndex.random().first)
                        }
                    },
                    onToggleCustomMode = { viewModel.setCustomTopicMode(!state.isCustomTopicMode) },
                    colors = colors
                )
            }

            item {
                Spacer(Modifier.height(8.dp))
            }

            if (!state.isCustomTopicMode) {
                // ── Vertical list of topic cards ──
                if (topicsWithIndex.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text("Chưa có chủ đề nào.", color = colors.hintText, fontSize = 13.sp)
                        }
                    }
                } else {
                    itemsIndexed(
                        items = topicsWithIndex,
                        key = { _, pair -> pair.second.name }
                    ) { i, pair ->
                        val idx = pair.first
                        val topic = pair.second
                        val isSelected = idx == selectedIdx

                        TopicRowItem(
                            topic = topic,
                            i = i,
                            isSelected = isSelected,
                            isTtsLoading = isSelected && isTtsLoading,
                            isLongPressed = longPressedTopicId == topic.id,
                            onClick = {
                                longPressedTopicId = null
                                viewModel.onTopicIndexSelected(idx)
                            },
                            onLongClick = {
                                longPressedTopicId = topic.id
                            },
                            onDismissLongClick = {
                                longPressedTopicId = null
                            },
                            onSpeak = {
                                viewModel.speakTopic(topic.name)
                            },
                            onRemove = {
                                viewModel.onRemoveTopic(topic.id)
                                longPressedTopicId = null
                            },
                            colors = colors
                        )
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

        // ── Pinned Start Button at the Bottom ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp)
                .navigationBarsPadding()
                .padding(bottom = 14.dp)
        ) {
            Button(
                onClick = {
                    if (isStartEnabled) {
                        viewModel.onStartLesson()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isStartEnabled) colors.accent else colors.mutedText.copy(alpha = 0.3f),
                    contentColor = if (isStartEnabled) Color.White else colors.hintText
                ),
                shape = CircleShape,
                enabled = isStartEnabled
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Bắt đầu học",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // --- Nút Quay Lại Đầu Trang ---
        val showScrollToTop by remember {
            derivedStateOf {
                lazyListState.firstVisibleItemIndex > 10
            }
        }

        if (showScrollToTop) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 80.dp)
                    .navigationBarsPadding()
                    .size(40.dp)
                    .background(colors.accent, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowUpward,
                    contentDescription = "Scroll to top",
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }

        SaveTopicDialog(
            showDialog = state.showSaveToDbDialog,
            customTopicInput = state.customTopicInput,
            onDismiss = { viewModel.dismissSaveToDbDialog() },
            onSave = { viewModel.saveCustomTopicToDb(state.customTopicInput) },
            colors = colors
        )
    }
}

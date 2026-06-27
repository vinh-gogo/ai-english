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
import com.aienglish.feature.aiteacher.domain.model.Topics
import com.aienglish.feature.aiteacher.ui.viewmodel.LevelType
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel

@Composable
fun TopicSelectionContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    val listState = rememberLazyListState()
    val selectedIdx = state.selectedTopicIndex

    val filteredTopics = remember(state.topicSearchQuery) {
        Topics.list.mapIndexed { i, t -> i to t }
            .filter { it.second.contains(state.topicSearchQuery, ignoreCase = true) }
    }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(12.dp))

            // Header icon + title
            Icon(
                imageVector = Icons.Rounded.School,
                contentDescription = null,
                tint = colors.accent,
                modifier = Modifier.size(36.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "AI English chào bạn!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mainText
            )
            Text(
                "Chọn chủ đề luyện dịch 10 câu sang tiếng Anh",
                fontSize = 13.sp,
                color = colors.mutedText,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(12.dp))

            // ── Carousel Card (giống Sensei AI) ──
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val prevIdx = remember(selectedIdx, filteredTopics) {
                        val ci = filteredTopics.indexOfFirst { it.first == selectedIdx }
                        if (ci != -1 && filteredTopics.isNotEmpty())
                            filteredTopics[(ci - 1 + filteredTopics.size) % filteredTopics.size].first
                        else (selectedIdx - 1 + Topics.list.size) % Topics.list.size
                    }
                    IconButton(onClick = { viewModel.onTopicIndexSelected(prevIdx) }, modifier = Modifier.padding(start = 4.dp)) {
                        Icon(Icons.Default.ChevronLeft, "Trước", tint = colors.mainText)
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "CHỦ ĐỀ ${selectedIdx + 1} / ${Topics.list.size}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.accent
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            Topics.list.getOrNull(selectedIdx) ?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.mainText,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }

                    val nextIdx = remember(selectedIdx, filteredTopics) {
                        val ci = filteredTopics.indexOfFirst { it.first == selectedIdx }
                        if (ci != -1 && filteredTopics.isNotEmpty())
                            filteredTopics[(ci + 1) % filteredTopics.size].first
                        else (selectedIdx + 1) % Topics.list.size
                    }
                    IconButton(onClick = { viewModel.onTopicIndexSelected(nextIdx) }, modifier = Modifier.padding(end = 4.dp)) {
                        Icon(Icons.Default.ChevronRight, "Sau", tint = colors.mainText)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Random topic button
            Button(
                onClick = { viewModel.onTopicIndexSelected(Topics.list.indices.random()) },
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

            Spacer(Modifier.height(12.dp))

            // ── Search Bar ──
            OutlinedTextField(
                value = state.topicSearchQuery,
                onValueChange = { viewModel.onTopicSearchChange(it) },
                placeholder = { Text("Tìm kiếm chủ đề...", fontSize = 13.sp, color = colors.hintText) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.accent,
                    unfocusedBorderColor = colors.border,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface,
                    cursorColor = colors.accent
                ),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search, null, modifier = Modifier.size(18.dp), tint = colors.hintText)
                },
                trailingIcon = {
                    if (state.topicSearchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onTopicSearchChange("") }) {
                            Icon(Icons.Default.Close, null, modifier = Modifier.size(14.dp), tint = colors.hintText)
                        }
                    }
                }
            )

            Spacer(Modifier.height(8.dp))

            // Chips label
            Text(
                if (state.topicSearchQuery.isEmpty()) "Danh sách chủ đề"
                else "Kết quả (${filteredTopics.size})",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mutedText,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(6.dp))

            // ── LazyRow topic chips (giống Sensei AI) ──
            LazyRow(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(filteredTopics) { _, pair ->
                    val idx = pair.first
                    val topic = pair.second
                    val isSelected = idx == selectedIdx
                    Surface(
                        modifier = Modifier.clickable { viewModel.onTopicIndexSelected(idx) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) colors.accent else colors.surface,
                        border = BorderStroke(1.dp, if (isSelected) colors.accent else colors.border)
                    ) {
                        Text(
                            "${idx + 1}. $topic",
                            color = if (isSelected) Color.White else colors.mainText,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // ── Target Level System (CEFR / TOEIC Selector) ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Thang điểm mục tiêu",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText
                )

                // Tab-like selector for CEFR vs TOEIC
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.surfaceElevated)
                        .padding(2.dp)
                ) {
                    val isCefr = state.levelType == LevelType.CEFR
                    // CEFR Tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isCefr) colors.accent else Color.Transparent)
                            .clickable { viewModel.onLevelTypeSelected(LevelType.CEFR) }
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "CEFR",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCefr) Color.White else colors.mutedText
                        )
                    }
                    // TOEIC Tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (!isCefr) colors.accent else Color.Transparent)
                            .clickable { viewModel.onLevelTypeSelected(LevelType.TOEIC) }
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "TOEIC",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (!isCefr) Color.White else colors.mutedText
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Render Level Buttons based on selected LevelType
            if (state.levelType == LevelType.CEFR) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("A1", "A2", "B1", "B2", "C1", "C2").forEach { level ->
                        val isSelected = level == state.selectedCefrLevel
                        Surface(
                            modifier = Modifier.weight(1f).clickable { viewModel.onCefrLevelSelected(level) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) colors.accent else colors.surface,
                            border = BorderStroke(1.dp, if (isSelected) colors.accent else colors.border)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    level,
                                    color = if (isSelected) Color.White else colors.mainText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("300", "500", "700", ">900").forEach { level ->
                        val isSelected = level == state.selectedToeicLevel
                        Surface(
                            modifier = Modifier.weight(1f).clickable { viewModel.onToeicLevelSelected(level) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) colors.accent else colors.surface,
                            border = BorderStroke(1.dp, if (isSelected) colors.accent else colors.border)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    level,
                                    color = if (isSelected) Color.White else colors.mainText,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            if (state.error != null) {
                Spacer(Modifier.height(12.dp))
                Text(state.error, color = colors.error, fontSize = 13.sp, textAlign = TextAlign.Center)
            }

            Spacer(Modifier.height(16.dp))
        }

        // ── Pinned start button ──
        Button(
            onClick = { viewModel.onStartLesson() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(bottom = 4.dp)
                .navigationBarsPadding(),
            colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Bắt đầu bài học", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

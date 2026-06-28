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
        "B2", "700", "N2", "HSK4" -> Color(0xFFEF4444)      // Đỏ Coral
        "C1", "900", "N1", "HSK5" -> colors.accent          // Màu accent chính (Blood đỏ sẫm)
        "C2", "HSK6" -> Color(0xFF8B5CF6)                   // Tím Violet
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
    val selectedIdx = state.selectedTopicIndex

    // Determine current level code and target color
    val activeLevelCode = when (state.levelType) {
        LevelType.CEFR -> state.selectedCefrLevel
        LevelType.TOEIC -> state.selectedToeicLevel
        LevelType.JLPT -> state.selectedJlptLevel
        LevelType.HSK -> state.selectedHskLevel
    }
    val levelColor = getLevelColor(activeLevelCode, colors)

    // Filter topics: show topics containing search query (pre-filtered by compatible levels in ViewModel)
    val filteredTopics = remember(state.topics, state.topicSearchQuery) {
        state.topics.mapIndexed { i, t -> i to t }
            .filter { pair ->
                pair.second.name.contains(state.topicSearchQuery, ignoreCase = true)
            }
    }

    // Determine current selected topic details for custom coloring
    val currentTopic = remember(state.topics, selectedIdx) { state.topics.getOrNull(selectedIdx) }
    val currentTopicLevel = currentTopic?.levelCode ?: activeLevelCode
    val currentTopicColor = getLevelColor(currentTopicLevel, colors)

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

            // Header icon + title side-by-side
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.School,
                    contentDescription = null,
                    tint = colors.accent,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "AI English chào bạn!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mainText
                )
            }

            Spacer(Modifier.height(12.dp))

            // ── Carousel Card (styled with uniform accent color) ──
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.accent.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!state.isCustomTopicMode) {
                        val prevIdx = remember(selectedIdx, filteredTopics) {
                            val ci = filteredTopics.indexOfFirst { it.first == selectedIdx }
                            if (ci != -1 && filteredTopics.isNotEmpty())
                                filteredTopics[(ci - 1 + filteredTopics.size) % filteredTopics.size].first
                            else selectedIdx
                        }
                        IconButton(onClick = { viewModel.onTopicIndexSelected(prevIdx) }, modifier = Modifier.padding(start = 4.dp)) {
                            Icon(Icons.Default.ChevronLeft, "Trước", tint = colors.mainText)
                        }
                    } else {
                        Spacer(Modifier.width(48.dp))
                    }

                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight().padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        val localIndexText = remember(selectedIdx, filteredTopics) {
                            val ci = filteredTopics.indexOfFirst { it.first == selectedIdx }
                            if (ci != -1) "${ci + 1} / ${filteredTopics.size}" else "1 / 1"
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                if (state.isCustomTopicMode) "CHỦ ĐỀ TỰ CHỌN" else "CHỦ ĐỀ $localIndexText",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.accent
                            )
                            if (!state.isCustomTopicMode) {
                                Spacer(Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = colors.accent.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = if (currentTopicLevel == "900") ">900" else currentTopicLevel,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = colors.accent,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        
                        Box(
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                if (state.isCustomTopicMode) {
                                    if (state.customTopicInput.isBlank()) "Hãy nhập chủ đề tự do..." else state.customTopicInput
                                } else {
                                    currentTopic?.name ?: ""
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.isCustomTopicMode && state.customTopicInput.isBlank()) colors.hintText else colors.mainText,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }

                    if (!state.isCustomTopicMode) {
                        val nextIdx = remember(selectedIdx, filteredTopics) {
                            val ci = filteredTopics.indexOfFirst { it.first == selectedIdx }
                            if (ci != -1 && filteredTopics.isNotEmpty())
                                filteredTopics[(ci + 1) % filteredTopics.size].first
                            else selectedIdx
                        }
                        IconButton(onClick = { viewModel.onTopicIndexSelected(nextIdx) }, modifier = Modifier.padding(end = 4.dp)) {
                            Icon(Icons.Default.ChevronRight, "Sau", tint = colors.mainText)
                        }
                    } else {
                        Spacer(Modifier.width(48.dp))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Target Level System (CEFR / TOEIC / JLPT Selector) ──
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

                if (state.targetLanguage == "ja") {
                    // Only 1 option for Japanese (JLPT), display it as a simple tag
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = colors.accent
                    ) {
                        Text(
                            "JLPT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                } else if (state.targetLanguage == "zh") {
                    // Only 1 option for Chinese (HSK), display it as a simple tag
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = colors.accent
                    ) {
                        Text(
                            "HSK",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    // English: Show CEFR and TOEIC
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(colors.surfaceElevated)
                            .padding(2.dp)
                    ) {
                        // CEFR Tab
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (state.levelType == LevelType.CEFR) colors.accent else Color.Transparent)
                                .clickable { viewModel.onLevelTypeSelected(LevelType.CEFR) }
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "CEFR",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.levelType == LevelType.CEFR) Color.White else colors.mutedText
                            )
                        }
                        // TOEIC Tab
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (state.levelType == LevelType.TOEIC) colors.accent else Color.Transparent)
                                .clickable { viewModel.onLevelTypeSelected(LevelType.TOEIC) }
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "TOEIC",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.levelType == LevelType.TOEIC) Color.White else colors.mutedText
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Render Level Buttons based on selected LevelType
            when (state.levelType) {
                LevelType.CEFR -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("A1", "A2", "B1", "B2", "C1", "C2").forEach { level ->
                            val isSelected = level == state.selectedCefrLevel
                            val thisLevelColor = getLevelColor(level, colors)
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
                }
                LevelType.TOEIC -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("300", "500", "700", "900").forEach { level ->
                            val isSelected = level == state.selectedToeicLevel
                            val thisLevelColor = getLevelColor(level, colors)
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
                                        text = if (level == "900") ">900" else level,
                                        color = if (isSelected) Color.White else colors.mainText,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
                LevelType.JLPT -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("N5", "N4", "N3", "N2", "N1").forEach { level ->
                            val isSelected = level == state.selectedJlptLevel
                            val thisLevelColor = getLevelColor(level, colors)
                            Surface(
                                modifier = Modifier.weight(1f).clickable { viewModel.onJlptLevelSelected(level) },
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
                LevelType.HSK -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("HSK1", "HSK2", "HSK3", "HSK4", "HSK5", "HSK6").forEach { level ->
                            val isSelected = level == state.selectedHskLevel
                            Surface(
                                modifier = Modifier.weight(1f).clickable { viewModel.onHskLevelSelected(level) },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) colors.accent else colors.surface,
                                border = BorderStroke(1.dp, if (isSelected) colors.accent else colors.border)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        level.replace("HSK", "H"),
                                        color = if (isSelected) Color.White else colors.mainText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Search Bar (Custom compact styling) ──
            var isSearchFocused by remember { mutableStateOf(false) }
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
                    modifier = Modifier.fillMaxWidth(),
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
                                modifier = Modifier.clickable { viewModel.onTopicIndexSelected(idx) },
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
        }

        // ── Pinned start button (styled with uniform Accent Color) ──
        Button(
            onClick = { viewModel.onStartLesson() },
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Bắt đầu bài học", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

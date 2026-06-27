package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import com.aienglish.core.designsystem.theme.LocalAppColors
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.EnglishSentence
import com.aienglish.feature.aiteacher.domain.model.Topics
import com.aienglish.feature.aiteacher.ui.viewmodel.ProfileViewModel
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import org.koin.compose.koinInject

// ─────────────────────────────────────────────────────────────
// Entry point
// ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherScreen(
    viewModel: TeacherViewModel = koinInject(),
    profileViewModel: ProfileViewModel = koinInject(),
    onGoogleSignIn: () -> Unit = {},
    onGoogleSignOut: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val profileState by profileViewModel.state.collectAsState()
    val colors = LocalAppColors.current
    var showHistory by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when {
                            state.feedback != null -> "Kết quả bài học"
                            state.lesson != null -> "Luyện dịch tiếng Anh"
                            else -> "AI English Teacher"
                        },
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mainText
                    )
                },
                navigationIcon = {
                    if (state.lesson != null || state.feedback != null) {
                        IconButton(onClick = { viewModel.onReset() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = colors.mainText
                            )
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(
                                            colors.accent,
                                            colors.accent.copy(alpha = 0.7f)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Rounded.School,
                                contentDescription = "AI English",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                actions = {
                        // History button
                        IconButton(onClick = { showHistory = true }) {
                            Icon(
                                Icons.Rounded.History,
                                contentDescription = "Lịch sử",
                                tint = colors.mutedText,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        // Profile button
                        IconButton(onClick = { profileViewModel.showProfile() }) {
                        if (profileState.isSignedIn && profileState.displayName != null) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(colors.accent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    profileState.displayName!!.first().uppercase(),
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Icon(
                                Icons.Rounded.AccountCircle,
                                contentDescription = "Profile",
                                tint = colors.mutedText,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.surface,
                    scrolledContainerColor = colors.surface
                )
            )
        },

        containerColor = colors.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Profile sheet overlay
            if (profileState.showProfile) {
                ProfileSheet(
                    profileViewModel = profileViewModel,
                    onGoogleSignIn = onGoogleSignIn,
                    onGoogleSignOut = onGoogleSignOut
                )
            }
            // History sheet overlay
            if (showHistory) {
                HistorySheet(onDismiss = { showHistory = false })
            }
            AnimatedContent(
                targetState = when {
                    state.feedback != null -> "feedback"
                    state.lesson != null -> "lesson"
                    else -> "topic"
                },
                transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) }
            ) { screen ->
                when (screen) {
                    "feedback" -> FeedbackContent(state, viewModel, colors)
                    "lesson" -> LessonContent(state, viewModel, colors)
                    else -> TopicSelectionContent(state, viewModel, colors)
                }
            }

            // Explanation bottom sheet overlay
            if (state.selectedExplainIndex != null) {
                ExplanationOverlay(state, viewModel, colors)
            }

            // Error snack
            if (state.error != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = colors.error),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(state.error ?: "", color = Color.White, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            IconButton(onClick = { viewModel.onDismissError() }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Screen 1: Topic Selection — mirrors Sensei AI layout exactly
// ─────────────────────────────────────────────────────────────
@Composable
private fun TopicSelectionContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: com.aienglish.core.designsystem.theme.AppColors
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
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

            Spacer(Modifier.height(16.dp))

            // ── CEFR Level Selector (giống JLPT selector của Sensei AI) ──
            Text(
                "Trình độ CEFR mục tiêu",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mutedText,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.height(8.dp))
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

            if (state.error != null) {
                Spacer(Modifier.height(12.dp))
                Text(state.error!!, color = colors.error, fontSize = 13.sp, textAlign = TextAlign.Center)
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

// ─────────────────────────────────────────────────────────────
// Screen 2: Lesson — sentence by sentence (giống LearningContent)
// ─────────────────────────────────────────────────────────────
@Composable
private fun LessonContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: com.aienglish.core.designsystem.theme.AppColors
) {
    val lesson = state.lesson ?: return
    val total = lesson.sentences.size
    val idx = state.currentSentenceIndex
    val sentence = lesson.sentences.getOrNull(idx) ?: return
    val userAnswer = state.userAnswers[idx] ?: ""
    val hasChecked = state.checkedAnswers.contains(idx)
    val answeredCount = state.userAnswers.count { it.value.isNotBlank() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Progress row (giống Sensei AI)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "CÂU HỎI ${idx + 1} / $total",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.accent
                )
                LinearProgressIndicator(
                    progress = { (idx + 1).toFloat() / total },
                    modifier = Modifier.width(120.dp).height(6.dp).clip(CircleShape),
                    color = colors.accent,
                    trackColor = colors.border
                )
            }

            Spacer(Modifier.height(12.dp))

            // Prompt card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        "Dịch sang tiếng Anh:",
                        fontSize = 12.sp,
                        color = colors.mutedText
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        sentence.vietnamese,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.mainText,
                        lineHeight = 24.sp
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // Input field (OutlinedTextField like Sensei AI)
            OutlinedTextField(
                value = userAnswer,
                onValueChange = { if (!hasChecked) viewModel.onAnswerChanged(idx, it) },
                placeholder = { Text("Nhập câu dịch tiếng Anh của bạn...", color = colors.hintText, fontSize = 13.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.accent,
                    unfocusedBorderColor = colors.border,
                    cursorColor = colors.accent,
                    focusedContainerColor = colors.surface,
                    unfocusedContainerColor = colors.surface
                ),
                singleLine = false,
                maxLines = 3,
                enabled = !hasChecked
            )

            // After checking — show answer and tokens
            if (hasChecked) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "Đáp án chuẩn:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.mutedText
                )
                Spacer(Modifier.height(6.dp))

                // Token chips in FlowRow
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    sentence.tokens.forEach { token ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, colors.border)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                                Text(token.word, color = colors.mainText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                if (token.phonetic.isNotEmpty()) Text(token.phonetic, color = colors.accent, fontSize = 10.sp)
                                if (token.partOfSpeech.isNotEmpty()) Text(token.partOfSpeech, color = colors.hintText, fontSize = 10.sp, fontStyle = FontStyle.Italic)
                            }
                        }
                    }
                }

                // Answer card
                Spacer(Modifier.height(10.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.correct.copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, colors.correct.copy(alpha = 0.3f))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.CheckCircle, null, tint = colors.correct, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(sentence.english, color = colors.correct, fontSize = 14.sp, fontStyle = FontStyle.Italic)
                    }
                }

                // Explanation card if available
                val explanation = state.explanations[idx]
                if (state.isExplaining && state.selectedExplainIndex == idx) {
                    Spacer(Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = colors.accent, strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("AI đang giải thích...", fontSize = 13.sp, color = colors.mutedText)
                    }
                } else if (explanation != null) {
                    Spacer(Modifier.height(10.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
                        border = BorderStroke(0.5.dp, colors.border)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("💡 Giải thích chi tiết:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = colors.accent)
                            Spacer(Modifier.height(4.dp))
                            MarkdownText(explanation, colors)
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        // Pinned bottom action
        Box(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
            if (!hasChecked) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Skip button
                    Button(
                        onClick = { viewModel.onCheckAnswer(idx) },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.surface),
                        border = BorderStroke(1.dp, colors.border),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Bỏ qua", color = colors.mutedText)
                    }
                    // Check button
                    Button(
                        onClick = { viewModel.onCheckAnswer(idx) },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).height(48.dp)
                    ) {
                        Text("Kiểm tra", color = Color.White)
                    }
                }
            } else {
                if (idx < total - 1) {
                    // Next sentence
                    Button(
                        onClick = { viewModel.onNextSentence() },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Text("Câu tiếp theo", color = Color.White)
                        Icon(Icons.Default.ChevronRight, null)
                    }
                } else {
                    // Last sentence — submit directly since explanation is auto-triggered
                    Button(
                        onClick = { viewModel.onSubmitLesson() },
                        colors = ButtonDefaults.buttonColors(containerColor = colors.correct),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        if (state.isEvaluating) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Xem đánh giá", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Screen 3: Feedback
// ─────────────────────────────────────────────────────────────
@Composable
private fun FeedbackContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: com.aienglish.core.designsystem.theme.AppColors
) {
    val feedback = state.feedback ?: return
    val lesson = state.lesson

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Score badge
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            feedback.score >= 8 -> colors.correct
                            feedback.score >= 5 -> colors.warning
                            else -> colors.error
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("${feedback.score}/10", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(10.dp))
            Text("Kết quả của bạn", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = colors.mainText)
            Text(
                "${state.selectedTopic} · CEFR ${state.selectedCefrLevel}",
                fontSize = 12.sp,
                color = colors.hintText
            )

            Spacer(Modifier.height(14.dp))

            // Review card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, colors.border)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Nhận xét từ AI Teacher", color = colors.accent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    MarkdownText(feedback.review, colors)
                }
            }

            // Per-sentence review
            if (lesson != null) {
                Spacer(Modifier.height(14.dp))
                Text("Chi tiết từng câu", color = colors.mutedText, fontSize = 12.sp, fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start))
                Spacer(Modifier.height(8.dp))
                lesson.sentences.forEachIndexed { i, sentence ->
                    val userAnswer = state.userAnswers[i]
                    SentenceReviewCard(i, sentence, userAnswer, state, viewModel, colors)
                    Spacer(Modifier.height(8.dp))
                }
            }

            Spacer(Modifier.height(80.dp))
        }

        // Reset button
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp).navigationBarsPadding()) {
            Button(
                onClick = { viewModel.onReset() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.accent),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Rounded.Refresh, null)
                Spacer(Modifier.width(8.dp))
                Text("Luyện tập bài mới", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SentenceReviewCard(
    index: Int,
    sentence: EnglishSentence,
    userAnswer: String?,
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: com.aienglish.core.designsystem.theme.AppColors
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, colors.border)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Câu ${index + 1}", color = colors.hintText, fontSize = 11.sp)
            Spacer(Modifier.height(4.dp))
            Text(sentence.vietnamese, color = colors.mainText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.CheckCircle, null, tint = colors.correct, modifier = Modifier.size(13.dp))
                Spacer(Modifier.width(4.dp))
                Text(sentence.english, color = colors.correct, fontSize = 12.sp, fontStyle = FontStyle.Italic)
            }
            if (!userAnswer.isNullOrBlank()) {
                Spacer(Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, null, tint = colors.mutedText, modifier = Modifier.size(11.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(userAnswer, color = colors.mutedText, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { viewModel.onExplainSentence(index) },
                modifier = Modifier.height(30.dp),
                border = BorderStroke(1.dp, colors.accent.copy(0.5f)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Icon(Icons.Default.Info, null, tint = colors.accent, modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    if (state.explanations.containsKey(index)) "Xem lại" else "Giải thích",
                    color = colors.accent,
                    fontSize = 11.sp
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Explanation overlay (bottom sheet style)
// ─────────────────────────────────────────────────────────────
@Composable
private fun ExplanationOverlay(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: com.aienglish.core.designsystem.theme.AppColors
) {
    val idx = state.selectedExplainIndex ?: return
    val explanation = state.explanations[idx]
    val sentence = state.lesson?.sentences?.getOrNull(idx)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
            .clickable { viewModel.onCloseExplanation() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .align(Alignment.BottomCenter)
                .clickable(enabled = false) {},
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(colors.border))
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💡 Giải thích chi tiết", color = colors.mainText, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    IconButton(onClick = { viewModel.onCloseExplanation() }) {
                        Icon(Icons.Default.Close, null, tint = colors.hintText)
                    }
                }
                HorizontalDivider(color = colors.border)
                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
                ) {
                    sentence?.let {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = colors.surfaceElevated),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, colors.accent.copy(0.3f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("🇻🇳 ${it.vietnamese}", color = colors.mainText, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                Spacer(Modifier.height(4.dp))
                                Text("🇬🇧 ${it.english}", color = colors.correct, fontSize = 13.sp, fontStyle = FontStyle.Italic)
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                    if (state.isExplaining) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = colors.accent, modifier = Modifier.size(28.dp))
                                Spacer(Modifier.height(8.dp))
                                Text("AI đang phân tích...", color = colors.mutedText, fontSize = 13.sp)
                            }
                        }
                    } else if (explanation != null) {
                        MarkdownText(explanation, colors)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Markdown renderer - ported from MainichiKanji
// ─────────────────────────────────────────────────────────────
private sealed interface Block {
    data class Heading(val level: Int, val text: String) : Block
    data class Paragraph(val text: String) : Block
    data class Bullet(val text: String) : Block
    data class Ordered(val index: String, val text: String) : Block
    data object Divider : Block
}

private fun parseBlocks(text: String): List<Block> {
    val groups = text.split(Regex("\\n{2,}"))
    val blocks = mutableListOf<Block>()

    for (group in groups) {
        val trimmed = group.trim()
        if (trimmed.isBlank()) continue

        // 1. Divider
        if (trimmed.all { it == '-' } && trimmed.length >= 3) {
            blocks.add(Block.Divider)
            continue
        }

        val lines = trimmed.lines().map { it.trimEnd() }

        // If it's a single line and starts with #, it's a heading
        if (lines.size == 1 && trimmed.startsWith("#")) {
            val level = trimmed.takeWhile { it == '#' }.length
            val rest = trimmed.substring(level).trim()
            blocks.add(Block.Heading(level, rest))
            continue
        }

        // Check if all lines in this group are bullet points or ordered list items
        var isAllList = true
        val parsedLines = lines.map { line ->
            val cleanLine = line.trimStart()
            if (cleanLine.startsWith("#")) {
                isAllList = false
                null
            } else {
                val bulletMatch = Regex("^[*\\-+ ]\\s+(.*)").find(cleanLine)
                val orderedMatch = Regex("^([0-9a-zA-Z]+[.)])\\s+(.*)").find(cleanLine)
                
                if (bulletMatch != null) {
                    Block.Bullet(bulletMatch.groupValues[1].trimStart())
                } else if (orderedMatch != null) {
                    Block.Ordered(orderedMatch.groupValues[1], orderedMatch.groupValues[2].trimStart())
                } else {
                    isAllList = false
                    null
                }
            }
        }

        if (isAllList) {
            parsedLines.forEach { block ->
                if (block != null) {
                    blocks.add(block)
                }
            }
        } else {
            // Mixed lines
            var currentParagraph = StringBuilder()
            for (line in lines) {
                val cleanLine = line.trimStart()
                if (cleanLine.startsWith("#")) {
                    if (currentParagraph.isNotEmpty()) {
                        blocks.add(Block.Paragraph(currentParagraph.toString().trim()))
                        currentParagraph = StringBuilder()
                    }
                    val level = cleanLine.takeWhile { it == '#' }.length
                    val rest = cleanLine.substring(level).trim()
                    blocks.add(Block.Heading(level, rest))
                } else {
                    val bulletMatch = Regex("^[*\\-+ ]\\s+(.*)").find(cleanLine)
                    val orderedMatch = Regex("^([0-9a-zA-Z]+[.)])\\s+(.*)").find(cleanLine)
                    
                    if (bulletMatch != null || orderedMatch != null) {
                        if (currentParagraph.isNotEmpty()) {
                            blocks.add(Block.Paragraph(currentParagraph.toString().trim()))
                            currentParagraph = StringBuilder()
                        }
                        if (bulletMatch != null) {
                            blocks.add(Block.Bullet(bulletMatch.groupValues[1].trimStart()))
                        } else {
                            blocks.add(Block.Ordered(orderedMatch!!.groupValues[1], orderedMatch.groupValues[2].trimStart()))
                        }
                    } else {
                        if (currentParagraph.isNotEmpty()) {
                            currentParagraph.append("\n")
                        }
                        currentParagraph.append(line)
                    }
                }
            }
            if (currentParagraph.isNotEmpty()) {
                blocks.add(Block.Paragraph(currentParagraph.toString().trim()))
            }
        }
    }

    return blocks
}

private fun buildInlineAnnotatedString(
    text: String,
    colors: com.aienglish.core.designsystem.theme.AppColors
): AnnotatedString {
    return buildAnnotatedString {
        var i = 0
        while (i < text.length) {
            when {
                text.startsWith("***", i) -> {
                    val close = text.indexOf("***", i + 3)
                    if (close != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                            append(text.substring(i + 3, close))
                        }
                        i = close + 3
                    } else {
                        append(text[i]); i++
                    }
                }
                text.startsWith("___", i) -> {
                    val close = text.indexOf("___", i + 3)
                    if (close != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                            append(text.substring(i + 3, close))
                        }
                        i = close + 3
                    } else {
                        append(text[i]); i++
                    }
                }
                text.startsWith("**", i) -> {
                    val close = text.indexOf("**", i + 2)
                    if (close != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.mainText)) {
                            append(text.substring(i + 2, close))
                        }
                        i = close + 2
                    } else {
                        append(text[i]); i++
                    }
                }
                text.startsWith("__", i) -> {
                    val close = text.indexOf("__", i + 2)
                    if (close != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = colors.mainText)) {
                            append(text.substring(i + 2, close))
                        }
                        i = close + 2
                    } else {
                        append(text[i]); i++
                    }
                }
                text.startsWith("*", i) && !text.startsWith("**", i) -> {
                    val close = text.indexOf('*', i + 1)
                    if (close != -1) {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = colors.mutedText)) {
                            append(text.substring(i + 1, close))
                        }
                        i = close + 1
                    } else {
                        append(text[i]); i++
                    }
                }
                text.startsWith("_", i) && !text.startsWith("__", i) -> {
                    val close = text.indexOf('_', i + 1)
                    if (close != -1) {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic, color = colors.mutedText)) {
                            append(text.substring(i + 1, close))
                        }
                        i = close + 1
                    } else {
                        append(text[i]); i++
                    }
                }
                text.startsWith("`", i) -> {
                    val close = text.indexOf("`", i + 1)
                    if (close != -1) {
                        withStyle(SpanStyle(color = colors.accent, fontWeight = FontWeight.Medium)) {
                            append(text.substring(i + 1, close))
                        }
                        i = close + 1
                    } else {
                        append(text[i]); i++
                    }
                }
                else -> {
                    append(text[i]); i++
                }
            }
        }
    }
}

@Composable
private fun MarkdownText(
    markdown: String,
    colors: com.aienglish.core.designsystem.theme.AppColors
) {
    val blocks = remember(markdown) { parseBlocks(markdown) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        for (block in blocks) {
            when (block) {
                is Block.Divider -> {
                    HorizontalDivider(
                        thickness = 0.5.dp,
                        color = colors.border,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                is Block.Heading -> {
                    val annotated = remember<AnnotatedString>(block.text) { buildInlineAnnotatedString(block.text, colors) }
                    val hSize = when (block.level) {
                        1 -> 16.sp
                        2 -> 14.sp
                        else -> 13.sp
                    }
                    Text(
                        text = annotated,
                        fontSize = hSize,
                        color = colors.mainText,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 6.dp, bottom = 2.dp)
                    )
                }
                is Block.Paragraph -> {
                    val annotated = remember<AnnotatedString>(block.text) { buildInlineAnnotatedString(block.text, colors) }
                    Text(
                        text = annotated,
                        fontSize = 13.sp,
                        color = colors.mutedText,
                        lineHeight = 20.sp
                    )
                }
                is Block.Bullet -> {
                    val annotated = remember<AnnotatedString>(block.text) { buildInlineAnnotatedString(block.text, colors) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            fontSize = 13.sp,
                            color = colors.accent,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(16.dp)
                        )
                        Text(
                            text = annotated,
                            fontSize = 13.sp,
                            color = colors.mutedText,
                            lineHeight = 20.sp
                        )
                    }
                }
                is Block.Ordered -> {
                    val annotated = remember<AnnotatedString>(block.text) { buildInlineAnnotatedString(block.text, colors) }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = block.index,
                            fontSize = 13.sp,
                            color = colors.accent,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(24.dp)
                        )
                        Text(
                            text = annotated,
                            fontSize = 13.sp,
                            color = colors.mutedText,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.LocalAppColors
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import com.aienglish.feature.history.ui.screen.HistorySheet
import com.aienglish.feature.profile.ui.screen.ProfileSheet
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
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

    LaunchedEffect(profileState.showProfile) {
        if (!profileState.showProfile) {
            viewModel.syncTargetLanguage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when {
                            state.feedback != null -> "Kết quả bài học"
                            state.lesson != null -> {
                                if (state.selectedLevelText.startsWith("N")) "Luyện dịch tiếng Nhật"
                                else "Luyện dịch tiếng Anh"
                            }
                            else -> "AI Teacher"
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
                .imePadding()
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
                HistorySheet(
                    onReview = { item -> viewModel.onReviewLesson(item) },
                    onReTest = { item -> viewModel.onReTestLesson(item) },
                    onContinue = { item -> viewModel.onResumeLesson(item) },
                    onDismiss = { showHistory = false }
                )
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

            // Error snackbar
            val error = state.error
            if (error != null) {
                val clipboardManager = LocalClipboardManager.current
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
                            SelectionContainer(modifier = Modifier.weight(1f)) {
                                Text(error, color = Color.White, fontSize = 13.sp)
                            }
                            Spacer(Modifier.width(8.dp))
                            IconButton(
                                onClick = { clipboardManager.setText(AnnotatedString(error)) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, "Copy", tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                            Spacer(Modifier.width(4.dp))
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

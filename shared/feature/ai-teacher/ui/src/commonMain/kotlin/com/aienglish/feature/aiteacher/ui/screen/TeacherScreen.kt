package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.LocalAppColors
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import com.aienglish.feature.history.ui.screen.HistorySheet
import com.aienglish.feature.history.ui.viewmodel.HistoryViewModel
import com.aienglish.core.designsystem.components.harmony.SeigaihaBackground
import com.aienglish.feature.profile.ui.screen.ProfileSheet
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
import com.aienglish.feature.pro.ui.screen.VipSheet
import com.aienglish.feature.pro.ui.viewmodel.ProViewModel
import com.aienglish.feature.pro.ui.intent.ProIntent
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.aiteacher.ui.screen.components.teacher.TeacherTopNavigation
import org.koin.compose.koinInject

// ─────────────────────────────────────────────────────────────
// Entry point
// ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherScreen(
    viewModel: TeacherViewModel = koinInject(),
    profileViewModel: ProfileViewModel = koinInject(),
    proViewModel: ProViewModel = koinInject(),
    historyViewModel: HistoryViewModel = koinInject(),
    onGoogleSignIn: () -> Unit = {},
    onGoogleSignOut: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val profileState by profileViewModel.state.collectAsState()
    val proState by proViewModel.state.collectAsState()
    val colors = LocalAppColors.current
    var showHistory by remember { mutableStateOf(false) }

    LaunchedEffect(profileState.showProfile, proState.showVip, state.lesson, state.feedback) {
        if (profileState.showProfile || proState.showVip) {
            viewModel.fetchMonthlyUsage { usage, limit ->
                profileViewModel.updateMonthlyUsage(usage, limit)
            }
        }
    }

    Scaffold(
        containerColor = colors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            // Seigaiha Cloud Background
            SeigaihaBackground(isDark = true)

            // Profile sheet overlay
            if (profileState.showProfile) {
                ProfileSheet(
                    profileViewModel = profileViewModel,
                    onGoogleSignIn = onGoogleSignIn,
                    onGoogleSignOut = onGoogleSignOut
                )
            }
            // VIP sheet overlay
            if (proState.showVip) {
                VipSheet(
                    proViewModel = proViewModel
                )
            }
            // History sheet overlay
            val handleReview = remember(viewModel) {
                { item: LessonHistory -> viewModel.onReviewLesson(item) }
            }
            val handleReTest = remember(viewModel) {
                { item: LessonHistory -> viewModel.onReTestLesson(item) }
            }
            val handleContinue = remember(viewModel) {
                { item: LessonHistory -> viewModel.onResumeLesson(item) }
            }
            val handleDismissHistory = remember {
                { showHistory = false }
            }

            if (showHistory) {
                HistorySheet(
                    historyViewModel = historyViewModel,
                    onReview = handleReview,
                    onReTest = handleReTest,
                    onContinue = handleContinue,
                    onDismiss = handleDismissHistory
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

            // ── Top Gradient Overlay behind the header ──
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colors.background,
                                colors.background.copy(alpha = 0.8f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Status bar background spacer (Black)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .background(Color.Black)
            )

            // Navigation bar background spacer (Black)
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsBottomHeight(WindowInsets.navigationBars)
                    .background(Color.Black)
                    .align(Alignment.BottomCenter)
            )

            // Floating Top Groups
            TeacherTopNavigation(
                showBackButton = state.lesson != null || state.feedback != null,
                isSignedIn = profileState.isSignedIn,
                displayName = profileState.displayName,
                isPaid = profileState.isPaid,
                onBackClick = { viewModel.onReset() },
                onHistoryClick = { showHistory = true },
                onProfileClick = { profileViewModel.showProfile() },
                onProClick = { proViewModel.onIntent(ProIntent.ShowVip) },
                colors = colors
            )

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

            // Info snackbar
            val infoMessage = state.infoMessage
            if (infoMessage != null) {
                LaunchedEffect(infoMessage) {
                    kotlinx.coroutines.delay(2000)
                    viewModel.clearInfoMessage()
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)), // Green success color
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(infoMessage, color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

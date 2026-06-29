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
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(top = 18.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Group 1: Back and Title (Segmented control style, height 48dp)
                Row(
                    modifier = Modifier
                        .height(48.dp)
                        .background(colors.surface, CircleShape)
                        .border(2.dp, colors.border, CircleShape),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.lesson != null || state.feedback != null) {
                        // Back Button Segment (Rounded on the left, flat on the right)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(32.dp)
                                .clip(RoundedCornerShape(topStart = 100.dp, bottomStart = 100.dp))
                                .clickable { viewModel.onReset() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = colors.mainText,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(20.dp)
                                .background(colors.border)
                        )

                        // Title Text Segment (with safe padding)
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(start = 8.dp, end = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when {
                                    state.feedback != null -> "Kết quả"
                                    state.lesson != null -> "Luyện tập"
                                    else -> "AI Lingua"
                                },
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.mainText
                            )
                        }
                    } else {
                        // Title Text only (standard padding)
                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "AI Lingua",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = colors.mainText
                            )
                        }
                    }
                }

                // Group 2: History, Profile, Pro badge (Grouped inside a single 48dp capsule aligned to the right)
                Row(
                    modifier = Modifier
                        .height(48.dp)
                        .background(colors.surface, CircleShape)
                        .border(2.dp, colors.border, CircleShape),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // History Button (Rounded on the left, flat on the right)
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(40.dp)
                            .clip(RoundedCornerShape(topStart = 100.dp, bottomStart = 100.dp))
                            .clickable { showHistory = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Rounded.History,
                            contentDescription = "Lịch sử",
                            tint = colors.mutedText,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(colors.border)
                    )

                    // Profile Button (Perfect rectangle/square click ripple in the middle)
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(40.dp)
                            .clip(RoundedCornerShape(0.dp))
                            .clickable { profileViewModel.showProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileState.isSignedIn && profileState.displayName != null) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(colors.accent),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    profileState.displayName!!.first().uppercase(),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Icon(
                                Icons.Rounded.AccountCircle,
                                contentDescription = "Profile",
                                tint = colors.mutedText,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Divider
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(20.dp)
                            .background(colors.border)
                    )

                    // PRO Button (Rounded on the right, flat on the left)
                    Row(
                        modifier = Modifier
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(topEnd = 100.dp, bottomEnd = 100.dp))
                            .clickable { proViewModel.onIntent(ProIntent.ShowVip) }
                            .padding(start = 8.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.WorkspacePremium,
                            contentDescription = "PRO",
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (profileState.isPaid) "PRO" else "GET",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFFFFB300)
                        )
                    }
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

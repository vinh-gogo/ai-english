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
import com.aienglish.feature.profile.ui.screen.ProfileSheet
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
import com.aienglish.feature.pro.ui.screen.VipSheet
import com.aienglish.feature.pro.ui.viewmodel.ProViewModel
import com.aienglish.feature.pro.ui.intent.ProIntent
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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when {
                            state.feedback != null -> "Kết quả"
                            state.lesson != null -> "Luyện tập"
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

                    Spacer(Modifier.width(6.dp))

                    // VIP / PRO Badge
                    Surface(
                        onClick = { proViewModel.onIntent(ProIntent.ShowVip) },
                        modifier = Modifier.align(Alignment.CenterVertically),
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFFB300).copy(0.12f),
                        border = BorderStroke(1.dp, Color(0xFFFFB300))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.WorkspacePremium,
                                contentDescription = "PRO",
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = if (profileState.isPaid) "PRO" else "GET",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFFFFB300)
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
            // VIP sheet overlay
            if (proState.showVip) {
                VipSheet(
                    proViewModel = proViewModel
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

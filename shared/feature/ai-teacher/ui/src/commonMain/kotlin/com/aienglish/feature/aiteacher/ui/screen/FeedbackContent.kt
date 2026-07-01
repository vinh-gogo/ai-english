package com.aienglish.feature.aiteacher.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aienglish.core.designsystem.theme.AppColors
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherUiState
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import com.aienglish.feature.aiteacher.ui.viewmodel.LevelType
import com.aienglish.feature.aiteacher.ui.screen.components.SentenceReviewCard
import com.aienglish.feature.aiteacher.ui.screen.components.feedback.FeedbackHeaderChips
import com.aienglish.feature.aiteacher.ui.screen.components.feedback.FeedbackActionButtons
import com.aienglish.feature.aiteacher.ui.screen.components.feedback.FeedbackReviewCard
import com.aienglish.feature.aiteacher.ui.viewmodel.getDisplayLevel

@Composable
fun FeedbackContent(
    state: TeacherUiState,
    viewModel: TeacherViewModel,
    colors: AppColors
) {
    val feedback = state.feedback ?: return
    val lesson = state.lesson

    val levelSystemAndValue = remember(state.levelType, state) {
        getDisplayLevel(state)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(78.dp)) // 18dp top pad + 48dp header + 12dp bottom gap = 78dp

            Text(
                text = "Kết quả của bạn",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colors.mainText
            )
            
            Spacer(Modifier.height(10.dp))

            // Subtitle Tags (Topic & Level Chips)
            FeedbackHeaderChips(
                selectedTopic = state.selectedTopic,
                levelSystem = levelSystemAndValue.first,
                levelValue = levelSystemAndValue.second,
                colors = colors
            )

            Spacer(Modifier.height(24.dp))

            // Review card
            FeedbackReviewCard(review = feedback.review, colors = colors)

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
            Spacer(Modifier.navigationBarsPadding())
        }

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

        // Reset and Retry buttons
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp)
                .navigationBarsPadding()
                .padding(bottom = 14.dp)
        ) {
            FeedbackActionButtons(
                onRetryClick = { viewModel.onRetryCurrentLesson() },
                onResetClick = { viewModel.onReset() },
                colors = colors
            )
        }
    }

    if (state.showSavedNotesBottomSheet) {
        val explainingIdx = state.explainingSentenceIndex ?: 0
        NotesBottomSheet(
            notes = state.savedNotes,
            colors = colors,
            sentenceIndex = explainingIdx,
            onDismiss = { viewModel.closeSavedNotes() },
            onDeleteNote = { id, index -> viewModel.deleteQuestionNote(id, index) },
            onUpdateNote = { id, index, text -> viewModel.updateQuestionNote(id, index, text) }
        )
    }
}

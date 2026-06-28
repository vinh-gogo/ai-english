package com.aienglish.feature.history.domain.repository

import com.aienglish.feature.history.domain.model.LessonHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun startLesson(topic: String, level: String): Long
    suspend fun completeLesson(
        id: Long,
        score: Int,
        lessonJson: String?,
        userAnswersJson: String?,
        explanationsJson: String?,
        feedbackJson: String?
    )
    fun getAllHistory(): Flow<List<LessonHistory>>
    suspend fun clearHistory()
    suspend fun updateLessonProgress(
        id: Long,
        lessonJson: String?,
        userAnswersJson: String?,
        explanationsJson: String?
    )
}

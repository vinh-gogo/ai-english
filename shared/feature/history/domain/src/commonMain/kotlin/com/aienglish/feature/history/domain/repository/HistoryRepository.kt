package com.aienglish.feature.history.domain.repository

import com.aienglish.feature.history.domain.model.LessonHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun startLesson(topic: String, level: String): Long
    suspend fun completeLesson(
        id: Long,
        score: Int,
        lessonData: String?,
        userAnswersData: String?,
        explanationsData: String?,
        feedbackData: String?
    )
    fun getAllHistory(): Flow<List<LessonHistory>>
    suspend fun clearHistory(ids: List<Long>)
    suspend fun updateLessonProgress(
        id: Long,
        lessonData: String?,
        userAnswersData: String?,
        explanationsData: String?
    )
}

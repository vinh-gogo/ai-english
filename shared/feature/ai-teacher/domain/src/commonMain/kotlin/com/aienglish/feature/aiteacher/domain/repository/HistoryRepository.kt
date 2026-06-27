package com.aienglish.feature.aiteacher.domain.repository

import com.aienglish.feature.aiteacher.domain.model.LessonHistory
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
}

package com.aienglish.feature.history.domain.repository

import com.aienglish.feature.history.domain.model.LessonHistory
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    /** 3NF: dùng systemCode + topicId (INTEGER) + levelCode thay vì topic String + level String */
    suspend fun startLesson(systemCode: String, topicId: Long, levelCode: String): Long
    suspend fun completeLesson(id: Long)
    fun getAllHistory(): Flow<List<LessonHistory>>
    suspend fun clearHistory(ids: List<Long>)
    suspend fun incrementAttempt(id: Long)
    /** 3NF: update tên custom topic trong learning_topics (không còn update lesson_history.topic) */
    suspend fun updateCustomTopicName(topicId: Long, newName: String)
}

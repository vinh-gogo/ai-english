package com.aienglish.feature.aiteacher.domain.repository

import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.TeacherLessonHistory

interface TeacherHistoryRepository {
    /** 3NF: dùng systemCode + topicId (INTEGER) + levelCode cho predefined topics */
    suspend fun startLesson(systemCode: String, topicId: Long, levelCode: String): Long
    /**
     * 3NF: Tạo custom topic trong learning_topics (nếu chưa có) rồi start lesson.
     * Custom topics được lưu với system_code='custom'.
     */
    suspend fun ensureCustomTopicAndStart(topicName: String, levelCode: String): Long
    suspend fun resetLessonProgress(id: Long)

    suspend fun completeLesson(
        id: Long,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>,
        feedback: EnglishFeedback
    )

    suspend fun updateLessonProgress(
        id: Long,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>
    )

    suspend fun saveQuestionNote(historyId: Long, sentenceIndex: Int, note: String)
    suspend fun getQuestionNotes(historyId: Long, sentenceIndex: Int): List<com.aienglish.feature.aiteacher.domain.model.QuestionNote>
    suspend fun updateQuestionNote(noteId: String, newNote: String)
    suspend fun deleteQuestionNote(noteId: String)
    
    /** Load full lesson detail from structured DB tables (no JSON parsing). */
    suspend fun loadHistory(historyId: Long): TeacherLessonHistory?
}

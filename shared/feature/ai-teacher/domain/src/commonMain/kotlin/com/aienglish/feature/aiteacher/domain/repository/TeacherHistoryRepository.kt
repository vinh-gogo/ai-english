package com.aienglish.feature.aiteacher.domain.repository

import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.TeacherLessonHistory

interface TeacherHistoryRepository {
    suspend fun startLesson(topic: String, level: String): Long
    
    suspend fun completeLesson(
        id: Long,
        score: Int,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>,
        feedback: EnglishFeedback
    )
    
    suspend fun updateLessonProgress(
        id: Long,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>,
        sentenceScores: Map<Int, Int>
    )

    fun parseHistory(
        id: Long,
        topic: String,
        level: String,
        score: Int?,
        lessonJson: String?,
        userAnswersJson: String?,
        explanationsJson: String?,
        feedbackJson: String?
    ): TeacherLessonHistory
}

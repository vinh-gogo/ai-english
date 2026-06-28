package com.aienglish.feature.aiteacher.data.repository

import com.aienglish.feature.aiteacher.data.model.EnglishFeedbackDto
import com.aienglish.feature.aiteacher.data.model.EnglishLessonDto
import com.aienglish.feature.aiteacher.data.model.toDto
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.TeacherLessonHistory
import com.aienglish.feature.aiteacher.domain.repository.TeacherHistoryRepository
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TeacherHistoryRepositoryImpl(
    private val historyRepository: HistoryRepository
) : TeacherHistoryRepository {

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    override suspend fun startLesson(topic: String, level: String): Long {
        return historyRepository.startLesson(topic, level)
    }

    override suspend fun completeLesson(
        id: Long,
        score: Int,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>,
        feedback: EnglishFeedback
    ) {
        val lessonJson = json.encodeToString(lesson.toDto())
        val userAnswersJson = json.encodeToString(userAnswers)
        val explanationsJson = json.encodeToString(explanations)
        val feedbackJson = json.encodeToString(feedback.toDto())
        historyRepository.completeLesson(
            id = id,
            score = score,
            lessonJson = lessonJson,
            userAnswersJson = userAnswersJson,
            explanationsJson = explanationsJson,
            feedbackJson = feedbackJson
        )
    }

    override suspend fun updateLessonProgress(
        id: Long,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>
    ) {
        val lessonJson = json.encodeToString(lesson.toDto())
        val userAnswersJson = json.encodeToString(userAnswers)
        val explanationsJson = json.encodeToString(explanations)
        historyRepository.updateLessonProgress(
            id = id,
            lessonJson = lessonJson,
            userAnswersJson = userAnswersJson,
            explanationsJson = explanationsJson
        )
    }

    override fun parseHistory(
        id: Long,
        topic: String,
        level: String,
        score: Int?,
        lessonJson: String?,
        userAnswersJson: String?,
        explanationsJson: String?,
        feedbackJson: String?
    ): TeacherLessonHistory {
        val lesson = lessonJson?.let {
            try {
                json.decodeFromString<EnglishLessonDto>(it).toDomain()
            } catch (e: Exception) {
                null
            }
        }
        val userAnswers = userAnswersJson?.let {
            try {
                json.decodeFromString<Map<Int, String>>(it)
            } catch (e: Exception) {
                emptyMap()
            }
        } ?: emptyMap()
        
        val explanations = explanationsJson?.let {
            try {
                json.decodeFromString<Map<Int, String>>(it)
            } catch (e: Exception) {
                emptyMap()
            }
        } ?: emptyMap()
        
        val feedback = feedbackJson?.let {
            try {
                json.decodeFromString<EnglishFeedbackDto>(it).toDomain()
            } catch (e: Exception) {
                null
            }
        }

        return TeacherLessonHistory(
            id = id,
            topic = topic,
            level = level,
            score = score,
            lesson = lesson,
            userAnswers = userAnswers,
            explanations = explanations,
            feedback = feedback
        )
    }
}

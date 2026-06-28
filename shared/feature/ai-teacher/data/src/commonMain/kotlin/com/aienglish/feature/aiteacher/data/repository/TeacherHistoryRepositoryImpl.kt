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

@kotlinx.serialization.Serializable
private data class ExplanationProgressDto(
    val score: Int,
    val explanation: String
)

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
        
        val progressMap = explanations.mapValues { entry ->
            val index = entry.key
            val sentenceScore = feedback.sentenceScores.getOrNull(index) ?: 0
            ExplanationProgressDto(
                score = sentenceScore,
                explanation = entry.value
            )
        }
        val explanationsJson = json.encodeToString(progressMap)
        
        val feedbackJson = json.encodeToString(feedback.toDto())
        historyRepository.completeLesson(
            id = id,
            score = score,
            lessonData = lessonJson,
            userAnswersData = userAnswersJson,
            explanationsData = explanationsJson,
            feedbackData = feedbackJson
        )
    }

    override suspend fun updateLessonProgress(
        id: Long,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>,
        sentenceScores: Map<Int, Int>
    ) {
        val lessonJson = json.encodeToString(lesson.toDto())
        val userAnswersJson = json.encodeToString(userAnswers)
        
        val progressMap = explanations.mapValues { entry ->
            ExplanationProgressDto(
                score = sentenceScores[entry.key] ?: 0,
                explanation = entry.value
            )
        }
        val explanationsJson = json.encodeToString(progressMap)
        
        historyRepository.updateLessonProgress(
            id = id,
            lessonData = lessonJson,
            userAnswersData = userAnswersJson,
            explanationsData = explanationsJson
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
        
        var sentenceScoresMap = emptyMap<Int, Int>()
        val explanations = explanationsJson?.let {
            try {
                val progressMap = json.decodeFromString<Map<Int, ExplanationProgressDto>>(it)
                sentenceScoresMap = progressMap.mapValues { entry -> entry.value.score }
                progressMap.mapValues { entry -> entry.value.explanation }
            } catch (e: Exception) {
                try {
                    json.decodeFromString<Map<Int, String>>(it)
                } catch (e2: Exception) {
                    emptyMap()
                }
            }
        } ?: emptyMap()
        
        val feedback = feedbackJson?.let {
            try {
                json.decodeFromString<EnglishFeedbackDto>(it).toDomain()
            } catch (e: Exception) {
                null
            }
        }

        val finalSentenceScores = if (feedback != null && feedback.sentenceScores.isNotEmpty()) {
            feedback.sentenceScores.mapIndexed { index, sc -> index to sc }.toMap()
        } else {
            sentenceScoresMap
        }

        return TeacherLessonHistory(
            id = id,
            topic = topic,
            level = level,
            score = score,
            lesson = lesson,
            userAnswers = userAnswers,
            explanations = explanations,
            sentenceScores = finalSentenceScores,
            feedback = feedback
        )
    }
}

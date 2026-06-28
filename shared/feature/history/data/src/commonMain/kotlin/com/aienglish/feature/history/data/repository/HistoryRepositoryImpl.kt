package com.aienglish.feature.history.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.history.domain.model.HistoryStatus
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime

class HistoryRepositoryImpl(
    private val database: AIEnglishDatabase,
    private val dispatchers: AppDispatchers
) : HistoryRepository {

    private val queries get() = database.lessonHistoryQueries

    override suspend fun startLesson(topic: String, level: String): Long =
        withContext(dispatchers.io) {
            val now = Clock.System.now().toEpochMilliseconds()
            queries.insertLesson(topic, level, now)
            queries.getLastInsertId().executeAsOne()
        }

    override suspend fun completeLesson(
        id: Long,
        score: Int,
        lessonData: String?,
        userAnswersData: String?,
        explanationsData: String?,
        feedbackData: String?
    ) {
        withContext(dispatchers.io) {
            val now = Clock.System.now().toEpochMilliseconds()
            queries.updateLessonCompleted(
                score = score.toLong(),
                completed_at = now,
                lesson_json = lessonData,
                user_answers_json = userAnswersData,
                explanations_json = explanationsData,
                feedback_json = feedbackData,
                id = id
            )
        }
    }

    override fun getAllHistory(): Flow<List<LessonHistory>> =
        queries.getAllHistory()
            .asFlow()
            .mapToList(dispatchers.io)
            .map { rows ->
                rows.map { row ->
                    LessonHistory(
                        id = row.id,
                        topic = row.topic,
                        level = row.level,
                        score = row.score?.toInt(),
                        status = HistoryStatus.from(row.status),
                        createdAt = row.created_at,
                        completedAt = row.completed_at,
                        lessonData = row.lesson_json,
                        userAnswersData = row.user_answers_json,
                        explanationsData = row.explanations_json,
                        feedbackData = row.feedback_json
                    )
                }
            }

    override suspend fun clearHistory() {
        withContext(dispatchers.io) {
            queries.clearHistory()
        }
    }

    override suspend fun updateLessonProgress(
        id: Long,
        lessonData: String?,
        userAnswersData: String?,
        explanationsData: String?
    ) {
        withContext(dispatchers.io) {
            queries.updateLessonProgress(
                lesson_json = lessonData,
                user_answers_json = userAnswersData,
                explanations_json = explanationsData,
                id = id
            )
        }
    }
}

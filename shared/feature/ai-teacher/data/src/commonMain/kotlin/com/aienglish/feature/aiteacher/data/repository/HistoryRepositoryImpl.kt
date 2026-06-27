package com.aienglish.feature.aiteacher.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.aiteacher.domain.model.HistoryStatus
import com.aienglish.feature.aiteacher.domain.model.LessonHistory
import com.aienglish.feature.aiteacher.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

class HistoryRepositoryImpl(
    private val database: AIEnglishDatabase,
    private val dispatchers: AppDispatchers
) : HistoryRepository {

    private val queries get() = database.lessonHistoryQueries

    @OptIn(ExperimentalTime::class)
    private fun nowString(): String {
        val instant = kotlin.time.Clock.System.now()
        return instant.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
    }

    override suspend fun startLesson(topic: String, level: String): Long =
        withContext(dispatchers.io) {
            queries.insertLesson(topic, level, nowString())
            queries.getLastInsertId().executeAsOne()
        }

    override suspend fun completeLesson(id: Long, score: Int) {
        withContext(dispatchers.io) {
            queries.updateLessonCompleted(score.toLong(), nowString(), id)
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
                        completedAt = row.completed_at
                    )
                }
            }

    override suspend fun clearHistory() {
        withContext(dispatchers.io) {
            queries.clearHistory()
        }
    }
}

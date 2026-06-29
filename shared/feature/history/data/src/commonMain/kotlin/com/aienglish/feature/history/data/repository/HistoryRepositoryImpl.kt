package com.aienglish.feature.history.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.history.domain.model.HistoryStatus
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class HistoryRepositoryImpl(
    private val database: AIEnglishDatabase,
    private val prefs: AppPreferences,
    private val dispatchers: AppDispatchers
) : HistoryRepository {

    private val queries get() = database.lessonHistoryQueries
    private val migrationMutex = Mutex()
    private var isMigrated = false

    private fun getCurrentUserId(): String {
        val email = prefs.getUserEmail()
        return if (prefs.isSignedIn() && email != "guest@aienglish.com") {
            email ?: "0000000000000000"
        } else {
            "0000000000000000"
        }
    }

    private suspend fun checkAndMigrateGuestData() {
        if (isMigrated) return
        migrationMutex.withLock {
            if (isMigrated) return
            withContext(dispatchers.io) {
                try {
                    val now = Clock.System.now().toEpochMilliseconds()
                    database.transaction {
                        database.userSchemaQueries.updateUserEmail(
                            email = "guest_old_${now}@local",
                            user_id = "guest"
                        )
                        database.userSchemaQueries.ensureUserExists(
                            user_id = "0000000000000000",
                            display_name = "Guest",
                            email = "guest@local",
                            created_at = now,
                            updated_at = now
                        )
                        database.lessonHistoryQueries.migrateGuestHistory()
                        database.lessonHistoryQueries.migrateGuestEmailHistory()
                        database.userSchemaQueries.deleteUserById("guest")
                        database.userSchemaQueries.deleteUserById("guest@aienglish.com")
                    }
                    isMigrated = true
                } catch (e: Exception) {
                    println("DATABASE MIGRATION ERROR: ${e.message}")
                    isMigrated = true // prevent infinite retry
                }
            }
        }
    }

    override suspend fun startLesson(topic: String, level: String): Long =
        withContext(dispatchers.io) {
            checkAndMigrateGuestData()
            val now = Clock.System.now().toEpochMilliseconds()
            val userId = getCurrentUserId()
            database.userSchemaQueries.ensureUserExists(
                user_id = userId,
                display_name = if (userId == "0000000000000000") "Guest" else userId.substringBefore("@"),
                email = if (userId == "0000000000000000") "guest@local" else userId,
                created_at = now,
                updated_at = now
            )
            queries.insertLesson(userId, topic, level, now)
            queries.getLastInsertId().executeAsOne()
        }

    override suspend fun completeLesson(id: Long, score: Int) {
        withContext(dispatchers.io) {
            val now = Clock.System.now().toEpochMilliseconds()
            queries.updateLessonCompleted(
                score = score.toLong(),
                completed_at = now,
                id = id
            )
        }
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override fun getAllHistory(): Flow<List<LessonHistory>> =
        combine(prefs.isSignedInFlow, prefs.userEmailFlow) { signedIn, email ->
            if (signedIn && email != "guest@aienglish.com") {
                email ?: "0000000000000000"
            } else {
                "0000000000000000"
            }
        }.flatMapLatest { userId ->
            checkAndMigrateGuestData()
            queries.getAllHistoryWithCounts(userId)
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
                            completedCount = row.completed_count.toInt(),
                            totalCount = row.total_count.toInt()
                        )
                    }
                }
        }

    override suspend fun clearHistory(ids: List<Long>) {
        withContext(dispatchers.io) {
            queries.deleteHistoryByIds(ids)
        }
    }
}

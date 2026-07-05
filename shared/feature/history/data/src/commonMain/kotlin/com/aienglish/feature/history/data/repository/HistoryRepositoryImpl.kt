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

    override suspend fun startLesson(systemCode: String, topicId: Long, levelCode: String): Long =
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
            // 3NF: truyền systemCode, topicId (INTEGER), levelCode
            queries.insertLesson(userId, systemCode, topicId, levelCode, now, now)
            queries.getLastInsertId().executeAsOne()
        }

    override suspend fun completeLesson(id: Long) {
        withContext(dispatchers.io) {
            val now = Clock.System.now().toEpochMilliseconds()
            queries.updateLessonCompleted(
                completed_at = now,
                updated_at = now,
                id = id
            )
        }
    }

    /**
     * Reconstruct formatted level string từ systemCode + levelCode.
     * Mirror logic của LevelType.uiPrefix để maintain backward compat với UI.
     */
    private fun reconstructLevel(systemCode: String, levelCode: String): String {
        if (systemCode == "custom") return levelCode
        val prefix = when (systemCode) {
            "TOEIC"   -> "TOEIC "
            "DELE"    -> "DELE "
            "SIELE"   -> "SIELE "
            "HINDI"   -> "Hindi "
            "DELF"    -> "DELF "
            "DALF"    -> "DALF "
            "GOETHE"  -> "Goethe "
            "TESTDAF" -> "TestDaF "
            else      -> ""  // CEFR, JLPT, HSK, TOPIK, VIET: no prefix
        }
        return prefix + levelCode
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
                            topic = row.topic,       // từ JOIN với learning_topics
                            systemCode = row.system_code,
                            topicId = row.topic_id,  // INTEGER NOT NULL
                            levelCode = row.level_code,
                            level = reconstructLevel(row.system_code, row.level_code),
                            status = HistoryStatus.from(row.status),
                            createdAt = row.created_at,
                            completedAt = row.completed_at,
                            completedCount = row.completed_count.toInt(),
                            totalCount = row.total_count.toInt(),
                            attemptCount = row.attempt_count.toInt(),
                            updatedAt = row.updated_at
                        )
                    }
                }
        }

    override suspend fun clearHistory(ids: List<Long>) {
        withContext(dispatchers.io) {
            queries.deleteHistoryByIds(ids)
        }
    }

    override suspend fun incrementAttempt(id: Long) {
        withContext(dispatchers.io) {
            val now = Clock.System.now().toEpochMilliseconds()
            queries.incrementLessonAttempt(
                updated_at = now,
                id = id
            )
        }
    }

    override suspend fun updateCustomTopicName(topicId: Long, newName: String) {
        withContext(dispatchers.io) {
            // 3NF: update tên custom topic trong learning_topics, không còn update lesson_history.topic
            queries.updateCustomTopicName(
                name = newName,
                topic_id = topicId
            )
        }
    }
}

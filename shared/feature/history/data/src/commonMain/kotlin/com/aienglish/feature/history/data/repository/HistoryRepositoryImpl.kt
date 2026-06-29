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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.Clock
import kotlin.time.ExperimentalTime
import kotlinx.serialization.json.*

private val jsonParser = Json { ignoreUnknownKeys = true }

class HistoryRepositoryImpl(
    private val database: AIEnglishDatabase,
    private val prefs: AppPreferences,
    private val dispatchers: AppDispatchers
) : HistoryRepository {

    private val queries get() = database.lessonHistoryQueries

    private fun getCurrentUserId(): String {
        val email = prefs.getUserEmail()
        return if (prefs.isSignedIn() && email != "guest@aienglish.com") {
            email ?: "0000000000000000"
        } else {
            "0000000000000000"
        }
    }

    override suspend fun startLesson(topic: String, level: String): Long =
        withContext(dispatchers.io) {
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
            saveQuestionsToDb(id, lessonData, userAnswersData, explanationsData)
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
            queries.getAllHistoryWithCounts(userId)
                .asFlow()
                .mapToList(dispatchers.io)
                .map { rows ->
                    rows.map { row ->
                        var total = row.total_count.toInt()
                        var completed = row.completed_count.toInt()
                        
                        // Fallback to JSON parsing for legacy data
                        if (total == 0) {
                            completed = row.explanations_json?.let { data ->
                                try {
                                    jsonParser.parseToJsonElement(data).jsonObject.size
                                } catch (e: Exception) {
                                    0
                                }
                            } ?: 0
                            total = row.lesson_json?.let { data ->
                                try {
                                    jsonParser.parseToJsonElement(data).jsonObject["sentences"]?.jsonArray?.size
                                } catch (e: Exception) {
                                    null
                                }
                            } ?: 10
                        }
                        
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
                            feedbackData = row.feedback_json,
                            completedCount = completed,
                            totalCount = total
                        )
                    }
                }
        }

    override suspend fun clearHistory(ids: List<Long>) {
        withContext(dispatchers.io) {
            queries.deleteHistoryByIds(ids)
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
            saveQuestionsToDb(id, lessonData, userAnswersData, explanationsData)
        }
    }

    private fun saveQuestionsToDb(
        historyId: Long,
        lessonJson: String?,
        userAnswersJson: String?,
        explanationsJson: String?
    ) {
        if (lessonJson.isNullOrEmpty()) return
        try {
            val lessonObj = jsonParser.parseToJsonElement(lessonJson).jsonObject
            val sentences = lessonObj["sentences"]?.jsonArray ?: return
            
            val userAnswersMap = userAnswersJson?.let {
                try {
                    jsonParser.parseToJsonElement(it).jsonObject
                } catch (e: Exception) {
                    null
                }
            }
            
            val explanationsMap = explanationsJson?.let {
                try {
                    jsonParser.parseToJsonElement(it).jsonObject
                } catch (e: Exception) {
                    null
                }
            }
            
            sentences.forEachIndexed { index, sentenceElement ->
                val sentenceObj = sentenceElement.jsonObject
                val vietnamese = sentenceObj["vietnamese"]?.jsonPrimitive?.content ?: ""
                val english = sentenceObj["english"]?.jsonPrimitive?.content ?: ""
                val explanation = sentenceObj["explanation"]?.jsonPrimitive?.content ?: ""
                
                val indexStr = index.toString()
                val userAnswer = userAnswersMap?.get(indexStr)?.jsonPrimitive?.content
                
                var aiScore: Int? = null
                var aiExplanation: String? = null
                
                explanationsMap?.get(indexStr)?.let { expElement ->
                    if (expElement is JsonObject) {
                        aiScore = expElement["score"]?.jsonPrimitive?.intOrNull
                        aiExplanation = expElement["explanation"]?.jsonPrimitive?.content
                    } else if (expElement is JsonPrimitive) {
                        aiExplanation = expElement.content
                    }
                }
                
                queries.insertQuestion(
                    history_id = historyId,
                    sentence_index = index.toLong(),
                    vietnamese = vietnamese,
                    english = english,
                    explanation = explanation,
                    user_answer = userAnswer,
                    ai_score = aiScore?.toLong(),
                    ai_explanation = aiExplanation
                )
                
                val questionId = queries.getQuestionId(historyId, index.toLong()).executeAsOne()
                
                queries.deleteTokensForQuestion(questionId)
                
                sentenceObj["tokens"]?.jsonArray?.forEach { tokenElement ->
                    val tokenObj = tokenElement.jsonObject
                    val word = tokenObj["word"]?.jsonPrimitive?.content ?: ""
                    val phonetic = tokenObj["phonetic"]?.jsonPrimitive?.content ?: ""
                    val partOfSpeech = tokenObj["partOfSpeech"]?.jsonPrimitive?.content ?: ""
                    queries.insertToken(
                        question_id = questionId,
                        word = word,
                        phonetic = phonetic,
                        part_of_speech = partOfSpeech
                    )
                }
            }
        } catch (e: Exception) {
            // Prevent crash
        }
    }
}

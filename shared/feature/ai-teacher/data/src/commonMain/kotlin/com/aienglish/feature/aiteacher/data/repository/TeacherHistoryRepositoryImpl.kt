package com.aienglish.feature.aiteacher.data.repository
import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.EnglishSentence
import com.aienglish.feature.aiteacher.domain.model.EnglishToken
import com.aienglish.feature.aiteacher.domain.model.TeacherLessonHistory
import com.aienglish.feature.aiteacher.domain.repository.TeacherHistoryRepository
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class TeacherHistoryRepositoryImpl(
    private val historyRepository: HistoryRepository,
    private val database: AIEnglishDatabase,
    private val dispatchers: AppDispatchers
) : TeacherHistoryRepository {

    private val queries get() = database.lessonHistoryQueries

    override suspend fun startLesson(systemCode: String, topicId: Long, levelCode: String): Long =
        historyRepository.startLesson(systemCode, topicId, levelCode)

    override suspend fun ensureCustomTopicAndStart(topicName: String, levelCode: String): Long =
        withContext(dispatchers.io) {
            val now = Clock.System.now().toEpochMilliseconds()
            // Ensure custom system/level entries exist (idempotent)
            database.userSchemaQueries.insertCustomSystemIfNeeded()
            database.userSchemaQueries.insertCustomLevelIfNeeded()
            // Insert custom topic nếu chưa tồn tại (topic_id = timestamp làm unique ID)
            database.userSchemaQueries.insertCustomTopic(now, topicName, now)
            // Lấy topic_id vừa insert hoặc đã tồn tại
            val topicId = database.userSchemaQueries
                .getOrCreateCustomTopicId(topicName)
                .executeAsOneOrNull() ?: now
            historyRepository.startLesson("custom", topicId, levelCode)
        }

    override suspend fun resetLessonProgress(id: Long) {
        withContext(dispatchers.io) {
            queries.resetLessonProgress(id)
            queries.deleteLessonFeedback(id)
            historyRepository.incrementAttempt(id)
        }
    }

    override suspend fun completeLesson(
        id: Long,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>,
        feedback: EnglishFeedback
    ) {
        // 1. Save all structured question data (best-effort — không block attempt_count)
        try {
            saveQuestionsToDb(id, lesson, userAnswers, explanations)
        } catch (e: Exception) {
            println("[completeLesson] saveQuestionsToDb failed for history $id: ${e.message}")
        }

        // 2. Save feedback (best-effort)
        try {
            withContext(dispatchers.io) {
                queries.insertOrReplaceFeedback(
                    history_id = id,
                    review = feedback.review,
                    score = feedback.score.toLong()
                )
            }
        } catch (e: Exception) {
            println("[completeLesson] insertOrReplaceFeedback failed for history $id: ${e.message}")
        }

        // 3. LUÔN tăng attempt_count — đây là nguồn sự thật duy nhất của số lần hoàn thành
        historyRepository.completeLesson(id = id)
    }

    override suspend fun updateLessonProgress(
        id: Long,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>
    ) {
        saveQuestionsToDb(id, lesson, userAnswers, explanations)
    }

    override suspend fun loadHistory(historyId: Long): TeacherLessonHistory? = withContext(dispatchers.io) {
        return@withContext try {
            val questionRows = queries.getQuestionsForHistory(historyId).executeAsList()
            if (questionRows.isEmpty()) return@withContext null

            val sentences = questionRows.map { row ->
                val tokens = queries.getTokensForStaticQuestion(row.static_question_id).executeAsList().map { t ->
                    EnglishToken(
                        word = t.word,
                        phonetic = t.phonetic ?: "",
                        partOfSpeech = t.part_of_speech ?: ""
                    )
                }
                val alternativesFromDb = queries.getAlternativesForStaticQuestion(row.static_question_id).executeAsList()
                val alternatives = alternativesFromDb

                EnglishSentence(
                    vietnamese = row.vietnamese,
                    english = row.english,
                    explanation = row.explanation ?: "",
                    alternatives = alternatives,
                    tokens = tokens
                )
            }

            val userAnswers = questionRows
                .mapIndexedNotNull { index, row ->
                    row.user_answer?.let { index to it }
                }
                .toMap()

            val explanations = questionRows
                .mapIndexedNotNull { index, row ->
                    row.ai_explanation?.let { index to it }
                }
                .toMap()

            val feedbackRow = queries.getFeedbackForHistory(historyId).executeAsOneOrNull()
            val feedback = feedbackRow?.let {
                EnglishFeedback(
                    review = it.review,
                    score = it.score.toInt()
                )
            }

            TeacherLessonHistory(
                id = historyId,
                topic = "",
                level = "",
                lesson = EnglishLesson(sentences = sentences),
                userAnswers = userAnswers,
                explanations = explanations,
                feedback = feedback
            )
        } catch (e: Exception) {
            println("loadHistory error: ${e.message}")
            null
        }
    }

    @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
    private suspend fun saveQuestionsToDb(
        historyId: Long,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>
    ) = withContext(dispatchers.io) {
        // 3NF: getHistoryTopicId giờ trả về (system_code, topic_id: Long, level_code)
        // topic_questions.topic_id vẫn là TEXT, nên convert Long → String
        val historyInfo = queries.getHistoryTopicId(historyId).executeAsOneOrNull()
        val topicId = historyInfo?.topic_id?.toString() ?: ""

        lesson.sentences.forEachIndexed { index, sentence ->
            val staticQuestion = queries.getStaticQuestionsForTopic(topicId).executeAsList()
                .firstOrNull { it.sentence_index == index.toLong() }
            
            val staticQuestionId = if (staticQuestion != null) {
                staticQuestion.id
            } else {
                val newGuid = kotlin.uuid.Uuid.random().toString()
                val promptKey = "topic_question_prompt_${newGuid}"
                val explanationKey = "topic_question_explanation_${newGuid}"
                
                database.userSchemaQueries.insertTranslationKey(promptKey, "curriculum")
                database.userSchemaQueries.insertTranslation(promptKey, "vi", sentence.vietnamese)
                
                if (sentence.explanation.isNotEmpty()) {
                    database.userSchemaQueries.insertTranslationKey(explanationKey, "curriculum")
                    database.userSchemaQueries.insertTranslation(explanationKey, "vi", sentence.explanation)
                }

                queries.insertTopicQuestion(
                    id = newGuid,
                    topic_id = topicId,
                    sentence_index = index.toLong(),
                    native_prompt_key = promptKey,
                    english = sentence.english,
                    explanation_key = if (sentence.explanation.isNotEmpty()) explanationKey else null
                )

                sentence.alternatives.forEach { alternative ->
                    queries.insertQuestionAlternative(newGuid, alternative)
                }

                sentence.tokens.forEach { token ->
                    queries.insertQuestionToken(
                        question_id = newGuid,
                        word = token.word,
                        phonetic = token.phonetic.takeIf { it.isNotEmpty() },
                        part_of_speech = token.partOfSpeech.takeIf { it.isNotEmpty() }
                    )
                }
                newGuid
            }

            val attemptId = queries.getQuestionId(historyId, index.toLong()).executeAsOneOrNull()
            if (attemptId != null) {
                queries.updateQuestionAttempt(
                    user_answer = userAnswers[index],
                    ai_explanation = explanations[index],
                    id = attemptId
                )
            } else {
                queries.insertQuestion(
                    history_id = historyId,
                    question_id = staticQuestionId,
                    user_answer = userAnswers[index],
                    ai_explanation = explanations[index]
                )
            }
        }
    }

    @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
    override suspend fun saveQuestionNote(historyId: Long, sentenceIndex: Int, note: String) = withContext(dispatchers.io) {
        val questionId = queries.getQuestionId(historyId, sentenceIndex.toLong()).executeAsOneOrNull() ?: return@withContext
        
        val existingNotes = queries.getQuestionNotes(questionId).executeAsList()
        if (existingNotes.any { it.note == note }) {
            return@withContext
        }

        val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
        val guid = kotlin.uuid.Uuid.random().toString()
        queries.insertQuestionNote(
            id = guid,
            question_id = questionId,
            note = note,
            created_at = now
        )
    }

    override suspend fun getQuestionNotes(historyId: Long, sentenceIndex: Int): List<com.aienglish.feature.aiteacher.domain.model.QuestionNote> = withContext(dispatchers.io) {
        val questionId = queries.getQuestionId(historyId, sentenceIndex.toLong()).executeAsOneOrNull() ?: return@withContext emptyList()
        queries.getQuestionNotes(questionId).executeAsList().map { 
            com.aienglish.feature.aiteacher.domain.model.QuestionNote(
                id = it.id,
                note = it.note,
                sentenceIndex = sentenceIndex
            )
        }
    }

    override suspend fun updateQuestionNote(noteId: String, newNote: String) {
        withContext(dispatchers.io) {
            queries.updateQuestionNote(note = newNote, id = noteId)
        }
    }

    override suspend fun deleteQuestionNote(noteId: String) {
        withContext(dispatchers.io) {
            queries.deleteQuestionNote(id = noteId)
        }
    }
}

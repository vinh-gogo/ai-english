package com.aienglish.feature.aiteacher.data.repository

import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.EnglishSentence
import com.aienglish.feature.aiteacher.domain.model.EnglishToken
import com.aienglish.feature.aiteacher.domain.model.TeacherLessonHistory
import com.aienglish.feature.aiteacher.domain.repository.TeacherHistoryRepository
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class TeacherHistoryRepositoryImpl(
    private val historyRepository: HistoryRepository,
    private val database: AIEnglishDatabase
) : TeacherHistoryRepository {

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
    private val queries get() = database.lessonHistoryQueries

    override suspend fun startLesson(topic: String, level: String): Long =
        historyRepository.startLesson(topic, level)

    override suspend fun completeLesson(
        id: Long,
        score: Int,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>,
        feedback: EnglishFeedback
    ) {
        // 1. Save all structured question data
        val sentenceScoresMap = feedback.sentenceScores
            .mapIndexed { index, score -> index to score }
            .toMap()
        saveQuestionsToDb(id, lesson, userAnswers, explanations, sentenceScoresMap)

        // 2. Save feedback
        queries.insertOrReplaceFeedback(
            history_id = id,
            overall_score = score.toLong(),
            review = feedback.review,
            sentence_scores_json = json.encodeToString(feedback.sentenceScores)
        )

        // 3. Mark lesson as completed (status + score only)
        historyRepository.completeLesson(id = id, score = score)
    }

    override suspend fun updateLessonProgress(
        id: Long,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>,
        sentenceScores: Map<Int, Int>
    ) {
        saveQuestionsToDb(id, lesson, userAnswers, explanations, sentenceScores)
    }

    override suspend fun loadHistory(historyId: Long): TeacherLessonHistory? {
        return try {
            val questionRows = queries.getQuestionsForHistory(historyId).executeAsList()
            if (questionRows.isEmpty()) return null

            val sentences = questionRows.map { row ->
                val tokens = queries.getTokensForQuestion(row.id).executeAsList().map { t ->
                    EnglishToken(
                        word = t.word,
                        phonetic = t.phonetic ?: "",
                        partOfSpeech = t.part_of_speech ?: ""
                    )
                }
                val alternatives = row.alternatives_json?.let {
                    try { json.decodeFromString<List<String>>(it) } catch (_: Exception) { emptyList() }
                } ?: emptyList()

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

            val sentenceScores = questionRows
                .mapIndexedNotNull { index, row ->
                    row.ai_score?.let { index to it.toInt() }
                }
                .toMap()

            val feedbackRow = queries.getFeedbackForHistory(historyId).executeAsOneOrNull()
            val feedback = feedbackRow?.let {
                val scores = it.sentence_scores_json?.let { raw ->
                    try { json.decodeFromString<List<Int>>(raw) } catch (_: Exception) { emptyList() }
                } ?: emptyList()
                EnglishFeedback(
                    score = it.overall_score.toInt(),
                    review = it.review,
                    sentenceScores = scores
                )
            }

            TeacherLessonHistory(
                id = historyId,
                topic = "",
                level = "",
                score = feedback?.score,
                lesson = EnglishLesson(sentences = sentences),
                userAnswers = userAnswers,
                explanations = explanations,
                sentenceScores = sentenceScores,
                feedback = feedback
            )
        } catch (e: Exception) {
            println("loadHistory error: ${e.message}")
            null
        }
    }

    private fun saveQuestionsToDb(
        historyId: Long,
        lesson: EnglishLesson,
        userAnswers: Map<Int, String>,
        explanations: Map<Int, String>,
        sentenceScores: Map<Int, Int>
    ) {
        lesson.sentences.forEachIndexed { index, sentence ->
            val alternativesJson = if (sentence.alternatives.isNotEmpty()) {
                json.encodeToString(sentence.alternatives)
            } else null

            queries.insertQuestion(
                history_id = historyId,
                sentence_index = index.toLong(),
                vietnamese = sentence.vietnamese,
                english = sentence.english,
                explanation = sentence.explanation.takeIf { it.isNotEmpty() },
                user_answer = userAnswers[index],
                ai_score = sentenceScores[index]?.toLong(),
                ai_explanation = explanations[index],
                alternatives_json = alternativesJson
            )

            val questionId = queries.getQuestionId(historyId, index.toLong()).executeAsOne()
            queries.deleteTokensForQuestion(questionId)

            sentence.tokens.forEach { token ->
                queries.insertToken(
                    question_id = questionId,
                    word = token.word,
                    phonetic = token.phonetic.takeIf { it.isNotEmpty() },
                    part_of_speech = token.partOfSpeech.takeIf { it.isNotEmpty() }
                )
            }
        }
    }
}

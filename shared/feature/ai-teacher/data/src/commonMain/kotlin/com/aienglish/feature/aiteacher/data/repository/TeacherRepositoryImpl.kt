package com.aienglish.feature.aiteacher.data.repository

import arrow.core.Either
import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.common.result.AppError
import com.aienglish.core.common.result.AppResult
import com.aienglish.core.network.client.GeminiClient
import com.aienglish.core.network.client.GEMINI_MODEL_CHAIN
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.repository.TeacherRepository
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class TeacherRepositoryImpl(
    private val geminiClient: GeminiClient,
    private val dispatchers: AppDispatchers,
    private val prefs: AppPreferences,
    private val fallbackApiKey: String   // from local.properties / BuildConfig
) : TeacherRepository {

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    // Always get the freshest API key — user may update it in Profile without restart
    private fun resolveApiKey(): String {
        val saved = prefs.getApiKey()
        return if (saved.isNotBlank()) saved else fallbackApiKey
    }

    private fun sanitizeJson(raw: String): String {
        return raw.trim()
            .removePrefix("```json").removePrefix("```")
            .removeSuffix("```")
            .trim()
    }

    override suspend fun generateLesson(
        userId: String,
        topic: String,
        level: String
    ): AppResult<EnglishLesson> = withContext(dispatchers.io) {
        try {
            val apiKey = resolveApiKey()
            if (apiKey.isBlank()) {
                return@withContext Either.Left(
                    AppError.Network("Chưa có API Key. Vào Profile → cài Gemini API Key để bắt đầu.")
                )
            }

            val systemPrompt = """
                You are an English AI Teacher - a professional and caring English language instructor.
                Your task is to help Vietnamese learners practice English through daily sentence translation exercises.
                When the user selects a topic, you must create exactly 10 short Vietnamese sentences related to that topic,
                along with their English translation and token breakdown.

                YOU MUST CREATE SENTENCES THAT MATCH EXACTLY THE CEFR LEVEL: $level.
                - A1/A2: Use simple vocabulary, basic present/past tense, short sentences, everyday words.
                - B1: Use intermediate grammar (conditionals, passive voice basics), natural expressions, varied vocabulary.
                - B2: Use advanced grammar structures, idiomatic expressions, diverse vocabulary and phrasal verbs.
                - C1/C2: Use academic vocabulary, complex structures, nuanced expressions, and native-like naturalness.

                YOU MUST RETURN VALID JSON FOLLOWING EXACTLY THIS STRUCTURE:
                {
                  "sentences": [
                    {
                      "vietnamese": "Short Vietnamese sentence",
                      "english": "Corresponding English translation",
                      "tokens": [
                        { "word": "Hello", "phonetic": "/həˈloʊ/", "partOfSpeech": "interjection" }
                      ]
                    }
                  ]
                }

                CRITICAL RULES:
                1. Return ONLY valid JSON. No markdown, no explanation, no preamble outside the JSON.
                2. Vietnamese sentences must be short, natural, and relevant to the chosen topic.
                3. Tokens must break the English sentence into individual words with IPA phonetics and part of speech.
            """.trimIndent()

            val rawResponse = geminiClient.generateContent(
                apiKey = apiKey,
                systemPrompt = systemPrompt,
                userPrompt = "Create 10 CEFR $level practice sentences for topic: $topic",
                model = GEMINI_MODEL_CHAIN.first()
            )

            val cleanedJson = sanitizeJson(rawResponse)
            val lesson = json.decodeFromString<EnglishLesson>(cleanedJson)
            Either.Right(lesson)
        } catch (e: Exception) {
            Either.Left(AppError.Network(friendlyError(e.message)))
        }
    }

    override suspend fun evaluateLesson(
        userId: String,
        topic: String,
        answers: List<Pair<String, String>>,
        sentences: List<String>,
        level: String
    ): AppResult<EnglishFeedback> = withContext(dispatchers.io) {
        try {
            val apiKey = resolveApiKey()
            if (apiKey.isBlank()) {
                return@withContext Either.Left(
                    AppError.Network("Chưa có API Key. Vào Profile → cài Gemini API Key.")
                )
            }

            val reports = answers.zip(sentences).mapIndexed { index, (answerAndTarget, vietnamese) ->
                val (userAnswer, targetEnglish) = answerAndTarget
                """
                Sentence ${index + 1}:
                - Vietnamese original: $vietnamese
                - Target English: $targetEnglish
                - Student's answer: ${userAnswer.ifBlank { "(Skipped / No answer)" }}
                """.trimIndent()
            }.joinToString("\n\n")

            val systemPrompt = """
                You are an English AI Teacher evaluating student translation results.
                The student just completed a 10-sentence translation exercise on topic: $topic.

                YOU MUST EVALUATE STRICTLY ACCORDING TO CEFR LEVEL: $level.
                - A1/A2: Accept simple phrasing, focus on basic grammar correctness. Be encouraging and supportive.
                - B1: Check for proper use of tenses, prepositions, and natural phrasing. Moderate strictness.
                - B2: Require accurate idiomatic expressions, correct use of phrasal verbs, and varied vocabulary.
                - C1/C2: Require near-native fluency, academic register, precise word choice, and natural complex sentences.

                Student's results:
                $reports

                Provide feedback in Vietnamese: praise what was done well, point out common errors, suggest improvements.
                Give a score out of 10.

                RETURN ONLY VALID JSON (no markdown, no extra text):
                {
                  "score": 8,
                  "review": "Detailed feedback in Vietnamese with clear paragraphs (use \\n for line breaks)."
                }
            """.trimIndent()

            val rawResponse = geminiClient.generateContent(
                apiKey = apiKey,
                systemPrompt = systemPrompt,
                userPrompt = "Please evaluate my English translation results.",
                model = GEMINI_MODEL_CHAIN.first()
            )

            val cleanedJson = sanitizeJson(rawResponse)
            val feedback = json.decodeFromString<EnglishFeedback>(cleanedJson)
            Either.Right(feedback)
        } catch (e: Exception) {
            Either.Left(AppError.Network(friendlyError(e.message)))
        }
    }

    override suspend fun explainSentence(
        userId: String,
        vietnamese: String,
        targetEnglish: String,
        userAnswer: String,
        level: String
    ): AppResult<String> = withContext(dispatchers.io) {
        try {
            val apiKey = resolveApiKey()
            if (apiKey.isBlank()) {
                return@withContext Either.Left(
                    AppError.Network("Chưa có API Key. Vào Profile → cài Gemini API Key.")
                )
            }

            val systemPrompt = """
                You are an English AI Teacher - a professional and caring instructor.
                Your task is to explain in detail the grammar, vocabulary, and usage of an English sentence,
                tailored to the student's CEFR target level: $level.

                Sentence information:
                - Original Vietnamese: $vietnamese
                - Target English: $targetEnglish
                - Student's answer: ${userAnswer.ifBlank { "(Student skipped this sentence)" }}

                Explanation requirements (adjusted for CEFR $level):
                1. Evaluate whether the student's answer is grammatically correct and natural.
                2. Compare with the target English, point out strengths or errors (grammar, word order, vocabulary, tone).
                3. If the student answered correctly, praise briefly and explain the grammar structures at $level depth.
                4. If the student skipped, provide a full breakdown of the target sentence, explaining vocabulary and grammar patterns relevant to $level.
                5. Write in Vietnamese, warm and encouraging. Return as Markdown (use ##, bold, lists - no JSON).
            """.trimIndent()

            val rawResponse = geminiClient.generateContent(
                apiKey = apiKey,
                systemPrompt = systemPrompt,
                userPrompt = "Please explain this English sentence in detail.",
                model = GEMINI_MODEL_CHAIN.first()
            )
            Either.Right(rawResponse.trim())
        } catch (e: Exception) {
            Either.Left(AppError.Network(friendlyError(e.message)))
        }
    }

    private fun friendlyError(msg: String?): String {
        if (msg == null) return "Lỗi không xác định"
        return when {
            "API_KEY_INVALID" in msg || "400" in msg && "key" in msg.lowercase() ->
                "API Key không hợp lệ. Vào Profile để cập nhật key mới."
            "QUOTA_EXCEEDED" in msg || "429" in msg ->
                "Vượt quota Gemini miễn phí. Thử lại sau 1 phút hoặc dùng API Key khác."
            "PERMISSION_DENIED" in msg || "403" in msg ->
                "API Key bị từ chối. Kiểm tra lại key trong Profile."
            "503" in msg || "overloaded" in msg.lowercase() ->
                "Gemini đang quá tải. Đã thử tất cả models dự phòng. Thử lại sau ít phút."
            "Empty Gemini response" in msg ->
                "AI không trả về kết quả. Thử lại hoặc đổi chủ đề."
            else -> msg
        }
    }
}

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
import com.aienglish.feature.aiteacher.data.model.EnglishLessonDto
import com.aienglish.feature.aiteacher.data.model.EnglishFeedbackDto
import com.aienglish.feature.aiteacher.data.repository.prompt.PromptProviderFactory
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

            val provider = PromptProviderFactory.getProvider(level)
            val systemPrompt = provider.getGenerateLessonSystemPrompt(level)
            val userPrompt = provider.getGenerateLessonUserPrompt(level, topic)

            val rawResponse = geminiClient.generateContent(
                apiKey = apiKey,
                systemPrompt = systemPrompt,
                userPrompt = userPrompt,
                model = GEMINI_MODEL_CHAIN.first()
            )

            val cleanedJson = sanitizeJson(rawResponse)
            val lesson = json.decodeFromString<EnglishLessonDto>(cleanedJson).toDomain()
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

            val provider = PromptProviderFactory.getProvider(level)
            val reports = answers.zip(sentences).mapIndexed { index, (answerAndTarget, vietnamese) ->
                val (userAnswer, targetLang) = answerAndTarget
                val langLabel = provider.languageLabel
                """
                Sentence ${index + 1}:
                - Vietnamese original: $vietnamese
                - Target $langLabel: $targetLang
                - Student's answer: ${userAnswer.ifBlank { "(Skipped / No answer)" }}
                """.trimIndent()
            }.joinToString("\n\n")

            val systemPrompt = provider.getEvaluateLessonSystemPrompt(level, topic, reports)
            val userPrompt = provider.getEvaluateLessonUserPrompt()

            val rawResponse = geminiClient.generateContent(
                apiKey = apiKey,
                systemPrompt = systemPrompt,
                userPrompt = userPrompt,
                model = GEMINI_MODEL_CHAIN.first()
            )

            val cleanedJson = sanitizeJson(rawResponse)
            val feedback = json.decodeFromString<EnglishFeedbackDto>(cleanedJson).toDomain()
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
    ): AppResult<com.aienglish.feature.aiteacher.domain.model.SentenceExplanation> = withContext(dispatchers.io) {
        try {
            val apiKey = resolveApiKey()
            if (apiKey.isBlank()) {
                return@withContext Either.Left(
                    AppError.Network("Chưa có API Key. Vào Profile → cài Gemini API Key.")
                )
            }

            val provider = PromptProviderFactory.getProvider(level)
            var systemPrompt = provider.getExplainSentenceSystemPrompt(level, vietnamese, targetEnglish, userAnswer)
            if (prefs.getAiResponseLength() == "short") {
                systemPrompt += "\n\nCRITICAL REQUIREMENT: The 'explanation' must be extremely concise and brief (ngắn gọn), but it MUST still cover the general structure of the sentence (cấu trúc chung của câu) and explain all the words (bao hàm đủ các chữ) in the target sentence."
            }
            val userPrompt = provider.getExplainSentenceUserPrompt()

            val rawResponse = geminiClient.generateContent(
                apiKey = apiKey,
                systemPrompt = systemPrompt,
                userPrompt = userPrompt,
                model = GEMINI_MODEL_CHAIN.first()
            )
            val cleanedJson = sanitizeJson(rawResponse)
            val explanation = json.decodeFromString<com.aienglish.feature.aiteacher.data.model.SentenceExplanationDto>(cleanedJson).toDomain()
            Either.Right(explanation)
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

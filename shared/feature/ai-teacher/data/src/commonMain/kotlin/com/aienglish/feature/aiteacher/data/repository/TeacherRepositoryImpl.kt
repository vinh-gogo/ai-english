package com.aienglish.feature.aiteacher.data.repository

import arrow.core.Either
import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.common.result.AppError
import com.aienglish.core.common.result.AppResult
import com.aienglish.core.network.client.GeminiClient
import com.aienglish.core.network.client.GEMINI_MODEL_CHAIN
import com.aienglish.core.network.client.NovitaClient
import com.aienglish.core.network.client.NOVITA_MODEL_CHAIN
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
    private val novitaClient: NovitaClient,
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

    /**
     * Robustly extract score + explanation from AI response.
     * Handles cases where the explanation field contains unescaped quotes,
     * newlines, markdown special characters, etc. that would break standard
     * JSON deserialization.
     *
     * Strategy:
     * 1. Strip markdown fences
     * 2. Extract "score" via regex (safe, always a number)
     * 3. Extract "explanation" as everything between the first "explanation":\ "
     *    and the last " before the closing }
     * 4. Fallback: if no JSON structure detected, treat entire response as explanation
     */
    private fun extractExplanationSafe(
        raw: String
    ): com.aienglish.feature.aiteacher.data.model.SentenceExplanationDto {
        val cleaned = sanitizeJson(raw)

        // Fast path: try standard parse first (works if AI behaved correctly)
        try {
            return json.decodeFromString<com.aienglish.feature.aiteacher.data.model.SentenceExplanationDto>(cleaned)
        } catch (_: Exception) { /* fall through to manual extraction */ }

        // Extract score (always a simple number, no risk)
        val scoreRegex = Regex("\"score\"\\s*:\\s*(\\d+(?:\\.\\d+)?)")  
        val score = scoreRegex.find(cleaned)?.groupValues?.get(1)?.toFloatOrNull()?.toInt() ?: 0

        val arrayMatch = Regex("\"explanation\"\\s*:\\s*\\[").find(cleaned)
        val stringMatch = Regex("\"explanation\"\\s*:\\s*\"").find(cleaned)

        val explanationElement: kotlinx.serialization.json.JsonElement = if (arrayMatch != null) {
            val contentStart = arrayMatch.range.last - 0 // Starts with '['
            val content = cleaned.substring(contentStart)
            val trailingPattern = Regex("\\]\\s*\\}\\s*$")
            val trailingMatch = trailingPattern.find(content)
            val jsonArrayStr = if (trailingMatch != null) {
                content.substring(0, trailingMatch.range.first + 1)
            } else {
                content.trimEnd().removeSuffix("}").trim()
            }
            try {
                json.parseToJsonElement(jsonArrayStr)
            } catch (e: Exception) {
                kotlinx.serialization.json.JsonPrimitive("")
            }
        } else if (stringMatch != null) {
            val contentStart = stringMatch.range.last + 1
            val content = cleaned.substring(contentStart)
            val trailingPattern = Regex("\"\\s*\\}\\s*$")
            val trailingMatch = trailingPattern.find(content)
            val str = if (trailingMatch != null) {
                content.substring(0, trailingMatch.range.first)
            } else {
                content.trimEnd().removeSuffix("}").trimEnd().removeSuffix("\"")
            }
            val unescaped = str
                .replace("\\n", "\n")
                .replace("\\t", "\t")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
            kotlinx.serialization.json.JsonPrimitive(unescaped)
        } else {
            kotlinx.serialization.json.JsonPrimitive(cleaned)
        }

        return com.aienglish.feature.aiteacher.data.model.SentenceExplanationDto(
            score = score,
            explanation = explanationElement
        )
    }

    override suspend fun generateLesson(
        userId: String,
        topic: String,
        level: String
    ): AppResult<EnglishLesson> = withContext(dispatchers.io) {
        try {
            val providerType = prefs.getApiProvider()
            val nativeLang = prefs.getLanguageNative()
            val provider = PromptProviderFactory.getProvider(level)
            val systemPrompt = provider.getGenerateLessonSystemPrompt(level, nativeLang)
            val userPrompt = provider.getGenerateLessonUserPrompt(level, topic, nativeLang)

            val rawResponse = if (providerType == "novita") {
                val novitaKey = prefs.getNovitaApiKey()
                if (novitaKey.isBlank()) {
                    return@withContext Either.Left(
                        AppError.Network("Chưa có Novita API Key. Vào Profile → cài Novita API Key để bắt đầu.")
                    )
                }
                novitaClient.generateContent(
                    apiKey = novitaKey,
                    systemPrompt = systemPrompt,
                    userPrompt = userPrompt,
                    model = NOVITA_MODEL_CHAIN.first()
                )
            } else {
                val apiKey = resolveApiKey()
                if (apiKey.isBlank()) {
                    return@withContext Either.Left(
                        AppError.Network("Chưa có API Key. Vào Profile → cài Gemini API Key để bắt đầu.")
                    )
                }
                geminiClient.generateContent(
                    apiKey = apiKey,
                    systemPrompt = systemPrompt,
                    userPrompt = userPrompt,
                    model = GEMINI_MODEL_CHAIN.first()
                )
            }

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
            val providerType = prefs.getApiProvider()
            val nativeLang = prefs.getLanguageNative()
            val provider = PromptProviderFactory.getProvider(level)
            val reports = answers.zip(sentences).mapIndexed { index, (answerAndTarget, nativeText) ->
                val (userAnswer, targetLang) = answerAndTarget
                val langLabel = provider.languageLabel
                val nativeLabel = if (nativeLang == "vi") "Vietnamese" else "English"
                """
                Sentence ${index + 1}:
                - $nativeLabel original: $nativeText
                - Target $langLabel: $targetLang
                - Student's answer: ${userAnswer.ifBlank { "(Skipped / No answer)" }}
                """.trimIndent()
            }.joinToString("\n\n")

            val systemPrompt = provider.getEvaluateLessonSystemPrompt(level, topic, reports, nativeLang)
            val userPrompt = provider.getEvaluateLessonUserPrompt(nativeLang)

            val rawResponse = if (providerType == "novita") {
                val novitaKey = prefs.getNovitaApiKey()
                if (novitaKey.isBlank()) {
                    return@withContext Either.Left(
                        AppError.Network("Chưa có Novita API Key. Vào Profile → cài Novita API Key.")
                    )
                }
                novitaClient.generateContent(
                    apiKey = novitaKey,
                    systemPrompt = systemPrompt,
                    userPrompt = userPrompt,
                    model = NOVITA_MODEL_CHAIN.first()
                )
            } else {
                val apiKey = resolveApiKey()
                if (apiKey.isBlank()) {
                    return@withContext Either.Left(
                        AppError.Network("Chưa có API Key. Vào Profile → cài Gemini API Key.")
                    )
                }
                geminiClient.generateContent(
                    apiKey = apiKey,
                    systemPrompt = systemPrompt,
                    userPrompt = userPrompt,
                    model = GEMINI_MODEL_CHAIN.first()
                )
            }

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
            val providerType = prefs.getApiProvider()
            val nativeLang = prefs.getLanguageNative()
            val provider = PromptProviderFactory.getProvider(level)
            var systemPrompt = provider.getExplainSentenceSystemPrompt(level, vietnamese, targetEnglish, userAnswer, nativeLang)
            if (prefs.getAiResponseLength() == "short") {
                if (nativeLang == "vi") {
                    systemPrompt += "\n\nCRITICAL REQUIREMENT: The 'explanation' should be concise and direct (ngắn gọn, súc tích), but prioritize a complete and clear explanation. It must cover the general structure of the sentence (cấu trúc chung) and explain all the vocabulary/words (giải thích đầy đủ các từ) without being overly brief or skipping important grammar details."
                } else {
                    systemPrompt += "\n\nCRITICAL REQUIREMENT: The 'explanation' should be concise and direct, but prioritize a complete and clear explanation. It must cover the general structure of the sentence and explain all the vocabulary/words without being overly brief or skipping important grammar details."
                }
            }
            val userPrompt = provider.getExplainSentenceUserPrompt(nativeLang)

            val rawResponse = if (providerType == "novita") {
                val novitaKey = prefs.getNovitaApiKey()
                if (novitaKey.isBlank()) {
                    return@withContext Either.Left(
                        AppError.Network("Chưa có Novita API Key. Vào Profile → cài Novita API Key.")
                    )
                }
                novitaClient.generateContent(
                    apiKey = novitaKey,
                    systemPrompt = systemPrompt,
                    userPrompt = userPrompt,
                    model = NOVITA_MODEL_CHAIN.first()
                )
            } else {
                val apiKey = resolveApiKey()
                if (apiKey.isBlank()) {
                    return@withContext Either.Left(
                        AppError.Network("Chưa có API Key. Vào Profile → cài Gemini API Key.")
                    )
                }
                geminiClient.generateContent(
                    apiKey = apiKey,
                    systemPrompt = systemPrompt,
                    userPrompt = userPrompt,
                    model = GEMINI_MODEL_CHAIN.first()
                )
            }

            val explanation = extractExplanationSafe(rawResponse).toDomain()
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

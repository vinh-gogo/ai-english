package com.aienglish.core.network.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.*

// Standard exceptions for flow control matching mainichi-app-v2
class GeminiRateLimitException(val retryAfterSeconds: Long) :
    Exception("Rate limited (RPM). Retry after ${retryAfterSeconds}s.")

class GeminiDailyQuotaExhaustedException :
    Exception("Daily quota exhausted (limit=0). Switch model immediately.")

// Expanded model priority chain - identical to mainichi-app-v2 to maximize quota pools
val GEMINI_MODEL_CHAIN = listOf(
    "gemini-2.5-flash",
    "gemini-2.0-flash",
    "gemini-2.0-flash-lite",
    "gemini-1.5-flash",
    "gemini-2.5-flash-lite",
    "gemini-3.1-flash-lite",
    "gemini-2.5-pro",
    "gemini-3.1-flash",
    "gemini-3.1-pro",
)

class GeminiClient(private val httpClient: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    /**
     * Generate content with automatic fallback across model chain.
     * Integrates KMP retry backoff and RPM/RPD detection from MainichiKanji.
     */
    suspend fun generateContent(
        apiKey: String,
        systemPrompt: String,
        userPrompt: String,
        model: String = GEMINI_MODEL_CHAIN.first()
    ): String {
        // Build ordered list: requested model first, then the rest of the fallback chain
        val modelsToTry = buildList {
            add(model)
            GEMINI_MODEL_CHAIN.forEach { if (it != model) add(it) }
        }

        var lastException: Exception? = null

        for (currentModel in modelsToTry) {
            try {
                println("▶ MODEL: $currentModel")
                return executeWithRetry(apiKey, systemPrompt, userPrompt, currentModel)
            } catch (e: GeminiDailyQuotaExhaustedException) {
                println("⚠ [MODEL SWITCH] $currentModel unavailable/exhausted. Switching model...")
                lastException = e
            } catch (e: GeminiRateLimitException) {
                println("⚠ [RPM LIMIT] $currentModel rate limited. Switching model...")
                lastException = e
            } catch (e: Exception) {
                println("⚠ [ERROR] $currentModel: ${e.message}")
                lastException = e
            }
        }

        throw lastException ?: Exception("All Gemini models failed")
    }

    private suspend fun executeWithRetry(
        apiKey: String,
        systemPrompt: String,
        userPrompt: String,
        model: String,
        maxRetries: Int = 2
    ): String {
        repeat(maxRetries + 1) { attempt ->
            try {
                return callGemini(apiKey, systemPrompt, userPrompt, model)
            } catch (e: GeminiDailyQuotaExhaustedException) {
                throw e // Switch model immediately, do not retry
            } catch (e: GeminiRateLimitException) {
                if (attempt < maxRetries) {
                    val delayMs = minOf(e.retryAfterSeconds * 1000L, 15000L)
                    println("  [RPM Retry] Attempt ${attempt + 1}/$maxRetries. Waiting ${delayMs / 1000}s...")
                    delay(delayMs)
                } else throw e
            }
        }
        throw IllegalStateException("Unreachable")
    }

    private suspend fun callGemini(
        apiKey: String,
        systemPrompt: String,
        userPrompt: String,
        model: String
    ): String {
        val requestBody = buildJsonObject {
            putJsonArray("contents") {
                addJsonObject {
                    put("role", "user")
                    putJsonArray("parts") {
                        addJsonObject { put("text", userPrompt) }
                    }
                }
            }
            if (systemPrompt.isNotBlank()) {
                putJsonObject("systemInstruction") {
                    putJsonArray("parts") {
                        addJsonObject { put("text", systemPrompt) }
                    }
                }
            }
            putJsonObject("generationConfig") {
                put("temperature", 0.7)
                put("topP", 0.9)
                put("responseMimeType", "application/json")
                // Disable thinking mode — ensures clean structured output (no thinking part mixing)
                putJsonObject("thinkingConfig") {
                    put("thinkingBudget", 0)
                }
            }
        }.toString()

        val response: HttpResponse = httpClient.post(
            "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
        ) {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        val body = response.bodyAsText()

        // Handle Quota and Rate Limit status codes
        if (response.status.value == 429) {
            throw parseQuotaError(body)
        }

        if (response.status.value == 503) {
            throw GeminiDailyQuotaExhaustedException() // Switch model immediately
        }

        if (!response.status.isSuccess()) {
            val errorMsg = extractErrorMessage(body)
            throw Exception("Gemini API error [${response.status.value}] model=$model: $errorMsg")
        }

        return extractTextFromResponse(body, model)
    }

    private fun parseQuotaError(rawBody: String): Exception {
        return try {
            val root = json.parseToJsonElement(rawBody).jsonObject
            val errorObj = root["error"]?.jsonObject ?: return GeminiRateLimitException(30L)
            
            // Check message or details for daily quota indicator
            val message = errorObj["message"]?.jsonPrimitive?.content ?: ""
            if ("quota" in message.lowercase() && ("limit" in message.lowercase() || "exhausted" in message.lowercase())) {
                return GeminiDailyQuotaExhaustedException()
            }

            val details = errorObj["details"]?.jsonArray ?: return GeminiRateLimitException(30L)

            val retryDelay = details
                .firstOrNull { it.jsonObject["@type"]?.jsonPrimitive?.content?.contains("RetryInfo") == true }
                ?.jsonObject?.get("retryDelay")?.jsonPrimitive?.content
                ?.replace("s", "")?.toLong() ?: 30L

            val isDailyExhausted = details
                .filter { it.jsonObject["@type"]?.jsonPrimitive?.content?.contains("QuotaFailure") == true }
                .flatMap { it.jsonObject["violations"]?.jsonArray ?: listOf() }
                .any { it.jsonObject["quotaId"]?.jsonPrimitive?.content?.contains("PerDay") == true }

            if (isDailyExhausted) GeminiDailyQuotaExhaustedException()
            else GeminiRateLimitException(retryDelay)
        } catch (_: Exception) {
            GeminiRateLimitException(30L)
        }
    }

    private fun extractTextFromResponse(body: String, model: String): String {
        val root = try {
            json.parseToJsonElement(body).jsonObject
        } catch (e: Exception) {
            throw Exception("Invalid JSON from Gemini model=$model: $body")
        }

        root["error"]?.let { error ->
            val msg = error.jsonObject["message"]?.jsonPrimitive?.content ?: "Unknown error"
            throw Exception("Gemini error model=$model: $msg")
        }

        val parts = root["candidates"]?.jsonArray
            ?.firstOrNull()?.jsonObject
            ?.get("content")?.jsonObject
            ?.get("parts")?.jsonArray
            ?: throw Exception("No candidates in Gemini response model=$model. Body: $body")

        val allTexts = parts.mapNotNull { part ->
            part.jsonObject["text"]?.jsonPrimitive?.content?.trim()
        }.filter { it.isNotEmpty() }

        if (allTexts.isEmpty()) {
            val finishReason = root["candidates"]?.jsonArray
                ?.firstOrNull()?.jsonObject
                ?.get("finishReason")?.jsonPrimitive?.content
            throw Exception("Empty response from Gemini model=$model. FinishReason=$finishReason. Body: $body")
        }

        return allTexts.last()
    }

    private fun extractErrorMessage(body: String): String {
        return try {
            val root = json.parseToJsonElement(body).jsonObject
            root["error"]?.jsonObject?.get("message")?.jsonPrimitive?.content ?: body
        } catch (e: Exception) {
            body
        }
    }

    suspend fun listModels(apiKey: String): List<String> {
        return try {
            val response: HttpResponse = httpClient.get(
                "https://generativelanguage.googleapis.com/v1beta/models?key=$apiKey"
            )
            val body = response.bodyAsText()
            val root = json.parseToJsonElement(body).jsonObject
            root["models"]?.jsonArray?.mapNotNull { model ->
                val name = model.jsonObject["name"]?.jsonPrimitive?.content ?: return@mapNotNull null
                val methods = model.jsonObject["supportedGenerationMethods"]?.jsonArray
                    ?.mapNotNull { it.jsonPrimitive.content } ?: emptyList()
                if ("generateContent" in methods) name.removePrefix("models/") else null
            } ?: emptyList()
        } catch (e: Exception) {
            GEMINI_MODEL_CHAIN
        }
    }
}

package com.aienglish.core.network.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

// Model priority chain — ordered by speed + reliability for structured JSON output
// All confirmed available from /v1beta/models endpoint
val GEMINI_MODEL_CHAIN = listOf(
    "gemini-2.5-flash",      // Primary: stable, fast, 65K output
    "gemini-2.0-flash",      // Fallback 1: stable, no thinking, 8K output
    "gemini-2.0-flash-lite"  // Fallback 2: lightest, most reliable
)

class GeminiClient(private val httpClient: HttpClient) {

    /**
     * Generate content with automatic fallback across model chain.
     * Disables thinking mode (thinkingBudget=0) to ensure clean JSON output.
     * Retries on each model before giving up.
     */
    suspend fun generateContent(
        apiKey: String,
        systemPrompt: String,
        userPrompt: String,
        model: String = GEMINI_MODEL_CHAIN.first()
    ): String {
        // Build ordered list: requested model first, then rest of chain
        val modelsToTry = buildList {
            add(model)
            GEMINI_MODEL_CHAIN.forEach { if (it != model) add(it) }
        }

        var lastException: Exception? = null

        for (currentModel in modelsToTry) {
            try {
                return callGemini(apiKey, systemPrompt, userPrompt, currentModel)
            } catch (e: Exception) {
                lastException = e
                // Continue to next model in chain
            }
        }

        throw lastException ?: Exception("All Gemini models failed")
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
            putJsonObject("systemInstruction") {
                putJsonArray("parts") {
                    addJsonObject { put("text", systemPrompt) }
                }
            }
            putJsonObject("generationConfig") {
                put("temperature", 0.7)
                put("topP", 0.9)
                // Disable thinking mode — ensures clean structured output (no thinking part mixing)
                // Works for gemini-2.5-flash and other thinking-capable models
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

        // Check for API-level errors (wrong key, quota, etc.)
        if (!response.status.isSuccess()) {
            val errorMsg = extractErrorMessage(body)
            throw Exception("Gemini API error [${response.status.value}] model=$model: $errorMsg")
        }

        return extractTextFromResponse(body, model)
    }

    /**
     * Extract text from Gemini response.
     * Handles thinking models: takes the LAST text part (thinking parts come first).
     * Falls back to joining all parts if last part is empty.
     */
    private fun extractTextFromResponse(body: String, model: String): String {
        val json = Json { ignoreUnknownKeys = true }
        val root = try {
            json.parseToJsonElement(body).jsonObject
        } catch (e: Exception) {
            throw Exception("Invalid JSON from Gemini model=$model: $body")
        }

        // Check for error in response body
        root["error"]?.let { error ->
            val msg = error.jsonObject["message"]?.jsonPrimitive?.content ?: "Unknown error"
            throw Exception("Gemini error model=$model: $msg")
        }

        val parts = root["candidates"]?.jsonArray
            ?.firstOrNull()?.jsonObject
            ?.get("content")?.jsonObject
            ?.get("parts")?.jsonArray
            ?: throw Exception("No candidates in Gemini response model=$model. Body: $body")

        // For thinking models: thinking part is first, actual content is LAST
        // We try the last part first, then fall back to joining all text parts
        val allTexts = parts.mapNotNull { part ->
            part.jsonObject["text"]?.jsonPrimitive?.content?.trim()
        }.filter { it.isNotEmpty() }

        if (allTexts.isEmpty()) {
            // Check finish reason
            val finishReason = root["candidates"]?.jsonArray
                ?.firstOrNull()?.jsonObject
                ?.get("finishReason")?.jsonPrimitive?.content
            throw Exception("Empty response from Gemini model=$model. FinishReason=$finishReason. Body: $body")
        }

        // Return the last text part (actual output, not thinking)
        return allTexts.last()
    }

    private fun extractErrorMessage(body: String): String {
        return try {
            val json = Json { ignoreUnknownKeys = true }
            val root = json.parseToJsonElement(body).jsonObject
            root["error"]?.jsonObject?.get("message")?.jsonPrimitive?.content ?: body
        } catch (e: Exception) {
            body
        }
    }

    /**
     * Fetch list of available models from Gemini API.
     * Useful for dynamic model selection in Profile settings.
     */
    suspend fun listModels(apiKey: String): List<String> {
        return try {
            val response: HttpResponse = httpClient.get(
                "https://generativelanguage.googleapis.com/v1beta/models?key=$apiKey"
            )
            val body = response.bodyAsText()
            val json = Json { ignoreUnknownKeys = true }
            val root = json.parseToJsonElement(body).jsonObject
            root["models"]?.jsonArray?.mapNotNull { model ->
                val name = model.jsonObject["name"]?.jsonPrimitive?.content ?: return@mapNotNull null
                val methods = model.jsonObject["supportedGenerationMethods"]?.jsonArray
                    ?.mapNotNull { it.jsonPrimitive.content } ?: emptyList()
                // Only return models that support generateContent
                if ("generateContent" in methods) name.removePrefix("models/") else null
            } ?: emptyList()
        } catch (e: Exception) {
            GEMINI_MODEL_CHAIN // fallback to known list
        }
    }
}

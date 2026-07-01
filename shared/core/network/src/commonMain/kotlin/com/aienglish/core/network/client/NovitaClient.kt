package com.aienglish.core.network.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.serialization.json.*

val NOVITA_MODEL_CHAIN = listOf(
    "meta-llama/llama-3.1-8b-instruct",
    "meta-llama/llama-3-8b-instruct",
    "meta-llama/llama-3.1-70b-instruct"
)

class NovitaClient(private val httpClient: HttpClient) {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    suspend fun generateContent(
        apiKey: String,
        systemPrompt: String,
        userPrompt: String,
        model: String = NOVITA_MODEL_CHAIN.first()
    ): String {
        val modelsToTry = buildList {
            add(model)
            NOVITA_MODEL_CHAIN.forEach { if (it != model) add(it) }
        }

        var lastException: Exception? = null

        for (currentModel in modelsToTry) {
            try {
                println("▶ NOVITA MODEL: $currentModel")
                return executeWithRetry(apiKey, systemPrompt, userPrompt, currentModel)
            } catch (e: Exception) {
                println("⚠ [NOVITA ERROR] $currentModel: ${e.message}")
                lastException = e
            }
        }

        throw lastException ?: Exception("All Novita models failed")
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
                return callNovita(apiKey, systemPrompt, userPrompt, model)
            } catch (e: Exception) {
                if (attempt < maxRetries && "rate limit" in (e.message ?: "").lowercase()) {
                    val delayMs = 5000L
                    println("  [Novita Retry] Attempt ${attempt + 1}/$maxRetries. Waiting 5s...")
                    delay(delayMs)
                } else throw e
            }
        }
        throw IllegalStateException("Unreachable")
    }

    private suspend fun callNovita(
        apiKey: String,
        systemPrompt: String,
        userPrompt: String,
        model: String
    ): String {
        val requestBody = buildJsonObject {
            put("model", model)
            putJsonArray("messages") {
                if (systemPrompt.isNotBlank()) {
                    addJsonObject {
                        put("role", "system")
                        put("content", systemPrompt)
                    }
                }
                addJsonObject {
                    put("role", "user")
                    put("content", userPrompt)
                }
            }
            put("temperature", 0.7)
            putJsonObject("response_format") {
                put("type", "json_object")
            }
        }.toString()

        val response: HttpResponse = httpClient.post(
            "https://api.novita.ai/v3/openai/chat/completions"
        ) {
            header(HttpHeaders.Authorization, "Bearer $apiKey")
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        val body = response.bodyAsText()

        if (!response.status.isSuccess()) {
            val errorMsg = extractErrorMessage(body)
            throw Exception("Novita API error [${response.status.value}] model=$model: $errorMsg")
        }

        return extractTextFromResponse(body, model)
    }

    private fun extractTextFromResponse(body: String, model: String): String {
        val root = try {
            json.parseToJsonElement(body).jsonObject
        } catch (e: Exception) {
            throw Exception("Invalid JSON from Novita model=$model: $body")
        }

        root["error"]?.let { error ->
            val msg = error.jsonObject["message"]?.jsonPrimitive?.content ?: "Unknown error"
            throw Exception("Novita error model=$model: $msg")
        }

        val choices = root["choices"]?.jsonArray
            ?: throw Exception("No choices in Novita response model=$model. Body: $body")

        val content = choices.firstOrNull()?.jsonObject
            ?.get("message")?.jsonObject
            ?.get("content")?.jsonPrimitive?.content
            ?: throw Exception("No content in choices from Novita response model=$model. Body: $body")

        return content
    }

    private fun extractErrorMessage(body: String): String {
        return try {
            val root = json.parseToJsonElement(body).jsonObject
            root["error"]?.jsonObject?.get("message")?.jsonPrimitive?.content
                ?: root["message"]?.jsonPrimitive?.content
                ?: body
        } catch (e: Exception) {
            body
        }
    }
}

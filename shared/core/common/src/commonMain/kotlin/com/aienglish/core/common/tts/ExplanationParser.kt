package com.aienglish.core.common.tts

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive

object ExplanationParser {
    fun parseSegments(jsonStr: String): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        try {
            val jsonElement = Json.parseToJsonElement(jsonStr)
            if (jsonElement is JsonArray) {
                for (item in jsonElement) {
                    if (item is JsonObject) {
                        val text = item["text"]?.jsonPrimitive?.content ?: ""
                        val lang = item["lang"]?.jsonPrimitive?.content ?: ""
                        list.add(Pair(text, lang))
                    }
                }
            }
        } catch (e: Exception) {
            // Silence parsing exceptions
        }
        return list
    }
}

package com.aienglish.feature.aiteacher.ui.screen.components

import com.aienglish.core.common.tts.ExplanationParser

fun getDisplayExplanation(explanation: String, targetLanguage: String): String {
    if (explanation.trim().startsWith("[") && explanation.trim().endsWith("]")) {
        val list = mutableListOf<String>()
        try {
            val segments = ExplanationParser.parseSegments(explanation)
            for ((text, lang) in segments) {
                var textVal = text
                // Neu la ngon ngu muc tieu thi boc trong ngoac kep va in dam
                if (lang.lowercase() == targetLanguage.lowercase() && textVal.trim().isNotEmpty()) {
                    val leadingSpaces = textVal.takeWhile { it.isWhitespace() }
                    val trailingSpaces = textVal.takeLastWhile { it.isWhitespace() }
                    val body = textVal.trim()
                    val cleanBody = body.removeSurrounding("\"").removeSurrounding("'")
                    textVal = "$leadingSpaces**\"$cleanBody\"**$trailingSpaces"
                }
                list.add(textVal)
            }
            if (list.isNotEmpty()) {
                return list.joinToString("")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return explanation
}

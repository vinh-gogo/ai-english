package com.aienglish.feature.aiteacher.data.repository.prompt

interface LanguagePromptProvider {
    val languageLabel: String

    fun getGenerateLessonSystemPrompt(level: String, nativeLanguage: String): String
    fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String

    fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String
    fun getEvaluateLessonUserPrompt(nativeLanguage: String): String

    fun getExplainSentenceSystemPrompt(level: String, vietnamese: String, targetText: String, userAnswer: String, nativeLanguage: String): String
    fun getExplainSentenceUserPrompt(nativeLanguage: String): String
}

package com.aienglish.feature.aiteacher.data.repository.prompt

interface LanguagePromptProvider {
    val languageLabel: String

    fun getGenerateLessonSystemPrompt(level: String): String
    fun getGenerateLessonUserPrompt(level: String, topic: String): String

    fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String): String
    fun getEvaluateLessonUserPrompt(): String

    fun getExplainSentenceSystemPrompt(level: String, vietnamese: String, targetText: String, userAnswer: String): String
    fun getExplainSentenceUserPrompt(): String
}

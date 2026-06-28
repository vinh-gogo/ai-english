package com.aienglish.feature.aiteacher.domain.model

data class EnglishLesson(
    val sentences: List<EnglishSentence>
)

data class EnglishSentence(
    val vietnamese: String,
    val english: String,
    val tokens: List<EnglishToken> = emptyList(),
    val explanation: String = ""
)

data class EnglishToken(
    val word: String,
    val phonetic: String = "",
    val partOfSpeech: String = ""
)

data class EnglishFeedback(
    val score: Int,
    val review: String,
    val sentenceScores: List<Int> = emptyList()
)

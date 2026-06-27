package com.aienglish.feature.aiteacher.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EnglishLesson(
    val sentences: List<EnglishSentence>
)

@Serializable
data class EnglishSentence(
    val vietnamese: String,
    val english: String,
    val tokens: List<EnglishToken> = emptyList(),
    val explanation: String = ""
)

@Serializable
data class EnglishToken(
    val word: String,
    val phonetic: String = "",
    val partOfSpeech: String = ""
)

@Serializable
data class EnglishFeedback(
    val score: Int,
    val review: String
)

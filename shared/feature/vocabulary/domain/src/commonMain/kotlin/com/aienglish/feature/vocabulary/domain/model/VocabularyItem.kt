package com.aienglish.feature.vocabulary.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VocabularyItem(
    val id: String,
    val word: String,
    val phonetic: String = "",
    val meaning: String,
    val example: String = "",
    val note: String = "",
    val language: String = "en",
    val createdAt: Long = 0L
)

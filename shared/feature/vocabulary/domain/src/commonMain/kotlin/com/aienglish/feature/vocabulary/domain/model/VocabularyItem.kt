package com.aienglish.feature.vocabulary.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VocabularyItem(
    val id: String,
    val word: String,
    val partOfSpeech: String = "",
    val phonetic: String = "",
    val meaning: String,
    val collocations: List<String> = emptyList(),
    val example: String = "",
    val note: String = "",
    val language: String = "en",
    val category: VocabularyCategory = VocabularyCategory.GENERAL,
    val createdAt: Long = 0L
)

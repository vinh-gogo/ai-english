package com.aienglish.feature.vocabulary.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class PartOfSpeech(
    val code: String,
    val shortName: String,
    val displayName: String
) {
    NOUN("noun", "n.", "Danh từ"),
    VERB("verb", "v.", "Động từ"),
    ADJECTIVE("adj", "adj.", "Tính từ"),
    ADVERB("adv", "adv.", "Trạng từ"),
    PHRASE("phrase", "phr.", "Cụm từ / Thành ngữ"),
    PREPOSITION("prep", "prep.", "Giới từ"),
    OTHER("other", "khác", "Khác");

    companion object {
        fun fromCode(code: String?): PartOfSpeech {
            if (code.isNullOrBlank()) return NOUN
            return entries.firstOrNull { 
                it.code.equals(code, ignoreCase = true) || 
                it.shortName.equals(code, ignoreCase = true) 
            } ?: NOUN
        }
    }
}

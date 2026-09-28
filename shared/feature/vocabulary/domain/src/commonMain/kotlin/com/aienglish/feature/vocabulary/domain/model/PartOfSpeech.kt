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
            val clean = code.trim().lowercase().removeSuffix(".")
            return entries.firstOrNull { 
                it.code.equals(clean, ignoreCase = true) || 
                it.shortName.removeSuffix(".").equals(clean, ignoreCase = true) ||
                it.displayName.equals(code.trim(), ignoreCase = true) ||
                it.name.equals(clean, ignoreCase = true)
            } ?: NOUN
        }
    }
}

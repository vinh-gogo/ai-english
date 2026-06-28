package com.aienglish.feature.aiteacher.domain.repository

import com.aienglish.feature.aiteacher.domain.model.TopicItem

interface TopicRepository {
    suspend fun getTopics(systemCode: String, levelCodes: List<String>): List<TopicItem>
    suspend fun seedDatabase(
        systemsJson: String,
        levelsJson: String,
        englishTopicsJson: String,
        japaneseTopicsJson: String,
        chineseTopicsJson: String,
        spanishTopicsJson: String,
        hindiTopicsJson: String,
        frenchTopicsJson: String
    )
}

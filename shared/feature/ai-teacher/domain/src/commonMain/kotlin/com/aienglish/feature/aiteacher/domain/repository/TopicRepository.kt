package com.aienglish.feature.aiteacher.domain.repository

import com.aienglish.feature.aiteacher.domain.model.TopicItem

interface TopicRepository {
    suspend fun getTopics(systemCode: String, levelCodes: List<String>): List<TopicItem>
    suspend fun seedDatabase()
    suspend fun addCustomTopic(name: String, systemCode: String, levelCode: String, isAiRecommended: Boolean = false)
    suspend fun deleteTopic(id: String, systemCode: String)
}


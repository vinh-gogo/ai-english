package com.aienglish.feature.aiteacher.data.repository

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.aiteacher.domain.model.TopicItem
import com.aienglish.feature.aiteacher.domain.repository.TopicRepository
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class JsonLearningSystem(
    val systemCode: String,
    val name: String,
    val description: String? = null
)

@Serializable
private data class JsonLearningLevel(
    val systemCode: String,
    val levelCode: String,
    val name: String
)

@Serializable
private data class JsonTopicItem(
    val id: Int,
    val name: String,
    val systemCode: String,
    val levelCode: String
)

class TopicRepositoryImpl(
    private val database: AIEnglishDatabase,
    private val dispatchers: AppDispatchers
) : TopicRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun getTopics(systemCode: String, levelCodes: List<String>): List<TopicItem> =
        withContext(dispatchers.io) {
            database.userSchemaQueries.getTopicsForLevels(systemCode, levelCodes)
                .executeAsList()
                .map {
                    TopicItem(
                        id = it.topic_id.toInt(),
                        name = it.name,
                        levelCode = it.level_code
                    )
                }
        }

    private fun String.removeBom(): String =
        if (this.startsWith("\uFEFF")) this.substring(1) else this

    override suspend fun seedDatabase(
        systemsJson: String,
        levelsJson: String,
        englishTopicsJson: String,
        japaneseTopicsJson: String
    ): Unit =
        withContext(dispatchers.io) {
            database.transaction {
                try {
                    val systems = json.decodeFromString<List<JsonLearningSystem>>(systemsJson.removeBom().trim())
                    val levels = json.decodeFromString<List<JsonLearningLevel>>(levelsJson.removeBom().trim())
                    val englishTopics = json.decodeFromString<List<JsonTopicItem>>(englishTopicsJson.removeBom().trim())
                    val japaneseTopics = json.decodeFromString<List<JsonTopicItem>>(japaneseTopicsJson.removeBom().trim())
                    
                    // 0. Clear old topics
                    database.userSchemaQueries.deleteAllTopics()
                    
                    // 1. Insert Systems
                    systems.forEach { sys ->
                        database.userSchemaQueries.insertLearningSystem(
                            system_code = sys.systemCode,
                            name = sys.name,
                            description = sys.description
                        )
                    }
                    
                    // 2. Insert Levels
                    levels.forEach { lvl ->
                        database.userSchemaQueries.insertLearningLevel(
                            system_code = lvl.systemCode,
                            level_code = lvl.levelCode,
                            name = lvl.name
                        )
                    }
                    
                    // 3. Insert English Topics
                    englishTopics.forEach { topic ->
                        database.userSchemaQueries.insertTopic(
                            topic_id = topic.id.toLong(),
                            name = topic.name,
                            system_code = topic.systemCode,
                            level_code = topic.levelCode
                        )
                    }

                    // 4. Insert Japanese Topics
                    japaneseTopics.forEach { topic ->
                        database.userSchemaQueries.insertTopic(
                            topic_id = topic.id.toLong(),
                            name = topic.name,
                            system_code = topic.systemCode,
                            level_code = topic.levelCode
                        )
                    }
                } catch (e: Exception) {
                    println("Seeding error in TopicRepositoryImpl: ${e.message}")
                    throw e
                }
            }
        }
}

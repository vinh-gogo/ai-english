package com.aienglish.feature.aiteacher.data.repository

import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.aiteacher.domain.model.TopicItem
import com.aienglish.feature.aiteacher.domain.repository.TopicRepository
import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
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
    private val prefs: AppPreferences,
    private val dispatchers: AppDispatchers,
    private val jsonResourceReader: JsonResourceReader
) : TopicRepository {
    private val json = Json { ignoreUnknownKeys = true }
    private val enTopicsCache = mutableMapOf<String, Map<Int, JsonTopicItem>>()

    override suspend fun getTopics(systemCode: String, levelCodes: List<String>): List<TopicItem> =
        withContext(dispatchers.io) {
            val dbTopics = database.userSchemaQueries.getTopicsForLevels(systemCode, levelCodes)
                .executeAsList()
            
            val isEn = prefs.getLanguageNative() == "en"
            
            if (isEn && (systemCode == "TOPIK" || systemCode == "HSK" || systemCode == "JLPT" || systemCode == "VIET")) {
                try {
                    val enTopicsMap = enTopicsCache.getOrPut(systemCode) {
                        val enTopicsJson = when (systemCode) {
                            "TOPIK" -> jsonResourceReader.readKoreanTopicsEnJson()
                            "HSK" -> jsonResourceReader.readChineseTopicsEnJson()
                            "VIET" -> jsonResourceReader.readVietnameseTopicsEnJson()
                            else -> jsonResourceReader.readJapaneseTopicsEnJson()
                        }
                        val enTopicsList = json.decodeFromString<List<JsonTopicItem>>(enTopicsJson.removeBom().trim())
                        enTopicsList.associateBy { it.id }
                    }
                    
                    dbTopics.map { row ->
                        val enTopic = enTopicsMap[row.topic_id.toInt()]
                        TopicItem(
                            id = row.topic_id.toInt(),
                            name = enTopic?.name ?: row.name,
                            levelCode = row.level_code
                        )
                    }
                } catch (e: Exception) {
                    dbTopics.map {
                        TopicItem(
                            id = it.topic_id.toInt(),
                            name = it.name,
                            levelCode = it.level_code
                        )
                    }
                }
            } else {
                dbTopics.map {
                    TopicItem(
                        id = it.topic_id.toInt(),
                        name = it.name,
                        levelCode = it.level_code
                    )
                }
            }
        }

    private fun String.removeBom(): String =
        if (this.startsWith("\uFEFF")) this.substring(1) else this

    override suspend fun seedDatabase(): Unit =
        withContext(dispatchers.io) {
            val systemsJson = jsonResourceReader.readSystemsJson()
            val levelsJson = jsonResourceReader.readLevelsJson()
            val englishTopicsJson = jsonResourceReader.readEnglishTopicsJson()
            val japaneseTopicsJson = jsonResourceReader.readJapaneseTopicsJson()
            val chineseTopicsJson = jsonResourceReader.readChineseTopicsJson()
            val spanishTopicsJson = jsonResourceReader.readSpanishTopicsJson()
            val hindiTopicsJson = jsonResourceReader.readHindiTopicsJson()
            val frenchTopicsJson = jsonResourceReader.readFrenchTopicsJson()
            val germanTopicsJson = jsonResourceReader.readGermanTopicsJson()
            val koreanTopicsJson = jsonResourceReader.readKoreanTopicsJson()
            val vietnameseTopicsEnJson = jsonResourceReader.readVietnameseTopicsEnJson()

            database.transaction {
                try {
                    val systems = json.decodeFromString<List<JsonLearningSystem>>(systemsJson.removeBom().trim())
                    val levels = json.decodeFromString<List<JsonLearningLevel>>(levelsJson.removeBom().trim())
                    val englishTopics = json.decodeFromString<List<JsonTopicItem>>(englishTopicsJson.removeBom().trim())
                    val japaneseTopics = json.decodeFromString<List<JsonTopicItem>>(japaneseTopicsJson.removeBom().trim())
                    val chineseTopics = json.decodeFromString<List<JsonTopicItem>>(chineseTopicsJson.removeBom().trim())
                    val spanishTopics = json.decodeFromString<List<JsonTopicItem>>(spanishTopicsJson.removeBom().trim())
                    val hindiTopics = json.decodeFromString<List<JsonTopicItem>>(hindiTopicsJson.removeBom().trim())
                    val frenchTopics = json.decodeFromString<List<JsonTopicItem>>(frenchTopicsJson.removeBom().trim())
                    val germanTopics = json.decodeFromString<List<JsonTopicItem>>(germanTopicsJson.removeBom().trim())
                    val koreanTopics = json.decodeFromString<List<JsonTopicItem>>(koreanTopicsJson.removeBom().trim())
                    val vietnameseTopics = json.decodeFromString<List<JsonTopicItem>>(vietnameseTopicsEnJson.removeBom().trim())
                    
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
                            level_code = topic.levelCode,
                            updated_at = 0L,
                            is_ai_recommended = 0L
                        )
                    }

                    // 4. Insert Japanese Topics
                    japaneseTopics.forEach { topic ->
                        database.userSchemaQueries.insertTopic(
                            topic_id = topic.id.toLong(),
                            name = topic.name,
                            system_code = topic.systemCode,
                            level_code = topic.levelCode,
                            updated_at = 0L,
                            is_ai_recommended = 0L
                        )
                    }

                    // 5. Insert Chinese Topics
                    chineseTopics.forEach { topic ->
                        database.userSchemaQueries.insertTopic(
                            topic_id = topic.id.toLong(),
                            name = topic.name,
                            system_code = topic.systemCode,
                            level_code = topic.levelCode,
                            updated_at = 0L,
                            is_ai_recommended = 0L
                        )
                    }

                    // 6. Insert Spanish Topics
                    spanishTopics.forEach { topic ->
                        database.userSchemaQueries.insertTopic(
                            topic_id = topic.id.toLong(),
                            name = topic.name,
                            system_code = topic.systemCode,
                            level_code = topic.levelCode,
                            updated_at = 0L,
                            is_ai_recommended = 0L
                        )
                    }

                    // 7. Insert Hindi Topics
                    hindiTopics.forEach { topic ->
                        database.userSchemaQueries.insertTopic(
                            topic_id = topic.id.toLong(),
                            name = topic.name,
                            system_code = topic.systemCode,
                            level_code = topic.levelCode,
                            updated_at = 0L,
                            is_ai_recommended = 0L
                        )
                    }

                    // 8. Insert French Topics
                    frenchTopics.forEach { topic ->
                        database.userSchemaQueries.insertTopic(
                            topic_id = topic.id.toLong(),
                            name = topic.name,
                            system_code = topic.systemCode,
                            level_code = topic.levelCode,
                            updated_at = 0L,
                            is_ai_recommended = 0L
                        )
                    }

                    // 9. Insert German Topics
                    germanTopics.forEach { topic ->
                        database.userSchemaQueries.insertTopic(
                            topic_id = topic.id.toLong(),
                            name = topic.name,
                            system_code = topic.systemCode,
                            level_code = topic.levelCode,
                            updated_at = 0L,
                            is_ai_recommended = 0L
                        )
                    }

                    // 10. Insert Korean Topics
                    koreanTopics.forEach { topic ->
                        database.userSchemaQueries.insertTopic(
                            topic_id = topic.id.toLong(),
                            name = topic.name,
                            system_code = topic.systemCode,
                            level_code = topic.levelCode,
                            updated_at = 0L,
                            is_ai_recommended = 0L
                        )
                    }

                    // 11. Insert Vietnamese Topics (EN locale)
                    vietnameseTopics.forEach { topic ->
                        database.userSchemaQueries.insertTopic(
                            topic_id = topic.id.toLong(),
                            name = topic.name,
                            system_code = topic.systemCode,
                            level_code = topic.levelCode,
                            updated_at = 0L,
                            is_ai_recommended = 0L
                        )
                    }
                } catch (e: Exception) {
                    println("Seeding error in TopicRepositoryImpl: ${e.message}")
                    throw e
                }
            }
        }

    override suspend fun addCustomTopic(name: String, systemCode: String, levelCode: String, isAiRecommended: Boolean): Unit =
        withContext(dispatchers.io) {
            try {
                val allTopics = database.userSchemaQueries.getAllTopics().executeAsList()
                val maxId = allTopics.filter { it.system_code == systemCode }.maxOfOrNull { it.topic_id } ?: 10000L
                val newId = maxId + 1
                database.userSchemaQueries.insertTopic(
                    topic_id = newId,
                    name = name,
                    system_code = systemCode,
                    level_code = levelCode,
                    updated_at = kotlinx.datetime.Clock.System.now().toEpochMilliseconds(),
                    is_ai_recommended = if (isAiRecommended) 1L else 0L
                )
            } catch (e: Exception) {
                println("Error inserting custom topic in TopicRepositoryImpl: ${e.message}")
            }
        }

    override suspend fun deleteTopic(id: Int, systemCode: String): Unit =
        withContext(dispatchers.io) {
            try {
                database.userSchemaQueries.deleteTopic(
                    system_code = systemCode,
                    topic_id = id.toLong()
                )
            } catch (e: Exception) {
                println("Error deleting topic in TopicRepositoryImpl: ${e.message}")
            }
        }
}


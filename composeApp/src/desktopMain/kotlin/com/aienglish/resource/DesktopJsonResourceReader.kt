package com.aienglish.resource

import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

class DesktopJsonResourceReader : JsonResourceReader {
    override suspend fun readSystemsJson(): String = readResource("composeResources/files/learning_systems.json")
    override suspend fun readLevelsJson(): String = readResource("composeResources/files/learning_levels.json")
    override suspend fun readEnglishTopicsJson(): String = readResource("composeResources/files/english_topics.json")
    override suspend fun readJapaneseTopicsJson(): String = readResource("composeResources/files/japanese_topics.json")
    override suspend fun readChineseTopicsJson(): String = readResource("composeResources/files/chinese_topics.json")

    private suspend fun readResource(path: String): String = withContext(Dispatchers.IO) {
        val stream = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
            ?: throw FileNotFoundException("Resource not found in classpath: $path")
        stream.bufferedReader().use { it.readText() }
    }
}

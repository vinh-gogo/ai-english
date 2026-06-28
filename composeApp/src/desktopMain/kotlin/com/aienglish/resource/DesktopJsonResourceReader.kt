package com.aienglish.resource

import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
import java.io.FileNotFoundException

class DesktopJsonResourceReader : JsonResourceReader {
    override suspend fun readSystemsJson(): String = readResource("composeResources/files/learning_systems.json")
    override suspend fun readLevelsJson(): String = readResource("composeResources/files/learning_levels.json")
    override suspend fun readEnglishTopicsJson(): String = readResource("composeResources/files/english_topics.json")
    override suspend fun readJapaneseTopicsJson(): String = readResource("composeResources/files/japanese_topics.json")

    private fun readResource(path: String): String {
        val stream = Thread.currentThread().contextClassLoader.getResourceAsStream(path)
            ?: throw FileNotFoundException("Resource not found in classpath: $path")
        return stream.bufferedReader().use { it.readText() }
    }
}

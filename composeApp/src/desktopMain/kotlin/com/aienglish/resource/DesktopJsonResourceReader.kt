package com.aienglish.resource

import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

class DesktopJsonResourceReader : JsonResourceReader {
    override suspend fun readSystemsJson(): String = readResource("composeResources/files/learning_systems.json")
    override suspend fun readLevelsJson(): String = readResource("composeResources/files/learning_levels.json")
    override suspend fun readEnglishTopicsJson(): String = readResource("composeResources/files/vi/english_topics.json")
    override suspend fun readJapaneseTopicsJson(): String = readResource("composeResources/files/vi/japanese_topics.json")
    override suspend fun readChineseTopicsJson(): String = readResource("composeResources/files/vi/chinese_topics.json")
    override suspend fun readSpanishTopicsJson(): String = readResource("composeResources/files/vi/spanish_topics.json")
    override suspend fun readHindiTopicsJson(): String = readResource("composeResources/files/vi/hindi_topics.json")
    override suspend fun readFrenchTopicsJson(): String = readResource("composeResources/files/vi/french_topics.json")
    override suspend fun readGermanTopicsJson(): String = readResource("composeResources/files/vi/german_topics.json")
    override suspend fun readKoreanTopicsJson(): String = readResource("composeResources/files/vi/korean_topics.json")
    override suspend fun readKoreanTopicsEnJson(): String = readResource("composeResources/files/en/korean_topics.json")
    override suspend fun readChineseTopicsEnJson(): String = readResource("composeResources/files/en/chinese_topics.json")
    override suspend fun readJapaneseTopicsEnJson(): String = readResource("composeResources/files/en/japanese_topics.json")
    override suspend fun readVietnameseTopicsEnJson(): String = readResource("composeResources/files/en/vietnamese_topics.json")

    private suspend fun readResource(path: String): String = withContext(Dispatchers.IO) {
        val cleanPath = path.removePrefix("/")
        val stripped = cleanPath.removePrefix("composeResources/").removePrefix("/")
        val candidates = listOf(
            "composeResources/ai_english.composeapp.generated.resources/$stripped",
            cleanPath,
            "composeResources/$stripped",
            stripped
        ).distinct()

        val classLoaders = listOfNotNull(
            Thread.currentThread().contextClassLoader,
            DesktopJsonResourceReader::class.java.classLoader,
            ClassLoader.getSystemClassLoader()
        )

        for (candidate in candidates) {
            for (cl in classLoaders) {
                val stream = cl.getResourceAsStream(candidate)
                    ?: cl.getResourceAsStream("/$candidate")
                if (stream != null) {
                    return@withContext stream.bufferedReader().use { it.readText() }
                }
            }
        }

        throw FileNotFoundException("Resource not found in classpath: $path (checked: $candidates)")
    }
}

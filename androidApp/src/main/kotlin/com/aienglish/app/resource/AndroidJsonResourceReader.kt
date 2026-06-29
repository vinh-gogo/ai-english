package com.aienglish.app.resource

import android.content.Context
import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidJsonResourceReader(private val context: Context) : JsonResourceReader {
    override suspend fun readSystemsJson(): String = readAsset("files/learning_systems.json")
    override suspend fun readLevelsJson(): String = readAsset("files/learning_levels.json")
    override suspend fun readEnglishTopicsJson(): String = readAsset("files/vi/english_topics.json")
    override suspend fun readJapaneseTopicsJson(): String = readAsset("files/vi/japanese_topics.json")
    override suspend fun readChineseTopicsJson(): String = readAsset("files/vi/chinese_topics.json")
    override suspend fun readSpanishTopicsJson(): String = readAsset("files/vi/spanish_topics.json")
    override suspend fun readHindiTopicsJson(): String = readAsset("files/vi/hindi_topics.json")
    override suspend fun readFrenchTopicsJson(): String = readAsset("files/vi/french_topics.json")
    override suspend fun readGermanTopicsJson(): String = readAsset("files/vi/german_topics.json")
    override suspend fun readKoreanTopicsJson(): String = readAsset("files/vi/korean_topics.json")
    override suspend fun readKoreanTopicsEnJson(): String = readAsset("files/en/korean_topics.json")
    override suspend fun readChineseTopicsEnJson(): String = readAsset("files/en/chinese_topics.json")
    override suspend fun readJapaneseTopicsEnJson(): String = readAsset("files/en/japanese_topics.json")

    private suspend fun readAsset(path: String): String = withContext(Dispatchers.IO) {
        val stream = context.assets.open(path)
        stream.bufferedReader().use { it.readText() }
    }
}

package com.aienglish.app.resource

import android.content.Context
import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidJsonResourceReader(private val context: Context) : JsonResourceReader {
    override suspend fun readSystemsJson(): String = readAsset("files/learning_systems.json")
    override suspend fun readLevelsJson(): String = readAsset("files/learning_levels.json")
    override suspend fun readEnglishTopicsJson(): String = readAsset("files/english_topics.json")
    override suspend fun readJapaneseTopicsJson(): String = readAsset("files/japanese_topics.json")
    override suspend fun readChineseTopicsJson(): String = readAsset("files/chinese_topics.json")
    override suspend fun readSpanishTopicsJson(): String = readAsset("files/spanish_topics.json")
    override suspend fun readHindiTopicsJson(): String = readAsset("files/hindi_topics.json")

    private suspend fun readAsset(path: String): String = withContext(Dispatchers.IO) {
        val stream = context.assets.open(path)
        stream.bufferedReader().use { it.readText() }
    }
}

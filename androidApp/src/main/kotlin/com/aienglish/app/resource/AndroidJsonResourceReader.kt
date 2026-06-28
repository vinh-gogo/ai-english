package com.aienglish.app.resource

import android.content.Context
import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
import java.io.FileNotFoundException

class AndroidJsonResourceReader(private val context: Context) : JsonResourceReader {
    override suspend fun readSystemsJson(): String = readAsset("files/learning_systems.json")
    override suspend fun readLevelsJson(): String = readAsset("files/learning_levels.json")
    override suspend fun readEnglishTopicsJson(): String = readAsset("files/english_topics.json")
    override suspend fun readJapaneseTopicsJson(): String = readAsset("files/japanese_topics.json")
    override suspend fun readChineseTopicsJson(): String = readAsset("files/chinese_topics.json")

    private fun readAsset(path: String): String {
        val stream = context.assets.open(path)
        return stream.bufferedReader().use { it.readText() }
    }
}

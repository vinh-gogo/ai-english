package com.aienglish.feature.aiteacher.domain.resource

interface JsonResourceReader {
    suspend fun readSystemsJson(): String
    suspend fun readLevelsJson(): String
    suspend fun readEnglishTopicsJson(): String
    suspend fun readJapaneseTopicsJson(): String
}

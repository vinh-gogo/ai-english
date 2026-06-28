package com.aienglish.feature.aiteacher.domain.resource

interface JsonResourceReader {
    suspend fun readSystemsJson(): String
    suspend fun readLevelsJson(): String
    suspend fun readEnglishTopicsJson(): String
    suspend fun readJapaneseTopicsJson(): String
    suspend fun readChineseTopicsJson(): String
    suspend fun readSpanishTopicsJson(): String
    suspend fun readHindiTopicsJson(): String
    suspend fun readFrenchTopicsJson(): String
    suspend fun readGermanTopicsJson(): String
    suspend fun readKoreanTopicsJson(): String
}

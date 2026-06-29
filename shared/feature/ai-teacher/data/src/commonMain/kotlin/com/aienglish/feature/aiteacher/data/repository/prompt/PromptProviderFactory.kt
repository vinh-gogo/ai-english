package com.aienglish.feature.aiteacher.data.repository.prompt

object PromptProviderFactory {
    fun getProvider(level: String): LanguagePromptProvider {
        return when {
            level.startsWith("N") -> JapanesePromptProvider
            level.startsWith("HSK") -> ChinesePromptProvider
            level.startsWith("DELE") || level.startsWith("SIELE") -> SpanishPromptProvider
            level.startsWith("Hindi") -> HindiPromptProvider
            level.startsWith("DELF") || level.startsWith("DALF") -> FrenchPromptProvider
            level.startsWith("Goethe") || level.startsWith("TestDaF") -> GermanPromptProvider
            level.startsWith("TOPIK") -> KoreanPromptProvider
            level == "TIEU_HOC" || level == "TRUNG_HOC" || level == "DAI_HOC" -> VietnamesePromptProvider
            else -> EnglishPromptProvider
        }
    }
}

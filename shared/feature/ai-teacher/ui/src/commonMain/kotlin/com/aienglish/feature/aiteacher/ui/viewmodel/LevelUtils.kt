package com.aienglish.feature.aiteacher.ui.viewmodel

fun getDisplayLevel(state: TeacherUiState): Pair<String, String> {
    val system = if (state.levelType == LevelType.JLPT) "JLPT" 
        else if (state.levelType == LevelType.HINDI) "Hindi"
        else if (state.levelType == LevelType.GOETHE) "Goethe"
        else if (state.levelType == LevelType.TESTDAF) "TestDaF"
        else state.levelType.systemCode

    val currentLevel = state.selectedLevels[state.levelType] ?: state.levelType.defaultLevel
    val value = when (state.levelType) {
        LevelType.HSK -> currentLevel.removePrefix("HSK")
        LevelType.TOPIK -> currentLevel.removePrefix("TOPIK")
        LevelType.VIET -> when (currentLevel) {
            "TIEU_HOC" -> "Tiểu học"
            "TRUNG_HOC" -> "Trung học"
            "DAI_HOC" -> "Đại học"
            else -> currentLevel
        }
        else -> currentLevel
    }
    return Pair(system, value)
}

fun parseLevelTypeAndValue(level: String): Pair<LevelType, String> {
    val isToeic = level.startsWith("TOEIC")
    val isJlpt = level.startsWith("N")
    val isHsk = level.startsWith("HSK")
    val isDele = level.startsWith("DELE")
    val isSiele = level.startsWith("SIELE")
    val isHindi = level.startsWith("Hindi")
    val isDelf = level.startsWith("DELF")
    val isDalf = level.startsWith("DALF")
    val isGoethe = level.startsWith("Goethe")
    val isTestdaf = level.startsWith("TestDaF")
    val isTopik = level.startsWith("TOPIK")
    
    val expectedType = when {
        isToeic -> LevelType.TOEIC
        isJlpt -> LevelType.JLPT
        isHsk -> LevelType.HSK
        isDele -> LevelType.DELE
        isSiele -> LevelType.SIELE
        isHindi -> LevelType.HINDI
        isDelf -> LevelType.DELF
        isDalf -> LevelType.DALF
        isGoethe -> LevelType.GOETHE
        isTestdaf -> LevelType.TESTDAF
        isTopik -> LevelType.TOPIK
        level == "TIEU_HOC" || level == "TRUNG_HOC" || level == "DAI_HOC" -> LevelType.VIET
        else -> LevelType.CEFR
    }
    val levelValue = when {
        isToeic -> level.removePrefix("TOEIC ").trim()
        isDele -> level.removePrefix("DELE ").trim()
        isSiele -> level.removePrefix("SIELE ").trim()
        isHindi -> level.removePrefix("Hindi ").trim()
        isDelf -> level.removePrefix("DELF ").trim()
        isDalf -> level.removePrefix("DALF ").trim()
        isGoethe -> level.removePrefix("Goethe ").trim()
        isTestdaf -> level.removePrefix("TestDaF ").trim()
        else -> level
    }
    return expectedType to levelValue
}

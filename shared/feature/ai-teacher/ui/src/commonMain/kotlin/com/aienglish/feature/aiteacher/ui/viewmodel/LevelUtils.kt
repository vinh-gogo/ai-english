package com.aienglish.feature.aiteacher.ui.viewmodel

fun getDisplayLevel(state: TeacherUiState): Pair<String, String> {
    val system = when (state.levelType) {
        LevelType.CEFR -> "CEFR"
        LevelType.TOEIC -> "TOEIC"
        LevelType.JLPT -> "JLPT"
        LevelType.HSK -> "HSK"
        LevelType.DELE -> "DELE"
        LevelType.SIELE -> "SIELE"
        LevelType.HINDI -> "Hindi"
        LevelType.DELF -> "DELF"
        LevelType.DALF -> "DALF"
        LevelType.GOETHE -> "Goethe"
        LevelType.TESTDAF -> "TestDaF"
        LevelType.TOPIK -> "TOPIK"
        LevelType.VIET -> "VIET"
    }
    val value = when (state.levelType) {
        LevelType.CEFR -> state.selectedCefrLevel
        LevelType.TOEIC -> state.selectedToeicLevel
        LevelType.JLPT -> state.selectedJlptLevel
        LevelType.HSK -> state.selectedHskLevel.removePrefix("HSK")
        LevelType.DELE -> state.selectedDeleLevel
        LevelType.SIELE -> state.selectedSieleLevel
        LevelType.HINDI -> state.selectedHindiLevel
        LevelType.DELF -> state.selectedDelfLevel
        LevelType.DALF -> state.selectedDalfLevel
        LevelType.GOETHE -> state.selectedGoetheLevel
        LevelType.TESTDAF -> state.selectedTestdafLevel
        LevelType.TOPIK -> state.selectedTopikLevel.removePrefix("TOPIK")
        LevelType.VIET -> when (state.selectedVietLevel) {
            "TIEU_HOC" -> "Tiểu học"
            "TRUNG_HOC" -> "Trung học"
            "DAI_HOC" -> "Đại học"
            else -> state.selectedVietLevel
        }
    }
    return Pair(system, value)
}

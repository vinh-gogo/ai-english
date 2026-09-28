package com.aienglish.feature.aiteacher.ui.viewmodel

import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.QuestionNote
import com.aienglish.feature.aiteacher.domain.model.TopicItem

enum class LevelType(val systemCode: String, val levels: List<String>, val defaultLevel: String, val uiPrefix: String = "") {
    CEFR("CEFR", listOf("A1", "A2", "B1", "B2", "C1", "C2"), "B1"),
    TOEIC("TOEIC", listOf("300", "500", "700", "900"), "500", "TOEIC "),
    JLPT("JLPT", listOf("N5", "N4", "N3", "N2", "N1"), "N3"),
    HSK("HSK", listOf("HSK1", "HSK2", "HSK3", "HSK4", "HSK5", "HSK6"), "HSK3"),
    DELE("DELE", listOf("A1", "A2", "B1", "B2", "C1", "C2"), "B1", "DELE "),
    SIELE("SIELE", listOf("A1", "A2", "B1", "B2", "C1"), "B1", "SIELE "),
    HINDI("HINDI", listOf("A1", "A2", "B1", "B2"), "B1", "Hindi "),
    DELF("DELF", listOf("A1", "A2", "B1", "B2"), "B1", "DELF "),
    DALF("DALF", listOf("C1", "C2"), "C1", "DALF "),
    GOETHE("GOETHE", listOf("A1", "A2", "B1", "B2", "C1", "C2"), "B1", "Goethe "),
    TESTDAF("TESTDAF", listOf("TDN3", "TDN4", "TDN5"), "TDN4", "TestDaF "),
    TOPIK("TOPIK", listOf("TOPIK1", "TOPIK2", "TOPIK3", "TOPIK4", "TOPIK5", "TOPIK6"), "TOPIK3"),
    VIET("VIET", listOf("TIEU_HOC", "TRUNG_HOC", "DAI_HOC"), "TIEU_HOC");

    companion object {
        fun fromTargetLanguage(target: String): LevelType = when (target) {
            "ja" -> JLPT
            "zh" -> HSK
            "es" -> DELE
            "hi" -> HINDI
            "fr" -> DELF
            "de" -> GOETHE
            "ko" -> TOPIK
            "vi" -> VIET
            else -> CEFR
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
                isToeic -> TOEIC
                isJlpt -> JLPT
                isHsk -> HSK
                isDele -> DELE
                isSiele -> SIELE
                isHindi -> HINDI
                isDelf -> DELF
                isDalf -> DALF
                isGoethe -> GOETHE
                isTestdaf -> TESTDAF
                isTopik -> TOPIK
                level == "TIEU_HOC" || level == "TRUNG_HOC" || level == "DAI_HOC" -> VIET
                else -> CEFR
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

        fun getDisplayLevel(state: TeacherUiState): Pair<String, String> {
            val system = if (state.levelType == JLPT) "JLPT" 
                else if (state.levelType == HINDI) "Hindi"
                else if (state.levelType == GOETHE) "Goethe"
                else if (state.levelType == TESTDAF) "TestDaF"
                else state.levelType.systemCode

            val currentLevel = state.selectedLevels[state.levelType] ?: state.levelType.defaultLevel
            val value = when (state.levelType) {
                HSK -> currentLevel.removePrefix("HSK")
                TOPIK -> currentLevel.removePrefix("TOPIK")
                VIET -> when (currentLevel) {
                    "TIEU_HOC" -> "Tiểu học"
                    "TRUNG_HOC" -> "Trung học"
                    "DAI_HOC" -> "Đại học"
                    else -> currentLevel
                }
                else -> currentLevel
            }
            return Pair(system, value)
        }
    }
}

data class TeacherUiState(
    // Topic selection
    val topics: List<TopicItem> = emptyList(),
    val selectedTopicIndex: Int = 0,
    val topicSearchQuery: String = "",
    val levelType: LevelType = LevelType.CEFR,
    val selectedLevels: Map<LevelType, String> = LevelType.entries.associateWith { it.defaultLevel },
    val targetLanguage: String = "en",
    val isCustomTopicMode: Boolean = false,
    val customTopicInput: String = "",
    val topicCounts: Map<String, Int> = emptyMap(),
    val showSaveToDbDialog: Boolean = false,

    // Lesson state
    val isGeneratingLesson: Boolean = false,
    val lesson: EnglishLesson? = null,
    val userAnswers: Map<Int, String> = emptyMap(),      // idx -> typed answer
    val checkedAnswers: Set<Int> = emptySet(),           // idx -> checked (submitted per sentence)
    val currentSentenceIndex: Int = 0,

    // Evaluation
    val isEvaluating: Boolean = false,
    val feedback: EnglishFeedback? = null,

    // Explanation
    val explainingSentenceIndex: Int? = null,           // tracks which sentence index is currently loading its AI explanation
    val explanations: Map<Int, String> = emptyMap(),

    // Notes
    val showSavedNotesBottomSheet: Boolean = false,
    val savedNotes: List<QuestionNote> = emptyList(),
    val savedNoteCounts: Map<Int, Int> = emptyMap(),

    // Error
    val error: String? = null,
    val infoMessage: String? = null
) {
    val selectedTopic: String get() = if (isCustomTopicMode) customTopicInput else topics.getOrNull(selectedTopicIndex)?.name ?: ""
    val selectedTopicId: String? get() = if (isCustomTopicMode) null else topics.getOrNull(selectedTopicIndex)?.id
    val currentLevelCode: String get() = selectedLevels[levelType] ?: levelType.defaultLevel
    val selectedLevelText: String get() = levelType.uiPrefix + currentLevelCode
}

data class TopicSelectionState(
    val levelType: LevelType,
    val selectedLevels: Map<LevelType, String>,
    val topicSearchQuery: String,
    val isCustomTopicMode: Boolean,
    val customTopicInput: String,
    val selectedTopicIndex: Int
)

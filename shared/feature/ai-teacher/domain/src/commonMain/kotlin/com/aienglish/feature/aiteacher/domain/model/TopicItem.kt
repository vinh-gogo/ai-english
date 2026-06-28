package com.aienglish.feature.aiteacher.domain.model

data class TopicItem(
    val id: Int,
    val name: String,
    val levelCode: String
)

fun getCompatibleLevels(activeLevel: String): List<String> {
    return when (activeLevel) {
        // CEFR
        "A1" -> listOf("A1")
        "A2" -> listOf("A1", "A2")
        "B1" -> listOf("A1", "A2", "B1")
        "B2" -> listOf("A1", "A2", "B1", "B2")
        "C1" -> listOf("A1", "A2", "B1", "B2", "C1")
        "C2" -> listOf("A1", "A2", "B1", "B2", "C1", "C2")
        // TOEIC
        "300" -> listOf("300")
        "500" -> listOf("300", "500")
        "700" -> listOf("300", "500", "700")
        "900" -> listOf("300", "500", "700", "900")
        // JLPT
        "N5" -> listOf("N5")
        "N4" -> listOf("N5", "N4")
        "N3" -> listOf("N5", "N4", "N3")
        "N2" -> listOf("N5", "N4", "N3", "N2")
        "N1" -> listOf("N5", "N4", "N3", "N2", "N1")
        // HSK
        "HSK1" -> listOf("HSK1")
        "HSK2" -> listOf("HSK1", "HSK2")
        "HSK3" -> listOf("HSK1", "HSK2", "HSK3")
        "HSK4" -> listOf("HSK1", "HSK2", "HSK3", "HSK4")
        "HSK5" -> listOf("HSK1", "HSK2", "HSK3", "HSK4", "HSK5")
        "HSK6" -> listOf("HSK1", "HSK2", "HSK3", "HSK4", "HSK5", "HSK6")
        // TestDaF
        "TDN3" -> listOf("TDN3")
        "TDN4" -> listOf("TDN3", "TDN4")
        "TDN5" -> listOf("TDN3", "TDN4", "TDN5")
        // TOPIK
        "TOPIK1" -> listOf("TOPIK1")
        "TOPIK2" -> listOf("TOPIK1", "TOPIK2")
        "TOPIK3" -> listOf("TOPIK1", "TOPIK2", "TOPIK3")
        "TOPIK4" -> listOf("TOPIK1", "TOPIK2", "TOPIK3", "TOPIK4")
        "TOPIK5" -> listOf("TOPIK1", "TOPIK2", "TOPIK3", "TOPIK4", "TOPIK5")
        "TOPIK6" -> listOf("TOPIK1", "TOPIK2", "TOPIK3", "TOPIK4", "TOPIK5", "TOPIK6")
        else -> when (activeLevel) {
            // Goethe shares CEFR names, so let's match them here as fallback
            "A1" -> listOf("A1")
            "A2" -> listOf("A1", "A2")
            "B1" -> listOf("A1", "A2", "B1")
            "B2" -> listOf("A1", "A2", "B1", "B2")
            "C1" -> listOf("A1", "A2", "B1", "B2", "C1")
            "C2" -> listOf("A1", "A2", "B1", "B2", "C1", "C2")
            else -> listOf(activeLevel)
        }
    }
}

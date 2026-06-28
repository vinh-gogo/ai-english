package com.aienglish.feature.history.domain.model

data class LessonHistory(
    val id: Long,
    val topic: String,
    val level: String,
    val score: Int?,
    val status: HistoryStatus,
    val createdAt: Long,
    val completedAt: Long?,
    val lessonData: String? = null,
    val userAnswersData: String? = null,
    val explanationsData: String? = null,
    val feedbackData: String? = null
)

enum class HistoryStatus {
    IN_PROGRESS,
    COMPLETED;

    companion object {
        fun from(value: String): HistoryStatus =
            entries.firstOrNull { it.name == value } ?: IN_PROGRESS
    }
}

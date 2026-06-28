package com.aienglish.feature.history.domain.model

data class LessonHistory(
    val id: Long,
    val topic: String,
    val level: String,
    val score: Int?,
    val status: HistoryStatus,
    val createdAt: Long,
    val completedAt: Long?,
    val lessonJson: String? = null,
    val userAnswersJson: String? = null,
    val explanationsJson: String? = null,
    val feedbackJson: String? = null
)

enum class HistoryStatus {
    IN_PROGRESS,
    COMPLETED;

    companion object {
        fun from(value: String): HistoryStatus =
            entries.firstOrNull { it.name == value } ?: IN_PROGRESS
    }
}

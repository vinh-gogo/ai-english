package com.aienglish.feature.history.domain.model

data class LessonHistory(
    val id: Long,
    val topic: String,
    val level: String,
    val status: HistoryStatus,
    val createdAt: Long,
    val completedAt: Long?,
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val topicId: String? = null
)

enum class HistoryStatus {
    IN_PROGRESS,
    COMPLETED;

    companion object {
        fun from(value: String): HistoryStatus =
            entries.firstOrNull { it.name == value } ?: IN_PROGRESS
    }
}

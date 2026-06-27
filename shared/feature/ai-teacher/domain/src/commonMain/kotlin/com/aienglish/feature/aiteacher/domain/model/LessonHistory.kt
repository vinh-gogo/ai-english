package com.aienglish.feature.aiteacher.domain.model

data class LessonHistory(
    val id: Long,
    val topic: String,
    val level: String,
    val score: Int?,
    val status: HistoryStatus,
    val createdAt: String,
    val completedAt: String?
)

enum class HistoryStatus {
    IN_PROGRESS,
    COMPLETED;

    companion object {
        fun from(value: String): HistoryStatus =
            entries.firstOrNull { it.name == value } ?: IN_PROGRESS
    }
}

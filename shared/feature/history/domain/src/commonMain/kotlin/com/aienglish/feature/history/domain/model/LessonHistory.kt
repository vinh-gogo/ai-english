package com.aienglish.feature.history.domain.model

data class LessonHistory(
    val id: Long,
    val topic: String,          // derived từ learning_topics.name (qua JOIN trong DB)
    val level: String,          // reconstructed: uiPrefix + levelCode (để backward compat với UI)
    val systemCode: String = "CEFR",   // NEW: system code (CEFR, TOEIC, JLPT, custom, ...)
    val levelCode: String = "B1",      // NEW: raw level code không có prefix
    val topicId: Long = 0L,            // CHANGED: String? → Long (NOT NULL INTEGER trong DB)
    val status: HistoryStatus,
    val createdAt: Long,
    val completedAt: Long?,
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val attemptCount: Int = 1,
    val updatedAt: Long = 0
)

enum class HistoryStatus {
    IN_PROGRESS,
    COMPLETED;

    companion object {
        fun from(value: String): HistoryStatus =
            entries.firstOrNull { it.name == value } ?: IN_PROGRESS
    }
}

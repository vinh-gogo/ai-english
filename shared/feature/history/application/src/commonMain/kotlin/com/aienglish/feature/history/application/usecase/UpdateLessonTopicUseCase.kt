package com.aienglish.feature.history.application.usecase

import com.aienglish.feature.history.domain.repository.HistoryRepository

class UpdateLessonTopicUseCase(
    private val repository: HistoryRepository
) {
    /**
     * 3NF: cập nhật tên custom topic trong learning_topics.
     * @param topicId ID của topic trong learning_topics (custom topics có âm ID)
     * @param newTopic Tên mới cho topic
     */
    suspend operator fun invoke(topicId: Long, newTopic: String) {
        repository.updateCustomTopicName(topicId, newTopic)
    }
}

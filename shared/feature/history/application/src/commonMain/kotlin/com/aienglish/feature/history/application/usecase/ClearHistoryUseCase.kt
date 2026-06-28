package com.aienglish.feature.history.application.usecase

import com.aienglish.feature.history.domain.repository.HistoryRepository

class ClearHistoryUseCase(
    private val repository: HistoryRepository
) {
    suspend operator fun invoke(ids: List<Long>) = repository.clearHistory(ids)
}

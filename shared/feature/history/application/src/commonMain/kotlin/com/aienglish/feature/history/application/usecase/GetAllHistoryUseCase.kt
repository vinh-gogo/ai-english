package com.aienglish.feature.history.application.usecase

import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow

class GetAllHistoryUseCase(
    private val repository: HistoryRepository
) {
    operator fun invoke(): Flow<List<LessonHistory>> = repository.getAllHistory()
}

package com.aienglish.feature.history.ui.viewmodel

import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class HistoryState(
    val items: List<LessonHistory> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HistoryViewModel(
    private val historyRepository: HistoryRepository
) : BaseViewModel<HistoryState>(HistoryState()) {

    init {
        observeHistory()
    }

    private fun observeHistory() {
        historyRepository.getAllHistory()
            .onEach { items ->
                setState { copy(items = items, isLoading = false, error = null) }
            }
            .catch { e ->
                setState { copy(isLoading = false, error = e.message ?: "Lỗi tải lịch sử bài học") }
            }
            .launchIn(viewModelScope)
    }

    fun clearHistory() {
        viewModelScope.launch {
            try {
                historyRepository.clearHistory()
                setState { copy(error = null) }
            } catch (e: Exception) {
                setState { copy(error = "Không thể xóa lịch sử: ${e.message}") }
            }
        }
    }

    fun dismissError() = setState { copy(error = null) }
}

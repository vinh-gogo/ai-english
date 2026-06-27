package com.aienglish.feature.aiteacher.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.aiteacher.domain.model.LessonHistory
import com.aienglish.feature.aiteacher.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class HistoryState(
    val items: List<LessonHistory> = emptyList(),
    val isLoading: Boolean = true
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
                setState { copy(items = items, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyRepository.clearHistory()
        }
    }
}

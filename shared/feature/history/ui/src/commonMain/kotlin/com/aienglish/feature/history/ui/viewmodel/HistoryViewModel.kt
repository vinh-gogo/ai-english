package com.aienglish.feature.history.ui.viewmodel

import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.history.application.usecase.ClearHistoryUseCase
import com.aienglish.feature.history.application.usecase.GetAllHistoryUseCase
import com.aienglish.feature.history.domain.model.LessonHistory
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

import com.aienglish.core.common.prefs.AppPreferences
import kotlinx.coroutines.flow.combine

data class HistoryState(
    val items: List<LessonHistory> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class HistoryViewModel(
    private val getAllHistoryUseCase: GetAllHistoryUseCase,
    private val clearHistoryUseCase: ClearHistoryUseCase,
    private val prefs: AppPreferences
) : BaseViewModel<HistoryState>(HistoryState()) {

    init {
        observeHistory()
    }

    private fun getLanguageFromLevel(level: String): String {
        return when {
            level.startsWith("N") -> "ja"
            level.startsWith("HSK") -> "zh"
            level.startsWith("DELE") || level.startsWith("SIELE") -> "es"
            level.startsWith("Hindi") -> "hi"
            level.startsWith("DELF") || level.startsWith("DALF") -> "fr"
            level.startsWith("Goethe") || level.startsWith("TestDaF") -> "de"
            level.startsWith("TOPIK") -> "ko"
            else -> "en"
        }
    }

    private fun observeHistory() {
        combine(
            getAllHistoryUseCase(),
            prefs.languageTargetFlow
        ) { items, targetLang ->
            items.filter { getLanguageFromLevel(it.level) == targetLang }
        }
            .onEach { filteredItems ->
                setState { copy(items = filteredItems, isLoading = false, error = null) }
            }
            .catch { e ->
                setState { copy(isLoading = false, error = e.message ?: "Lỗi tải lịch sử bài học") }
            }
            .launchIn(viewModelScope)
    }

    fun clearHistory() {
        viewModelScope.launch {
            try {
                clearHistoryUseCase()
                setState { copy(error = null) }
            } catch (e: Exception) {
                setState { copy(error = "Không thể xóa lịch sử: ${e.message}") }
            }
        }
    }

    fun dismissError() = setState { copy(error = null) }
}

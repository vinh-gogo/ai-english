package com.aienglish.core.common.viewmodel

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Minimal ViewModel base that works for Desktop (JVM) without Android lifecycle.
 * Uses a SupervisorJob-based scope for structured concurrency.
 */
abstract class BaseViewModel<S : Any>(
    initialState: S,
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main
) {
    protected val viewModelScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + mainDispatcher)

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    protected val currentState: S get() = _state.value

    protected fun setState(reducer: S.() -> S) {
        _state.update { it.reducer() }
    }

    open fun onCleared() {
        viewModelScope.cancel() // Hủy job để tránh leak coroutine khi ViewModel giải phóng
    }
}

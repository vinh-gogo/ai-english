package com.aienglish.feature.pro.ui.viewmodel

import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.pro.application.usecase.GetNativeLanguageUseCase
import com.aienglish.feature.pro.application.usecase.GetVipStatusUseCase
import com.aienglish.feature.pro.application.usecase.SetVipStatusUseCase
import com.aienglish.feature.pro.ui.intent.ProIntent
import com.aienglish.feature.pro.ui.state.ProState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ProViewModel(
    private val getVipStatusUseCase: GetVipStatusUseCase,
    private val setVipStatusUseCase: SetVipStatusUseCase,
    private val getNativeLanguageUseCase: GetNativeLanguageUseCase
) : BaseViewModel<ProState>(ProState()) {

    init {
        // Collect reactive status from flows
        viewModelScope.launch {
            combine(
                getVipStatusUseCase(),
                getNativeLanguageUseCase()
            ) { isPaid, nativeLang ->
                isPaid to nativeLang
            }.collect { (isPaid, nativeLang) ->
                setState {
                    copy(
                        isPaid = isPaid,
                        languageNative = nativeLang
                    )
                }
            }
        }
    }

    fun onIntent(intent: ProIntent) {
        processIntent(intent)
    }

    private fun processIntent(intent: ProIntent) {
        when (intent) {
            is ProIntent.SyncState -> {
                // Handled reactively via flow collections in init block
            }
            is ProIntent.ShowVip -> {
                setState { copy(showVip = true) }
            }
            is ProIntent.HideVip -> {
                setState { copy(showVip = false) }
            }
            is ProIntent.SetVipPaid -> {
                viewModelScope.launch {
                    setVipStatusUseCase(intent.isPaid)
                }
            }
        }
    }
}

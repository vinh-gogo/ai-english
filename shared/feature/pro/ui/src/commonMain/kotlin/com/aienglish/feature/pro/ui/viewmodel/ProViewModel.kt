package com.aienglish.feature.pro.ui.viewmodel

import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.pro.application.usecase.GetNativeLanguageUseCase
import com.aienglish.feature.pro.application.usecase.GetVipStatusUseCase
import com.aienglish.feature.pro.application.usecase.SetVipStatusUseCase
import com.aienglish.feature.pro.ui.intent.ProIntent
import com.aienglish.feature.pro.ui.state.ProState

class ProViewModel(
    private val getVipStatusUseCase: GetVipStatusUseCase,
    private val setVipStatusUseCase: SetVipStatusUseCase,
    private val getNativeLanguageUseCase: GetNativeLanguageUseCase
) : BaseViewModel<ProState>(ProState()) {

    init {
        processIntent(ProIntent.SyncState)
    }

    fun onIntent(intent: ProIntent) {
        processIntent(intent)
    }

    private fun processIntent(intent: ProIntent) {
        when (intent) {
            is ProIntent.SyncState -> {
                setState {
                    copy(
                        isPaid = getVipStatusUseCase(),
                        languageNative = getNativeLanguageUseCase()
                    )
                }
            }
            is ProIntent.ShowVip -> {
                setState { copy(showVip = true) }
            }
            is ProIntent.HideVip -> {
                setState { copy(showVip = false) }
            }
            is ProIntent.SetVipPaid -> {
                setVipStatusUseCase(intent.isPaid)
                setState { copy(isPaid = intent.isPaid) }
            }
        }
    }
}

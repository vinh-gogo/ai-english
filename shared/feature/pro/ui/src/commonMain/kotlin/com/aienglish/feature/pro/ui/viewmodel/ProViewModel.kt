package com.aienglish.feature.pro.ui.viewmodel

import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.pro.domain.repository.ProRepository

data class ProState(
    val isPaid: Boolean = false,
    val showVip: Boolean = false,
    val languageNative: String = "vi"
)

class ProViewModel(
    private val repository: ProRepository
) : BaseViewModel<ProState>(ProState()) {

    init {
        syncState()
    }

    fun syncState() {
        setState {
            copy(
                isPaid = repository.isPaid(),
                languageNative = repository.getLanguageNative()
            )
        }
    }

    fun showVip() = setState { copy(showVip = true) }
    fun hideVip() = setState { copy(showVip = false) }

    fun setPaid(value: Boolean) {
        repository.savePaid(value)
        setState { copy(isPaid = value) }
    }
}

package com.aienglish.feature.history.ui.di

import com.aienglish.feature.history.ui.viewmodel.HistoryViewModel
import org.koin.dsl.module

val historyUiModule = module {
    single {
        HistoryViewModel(
            getAllHistoryUseCase = get(),
            clearHistoryUseCase = get()
        )
    }
}

package com.aienglish.feature.history.application.di

import com.aienglish.feature.history.application.usecase.ClearHistoryUseCase
import com.aienglish.feature.history.application.usecase.GetAllHistoryUseCase
import org.koin.dsl.module

val historyApplicationModule = module {
    factory { GetAllHistoryUseCase(get()) }
    factory { ClearHistoryUseCase(get()) }
}

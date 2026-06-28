package com.aienglish.feature.pro.application.di

import com.aienglish.feature.pro.application.usecase.GetNativeLanguageUseCase
import com.aienglish.feature.pro.application.usecase.GetVipStatusUseCase
import com.aienglish.feature.pro.application.usecase.SetVipStatusUseCase
import org.koin.dsl.module

val proApplicationModule = module {
    factory { GetVipStatusUseCase(get()) }
    factory { SetVipStatusUseCase(get()) }
    factory { GetNativeLanguageUseCase(get()) }
}

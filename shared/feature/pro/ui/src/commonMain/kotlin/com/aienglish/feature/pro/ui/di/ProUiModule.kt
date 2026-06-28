package com.aienglish.feature.pro.ui.di

import com.aienglish.feature.pro.ui.viewmodel.ProViewModel
import org.koin.dsl.module

val proUiModule = module {
    single { ProViewModel(get()) }
}

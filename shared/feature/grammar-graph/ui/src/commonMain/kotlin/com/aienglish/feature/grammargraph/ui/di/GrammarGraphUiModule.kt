package com.aienglish.feature.grammargraph.ui.di

import com.aienglish.feature.grammargraph.ui.viewmodel.GrammarGraphViewModel
import org.koin.dsl.module

val grammarGraphUiModule = module {
    factory { GrammarGraphViewModel(get(), get()) }
}

package com.aienglish.feature.grammargraph.application.di

import com.aienglish.feature.grammargraph.application.usecase.GetGrammarGraphUseCase
import org.koin.dsl.module

val grammarGraphApplicationModule = module {
    single { GetGrammarGraphUseCase(get()) }
}

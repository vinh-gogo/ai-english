package com.aienglish.feature.grammargraph.data.di

import com.aienglish.feature.grammargraph.data.repository.GrammarGraphRepositoryImpl
import com.aienglish.feature.grammargraph.domain.repository.GrammarGraphRepository
import org.koin.dsl.module

import org.koin.core.qualifier.named

val grammarGraphDataModule = module {
    single<GrammarGraphRepository> { 
        GrammarGraphRepositoryImpl(
            database = get(),
            driver = get(),
            geminiClient = get(),
            prefs = get(),
            fallbackApiKey = get(named("fallback_api_key"))
        ) 
    }
}

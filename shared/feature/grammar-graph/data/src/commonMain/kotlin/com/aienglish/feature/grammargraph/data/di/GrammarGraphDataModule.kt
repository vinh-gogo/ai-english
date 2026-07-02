package com.aienglish.feature.grammargraph.data.di

import com.aienglish.feature.grammargraph.data.repository.GrammarGraphRepositoryImpl
import com.aienglish.feature.grammargraph.domain.repository.GrammarGraphRepository
import org.koin.dsl.module

val grammarGraphDataModule = module {
    single<GrammarGraphRepository> { GrammarGraphRepositoryImpl(get()) }
}

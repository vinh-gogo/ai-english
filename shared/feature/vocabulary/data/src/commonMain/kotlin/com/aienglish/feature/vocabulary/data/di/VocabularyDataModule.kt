package com.aienglish.feature.vocabulary.data.di

import com.aienglish.feature.vocabulary.data.repository.InMemoryVocabularyRepository
import com.aienglish.feature.vocabulary.domain.repository.VocabularyRepository
import org.koin.dsl.module

val vocabularyDataModule = module {
    single<VocabularyRepository> { InMemoryVocabularyRepository() }
}

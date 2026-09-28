package com.aienglish.feature.vocabulary.application.di

import com.aienglish.feature.vocabulary.application.usecase.AddVocabularyItemUseCase
import com.aienglish.feature.vocabulary.application.usecase.DeleteVocabularyItemUseCase
import com.aienglish.feature.vocabulary.application.usecase.GetVocabularyListUseCase
import org.koin.dsl.module

val vocabularyApplicationModule = module {
    single { GetVocabularyListUseCase(get()) }
    single { AddVocabularyItemUseCase(get()) }
    single { DeleteVocabularyItemUseCase(get()) }
}

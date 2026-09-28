package com.aienglish.feature.vocabulary.ui.di

import com.aienglish.feature.vocabulary.ui.viewmodel.VocabularyViewModel
import org.koin.dsl.module

val vocabularyUiModule = module {
    single {
        VocabularyViewModel(
            getVocabularyListUseCase = get(),
            addVocabularyItemUseCase = get(),
            deleteVocabularyItemUseCase = get()
        )
    }
}

package com.aienglish.feature.vocabulary.application.usecase

import com.aienglish.feature.vocabulary.domain.model.VocabularyItem
import com.aienglish.feature.vocabulary.domain.repository.VocabularyRepository

class AddVocabularyItemUseCase(
    private val repository: VocabularyRepository
) {
    suspend operator fun invoke(item: VocabularyItem) {
        repository.addVocabularyItem(item)
    }
}

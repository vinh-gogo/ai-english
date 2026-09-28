package com.aienglish.feature.vocabulary.application.usecase

import com.aienglish.feature.vocabulary.domain.repository.VocabularyRepository

class DeleteVocabularyItemUseCase(
    private val repository: VocabularyRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteVocabularyItem(id)
    }
}

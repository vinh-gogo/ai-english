package com.aienglish.feature.vocabulary.application.usecase

import com.aienglish.feature.vocabulary.domain.model.VocabularyItem
import com.aienglish.feature.vocabulary.domain.repository.VocabularyRepository
import kotlinx.coroutines.flow.Flow

class GetVocabularyListUseCase(
    private val repository: VocabularyRepository
) {
    operator fun invoke(): Flow<List<VocabularyItem>> {
        return repository.getVocabularyList()
    }
}

package com.aienglish.feature.vocabulary.domain.repository

import com.aienglish.feature.vocabulary.domain.model.VocabularyItem
import kotlinx.coroutines.flow.Flow

interface VocabularyRepository {
    fun getVocabularyList(): Flow<List<VocabularyItem>>
    suspend fun addVocabularyItem(item: VocabularyItem)
    suspend fun updateVocabularyItem(item: VocabularyItem)
    suspend fun deleteVocabularyItem(id: String)
}

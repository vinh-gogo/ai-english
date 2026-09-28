package com.aienglish.feature.vocabulary.ui.viewmodel

import com.aienglish.feature.vocabulary.domain.model.VocabularyItem

data class VocabularyUiState(
    val items: List<VocabularyItem> = emptyList(),
    val filteredItems: List<VocabularyItem> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val showAddDialog: Boolean = false
)

package com.aienglish.feature.vocabulary.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aienglish.core.common.tts.TtsPlayer
import com.aienglish.feature.vocabulary.application.usecase.AddVocabularyItemUseCase
import com.aienglish.feature.vocabulary.application.usecase.DeleteVocabularyItemUseCase
import com.aienglish.feature.vocabulary.application.usecase.GetVocabularyListUseCase
import com.aienglish.feature.vocabulary.domain.model.VocabularyCategory
import com.aienglish.feature.vocabulary.domain.model.VocabularyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class VocabularyViewModel(
    private val getVocabularyListUseCase: GetVocabularyListUseCase,
    private val addVocabularyItemUseCase: AddVocabularyItemUseCase,
    private val deleteVocabularyItemUseCase: DeleteVocabularyItemUseCase,
    private val ttsPlayer: TtsPlayer
) : ViewModel() {

    private val _state = MutableStateFlow(VocabularyUiState(isLoading = true))
    val state: StateFlow<VocabularyUiState> = _state.asStateFlow()

    val isTtsLoading: StateFlow<Boolean> = ttsPlayer.isLoading

    init {
        loadVocabulary()
    }

    private fun loadVocabulary() {
        viewModelScope.launch {
            getVocabularyListUseCase().collect { list ->
                _state.update { current ->
                    current.copy(
                        items = list,
                        filteredItems = filterList(list, current.selectedCategory),
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onCategorySelected(category: VocabularyCategory?) {
        _state.update { current ->
            val nextCategory = if (current.selectedCategory == category) null else category
            current.copy(
                selectedCategory = nextCategory,
                filteredItems = filterList(current.items, nextCategory)
            )
        }
    }

    fun playTts(text: String, languageCode: String = "en") {
        if (text.isNotBlank()) {
            ttsPlayer.play(text, languageCode)
        }
    }

    fun onOpenAddDialog() {
        _state.update { it.copy(showAddDialog = true) }
    }

    fun onDismissAddDialog() {
        _state.update { it.copy(showAddDialog = false) }
    }

    fun onAddWord(
        word: String,
        partOfSpeech: String,
        phonetic: String,
        meaning: String,
        collocations: List<String>,
        example: String,
        category: VocabularyCategory = VocabularyCategory.GENERAL
    ) {
        if (word.isBlank() || meaning.isBlank()) return
        viewModelScope.launch {
            val newItem = VocabularyItem(
                id = "${kotlin.random.Random.nextLong()}",
                word = word.trim(),
                partOfSpeech = partOfSpeech.trim(),
                phonetic = phonetic.trim(),
                meaning = meaning.trim(),
                collocations = collocations.map { it.trim() }.filter { it.isNotBlank() },
                example = example.trim(),
                category = category,
                createdAt = 0L
            )
            addVocabularyItemUseCase(newItem)
            _state.update { it.copy(showAddDialog = false) }
        }
    }

    fun onDeleteWord(id: String) {
        viewModelScope.launch {
            deleteVocabularyItemUseCase(id)
        }
    }

    private fun filterList(
        list: List<VocabularyItem>,
        category: VocabularyCategory?
    ): List<VocabularyItem> {
        return if (category == null) {
            list
        } else {
            list.filter { it.category == category }
        }
    }
}

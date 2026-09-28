package com.aienglish.feature.vocabulary.data.repository

import com.aienglish.feature.vocabulary.domain.model.VocabularyItem
import com.aienglish.feature.vocabulary.domain.repository.VocabularyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryVocabularyRepository : VocabularyRepository {
    private val _items = MutableStateFlow<List<VocabularyItem>>(
        listOf(
            VocabularyItem(
                id = "1",
                word = "Meticulous",
                phonetic = "/məˈtɪkjələs/",
                meaning = "Tỉ mỉ, cẩn thận, trau chuốt từng chi tiết nhỏ.",
                example = "She was meticulous about keeping her vocabulary notes organized.",
                language = "en",
                createdAt = 1719580000000L
            ),
            VocabularyItem(
                id = "2",
                word = "Serendipity",
                phonetic = "/ˌserənˈdɪpəti/",
                meaning = "Sự tình cờ may mắn, khám phá điều tốt đẹp bất ngờ.",
                example = "Finding this language learning app was pure serendipity.",
                language = "en",
                createdAt = 1719581000000L
            ),
            VocabularyItem(
                id = "3",
                word = "Eloquent",
                phonetic = "/ˈeləkwənt/",
                meaning = "Hùng biện, có tài ăn nói lưu loát và truyền cảm.",
                example = "An eloquent speaker can persuade any audience easily.",
                language = "en",
                createdAt = 1719582000000L
            )
        )
    )

    override fun getVocabularyList(): Flow<List<VocabularyItem>> {
        return _items.asStateFlow()
    }

    override suspend fun addVocabularyItem(item: VocabularyItem) {
        _items.update { current ->
            listOf(item) + current
        }
    }

    override suspend fun updateVocabularyItem(item: VocabularyItem) {
        _items.update { current ->
            current.map { if (it.id == item.id) item else it }
        }
    }

    override suspend fun deleteVocabularyItem(id: String) {
        _items.update { current ->
            current.filterNot { it.id == id }
        }
    }
}

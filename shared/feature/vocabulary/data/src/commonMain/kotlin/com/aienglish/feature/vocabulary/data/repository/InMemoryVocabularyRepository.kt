package com.aienglish.feature.vocabulary.data.repository

import com.aienglish.feature.vocabulary.domain.model.VocabularyCategory
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
                word = "Continuous Integration",
                phonetic = "/kənˈtɪnjuəs ˌɪntɪˈɡreɪʃn/",
                meaning = "Tích hợp liên tục (CI) — tự động kiểm thử và build mã nguồn khi lập trình viên commit code.",
                example = "Our IT team sets up a CI pipeline to deploy bug-free builds automatically.",
                category = VocabularyCategory.IT,
                language = "en",
                createdAt = 1719580000000L
            ),
            VocabularyItem(
                id = "2",
                word = "Omnichannel",
                phonetic = "/ˈɑːmniˌtʃænl/",
                meaning = "Chiến lược tiếp thị đa kênh hợp nhất trải nghiệm khách hàng.",
                example = "The new marketing strategy focuses on an omnichannel customer journey.",
                category = VocabularyCategory.MARKETING,
                language = "en",
                createdAt = 1719581000000L
            ),
            VocabularyItem(
                id = "3",
                word = "Talent Acquisition",
                phonetic = "/ˈtælənt ˌækwɪˈzɪʃn/",
                meaning = "Thu hút và tuyển dụng nhân tài chiến lược cho doanh nghiệp.",
                example = "Our HR department is revamping its talent acquisition process.",
                category = VocabularyCategory.HR,
                language = "en",
                createdAt = 1719582000000L
            ),
            VocabularyItem(
                id = "4",
                word = "Cognitive Bias",
                phonetic = "/ˈkɑːɡnətɪv ˈbaɪəs/",
                meaning = "Thiên kiến nhận thức — lối suy nghĩ sai lệch mang tính hệ thống trong tâm lý học.",
                example = "Psychologists study how cognitive bias influences consumer purchasing decisions.",
                category = VocabularyCategory.PSYCHOLOGY,
                language = "en",
                createdAt = 1719583000000L
            ),
            VocabularyItem(
                id = "5",
                word = "Hydroponics",
                phonetic = "/ˌhaɪdrəˈpɑːnɪks/",
                meaning = "Phương pháp trồng cây thủy canh không cần đất trong nông nghiệp công nghệ cao.",
                example = "Hydroponics is revolutionizing modern urban agriculture.",
                category = VocabularyCategory.AGRICULTURE,
                language = "en",
                createdAt = 1719584000000L
            ),
            VocabularyItem(
                id = "6",
                word = "Appetizer",
                phonetic = "/ˈæpɪtaɪzər/",
                meaning = "Món khai vị trong ẩm thực trước khi dùng món chính.",
                example = "They ordered crispy spring rolls as an appetizer.",
                category = VocabularyCategory.FOOD_BEVERAGE,
                language = "en",
                createdAt = 1719585000000L
            ),
            VocabularyItem(
                id = "7",
                word = "Meticulous",
                phonetic = "/məˈtɪkjələs/",
                meaning = "Tỉ mỉ, cẩn thận, trau chuốt từng chi tiết nhỏ.",
                example = "She was meticulous about keeping her vocabulary notes organized.",
                category = VocabularyCategory.DAILY_LIFE,
                language = "en",
                createdAt = 1719586000000L
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

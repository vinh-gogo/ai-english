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
                partOfSpeech = "noun",
                phonetic = "/kənˈtɪnjuəs ˌɪntɪˈɡreɪʃn/",
                meaning = "Tích hợp liên tục (CI) — tự động kiểm thử và build mã nguồn khi lập trình viên commit code.",
                collocations = listOf("CI pipeline", "seamless integration", "automated testing"),
                example = "Our IT team sets up a CI pipeline to deploy bug-free builds automatically.",
                category = VocabularyCategory.IT,
                language = "en",
                createdAt = 1719580000000L
            ),
            VocabularyItem(
                id = "2",
                word = "Omnichannel",
                partOfSpeech = "adj",
                phonetic = "/ˈɑːmniˌtʃænl/",
                meaning = "Chiến lược tiếp thị đa kênh hợp nhất trải nghiệm khách hàng.",
                collocations = listOf("omnichannel marketing", "customer journey", "omnichannel retailing"),
                example = "The new marketing strategy focuses on an omnichannel customer journey.",
                category = VocabularyCategory.MARKETING,
                language = "en",
                createdAt = 1719581000000L
            ),
            VocabularyItem(
                id = "3",
                word = "Talent Acquisition",
                partOfSpeech = "noun",
                phonetic = "/ˈtælənt ˌækwɪˈzɪʃn/",
                meaning = "Thu hút và tuyển dụng nhân tài chiến lược cho doanh nghiệp.",
                collocations = listOf("talent acquisition specialist", "strategic hiring", "headhunting"),
                example = "Our HR department is revamping its talent acquisition process.",
                category = VocabularyCategory.HR,
                language = "en",
                createdAt = 1719582000000L
            ),
            VocabularyItem(
                id = "4",
                word = "Cognitive Bias",
                partOfSpeech = "noun",
                phonetic = "/ˈkɑːɡnətɪv ˈbaɪəs/",
                meaning = "Thiên kiến nhận thức — lối suy nghĩ sai lệch mang tính hệ thống trong tâm lý học.",
                collocations = listOf("confirmation bias", "unconscious bias", "overcome bias"),
                example = "Psychologists study how cognitive bias influences consumer purchasing decisions.",
                category = VocabularyCategory.PSYCHOLOGY,
                language = "en",
                createdAt = 1719583000000L
            ),
            VocabularyItem(
                id = "5",
                word = "Hydroponics",
                partOfSpeech = "noun",
                phonetic = "/ˌhaɪdrəˈpɑːnɪks/",
                meaning = "Phương pháp trồng cây thủy canh không cần đất trong nông nghiệp công nghệ cao.",
                collocations = listOf("hydroponic farm", "nutrient solution", "vertical farming"),
                example = "Hydroponics is revolutionizing modern urban agriculture.",
                category = VocabularyCategory.AGRICULTURE,
                language = "en",
                createdAt = 1719584000000L
            ),
            VocabularyItem(
                id = "6",
                word = "Culinary",
                partOfSpeech = "adj",
                phonetic = "/ˈkʌlɪneri/",
                meaning = "Liên quan đến ẩm thực, nấu nướng và nghệ thuật chế biến món ăn.",
                collocations = listOf("culinary arts", "culinary skills", "culinary delight"),
                example = "He decided to pursue culinary arts at a prestigious French academy.",
                category = VocabularyCategory.FOOD_BEVERAGE,
                language = "en",
                createdAt = 1719585000000L
            ),
            VocabularyItem(
                id = "7",
                word = "Procrastinate",
                partOfSpeech = "verb",
                phonetic = "/prəˈkræstɪneɪt/",
                meaning = "Trì hoãn, chần chừ việc cần làm trong cuộc sống hàng ngày.",
                collocations = listOf("procrastinate on tasks", "stop procrastinating", "time management"),
                example = "Don't procrastinate when preparing for your upcoming English examination.",
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

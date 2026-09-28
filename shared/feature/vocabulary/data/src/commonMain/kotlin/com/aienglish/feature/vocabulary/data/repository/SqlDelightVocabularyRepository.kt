package com.aienglish.feature.vocabulary.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.core.database.Vocabulary_items
import com.aienglish.feature.vocabulary.domain.model.VocabularyCategory
import com.aienglish.feature.vocabulary.domain.model.VocabularyItem
import com.aienglish.feature.vocabulary.domain.repository.VocabularyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SqlDelightVocabularyRepository(
    private val database: AIEnglishDatabase,
    private val dispatchers: AppDispatchers
) : VocabularyRepository {

    private val queries get() = database.vocabularyQueries

    init {
        seedInitialDataIfNeeded()
    }

    private fun seedInitialDataIfNeeded() {
        try {
            val count = queries.countVocabulary().executeAsOne()
            if (count == 0L) {
                val seedItems = listOf(
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
                        word = "Culinary",
                        phonetic = "/ˈkʌlɪneri/",
                        meaning = "Liên quan đến ẩm thực, nấu nướng và nghệ thuật chế biến món ăn.",
                        example = "He decided to pursue culinary arts at a prestigious academy.",
                        category = VocabularyCategory.FOOD_BEVERAGE,
                        language = "en",
                        createdAt = 1719585000000L
                    ),
                    VocabularyItem(
                        id = "7",
                        word = "Procrastinate",
                        phonetic = "/prəˈkræstɪneɪt/",
                        meaning = "Trì hoãn, chần chừ việc cần làm trong cuộc sống hàng ngày.",
                        example = "Don't procrastinate when preparing for your upcoming exam.",
                        category = VocabularyCategory.DAILY_LIFE,
                        language = "en",
                        createdAt = 1719586000000L
                    )
                )

                database.transaction {
                    for (item in seedItems) {
                        queries.insertVocabulary(
                            id = item.id,
                            userId = null,
                            categoryCode = item.category.code,
                            word = item.word,
                            phonetic = item.phonetic.takeIf { it.isNotBlank() },
                            meaning = item.meaning,
                            example = item.example.takeIf { it.isNotBlank() },
                            note = item.note.takeIf { it.isNotBlank() },
                            language = item.language,
                            createdAt = item.createdAt,
                            updatedAt = null
                        )
                    }
                }
            }
        } catch (e: Exception) {
            println("[SqlDelightVocabularyRepository] Seed error: ${e.message}")
        }
    }

    override fun getVocabularyList(): Flow<List<VocabularyItem>> {
        return queries.getAllVocabulary()
            .asFlow()
            .mapToList(dispatchers.io)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addVocabularyItem(item: VocabularyItem): Unit = withContext(dispatchers.io) {
        queries.insertVocabulary(
            id = item.id,
            userId = null,
            categoryCode = item.category.code,
            word = item.word,
            phonetic = item.phonetic.takeIf { it.isNotBlank() },
            meaning = item.meaning,
            example = item.example.takeIf { it.isNotBlank() },
            note = item.note.takeIf { it.isNotBlank() },
            language = item.language,
            createdAt = item.createdAt,
            updatedAt = null
        )
    }

    override suspend fun updateVocabularyItem(item: VocabularyItem): Unit = withContext(dispatchers.io) {
        queries.updateVocabulary(
            categoryCode = item.category.code,
            word = item.word,
            phonetic = item.phonetic.takeIf { it.isNotBlank() },
            meaning = item.meaning,
            example = item.example.takeIf { it.isNotBlank() },
            note = item.note.takeIf { it.isNotBlank() },
            language = item.language,
            updatedAt = item.createdAt,
            id = item.id
        )
    }

    override suspend fun deleteVocabularyItem(id: String): Unit = withContext(dispatchers.io) {
        queries.deleteVocabulary(id = id)
    }

    private fun Vocabulary_items.toDomain(): VocabularyItem = VocabularyItem(
        id = id,
        word = word,
        phonetic = phonetic ?: "",
        meaning = meaning,
        example = example ?: "",
        note = note ?: "",
        language = language,
        category = VocabularyCategory.fromCode(category_code),
        createdAt = created_at
    )
}

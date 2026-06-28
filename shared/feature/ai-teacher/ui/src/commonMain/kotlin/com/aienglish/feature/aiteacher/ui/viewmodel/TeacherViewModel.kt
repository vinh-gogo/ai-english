package com.aienglish.feature.aiteacher.ui.viewmodel

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.aiteacher.application.usecase.EvaluateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.application.usecase.ExplainSentenceUseCase
import com.aienglish.feature.aiteacher.application.usecase.GenerateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.TopicItem
import com.aienglish.feature.aiteacher.domain.model.getCompatibleLevels
import com.aienglish.feature.aiteacher.domain.repository.TopicRepository
import com.aienglish.feature.aiteacher.domain.repository.TeacherHistoryRepository
import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

enum class LevelType {
    CEFR, TOEIC, JLPT, HSK
}

data class TeacherUiState(
    // Topic selection
    val topics: List<TopicItem> = emptyList(),
    val selectedTopicIndex: Int = 0,
    val topicSearchQuery: String = "",
    val levelType: LevelType = LevelType.CEFR,
    val selectedCefrLevel: String = "B1",
    val selectedToeicLevel: String = "500",
    val selectedJlptLevel: String = "N3",
    val selectedHskLevel: String = "HSK3",
    val targetLanguage: String = "en",
    val isCustomTopicMode: Boolean = false,
    val customTopicInput: String = "",

    // Lesson state
    val isGeneratingLesson: Boolean = false,
    val lesson: EnglishLesson? = null,
    val userAnswers: Map<Int, String> = emptyMap(),      // idx -> typed answer
    val checkedAnswers: Set<Int> = emptySet(),           // idx -> checked (submitted per sentence)
    val currentSentenceIndex: Int = 0,

    // Evaluation
    val isEvaluating: Boolean = false,
    val feedback: EnglishFeedback? = null,

    // Explanation
    val explainingSentenceIndex: Int? = null,           // tracks which sentence index is currently loading its AI explanation
    val explanations: Map<Int, String> = emptyMap(),

    // Error
    val error: String? = null
) {
    val selectedTopic: String get() = if (isCustomTopicMode) customTopicInput else topics.getOrNull(selectedTopicIndex)?.name ?: ""
    val selectedLevelText: String get() = when (levelType) {
        LevelType.CEFR -> selectedCefrLevel
        LevelType.TOEIC -> "TOEIC $selectedToeicLevel"
        LevelType.JLPT -> selectedJlptLevel
        LevelType.HSK -> selectedHskLevel
    }
}

class TeacherViewModel(
    private val generateLesson: GenerateEnglishLessonUseCase,
    private val evaluateLesson: EvaluateEnglishLessonUseCase,
    private val explainSentence: ExplainSentenceUseCase,
    private val teacherHistoryRepository: TeacherHistoryRepository,
    private val topicRepository: TopicRepository,
    private val jsonResourceReader: JsonResourceReader,
    private val prefs: AppPreferences,
    private val historyRepository: HistoryRepository
) : BaseViewModel<TeacherUiState>(TeacherUiState()) {

    // Track current lesson DB id for history update
    private var currentLessonHistoryId: Long? = null
    private var loadTopicsJob: Job? = null

    init {
        val target = prefs.getLanguageTarget() // "en", "ja", "zh"
        val expectedType = when (target) {
            "ja" -> LevelType.JLPT
            "zh" -> LevelType.HSK
            else -> LevelType.CEFR
        }
        val expectedLevel = when (expectedType) {
            LevelType.JLPT -> currentState.selectedJlptLevel
            LevelType.HSK -> currentState.selectedHskLevel
            else -> currentState.selectedCefrLevel
        }
        setState {
            copy(
                targetLanguage = target,
                levelType = expectedType
            )
        }
        loadTopics(expectedType, expectedLevel)

        // Observe target language changes reactively
        viewModelScope.launch {
            prefs.languageTargetFlow.collect { newTarget ->
                syncTargetLanguage(newTarget)
            }
        }
    }

    private suspend fun ensureDatabaseSeeded() {
        val currentTopics = topicRepository.getTopics("CEFR", listOf("A1"))
        val currentJapaneseTopics = topicRepository.getTopics("JLPT", listOf("N5"))
        val currentChineseTopics = topicRepository.getTopics("HSK", listOf("HSK1"))
        val currentToeicTopics = topicRepository.getTopics("TOEIC", listOf("300"))
        if (currentTopics.isEmpty() || currentJapaneseTopics.size < 120 || currentChineseTopics.isEmpty() || currentToeicTopics.isEmpty()) {
            val systemsStr = jsonResourceReader.readSystemsJson()
            val levelsStr = jsonResourceReader.readLevelsJson()
            val englishStr = jsonResourceReader.readEnglishTopicsJson()
            val japaneseStr = jsonResourceReader.readJapaneseTopicsJson()
            val chineseStr = jsonResourceReader.readChineseTopicsJson()
            topicRepository.seedDatabase(systemsStr, levelsStr, englishStr, japaneseStr, chineseStr)
        }
    }

    private fun loadTopics(levelType: LevelType, levelCode: String) {
        val systemCode = when (levelType) {
            LevelType.CEFR -> "CEFR"
            LevelType.TOEIC -> "TOEIC"
            LevelType.JLPT -> "JLPT"
            LevelType.HSK -> "HSK"
        }
        loadTopicsJob?.cancel()
        loadTopicsJob = viewModelScope.launch {
            try {
                ensureDatabaseSeeded()

                val compatible = getCompatibleLevels(levelCode)
                val list = topicRepository.getTopics(systemCode, compatible)
                setState {
                    copy(
                        topics = list,
                        selectedTopicIndex = 0
                    )
                }
            } catch (e: Exception) {
                setState { copy(error = "Không thể tải chủ đề từ database: ${e.message}") }
            }
        }
    }

    private fun syncTargetLanguage(target: String) {
        val currentTarget = currentState.targetLanguage
        if (target != currentTarget) {
            val expectedType = when (target) {
                "ja" -> LevelType.JLPT
                "zh" -> LevelType.HSK
                else -> LevelType.CEFR
            }
            val expectedLevel = when (expectedType) {
                LevelType.JLPT -> currentState.selectedJlptLevel
                LevelType.HSK -> currentState.selectedHskLevel
                else -> currentState.selectedCefrLevel
            }
            setState {
                copy(
                    targetLanguage = target,
                    levelType = expectedType
                )
            }
            loadTopics(expectedType, expectedLevel)
        }
    }

    // ── Topic Selection ──
    fun onTopicIndexSelected(index: Int) = setState { copy(selectedTopicIndex = index) }
    fun onTopicSearchChange(query: String) = setState { copy(topicSearchQuery = query) }
    
    fun onLevelTypeSelected(type: LevelType) {
        val activeLevel = when (type) {
            LevelType.CEFR -> currentState.selectedCefrLevel
            LevelType.TOEIC -> currentState.selectedToeicLevel
            LevelType.JLPT -> currentState.selectedJlptLevel
            LevelType.HSK -> currentState.selectedHskLevel
        }
        setState { copy(levelType = type) }
        loadTopics(type, activeLevel)
    }

    fun onHskLevelSelected(level: String) {
        setState { copy(selectedHskLevel = level) }
        loadTopics(LevelType.HSK, level)
    }

    fun onCefrLevelSelected(level: String) {
        setState { copy(selectedCefrLevel = level) }
        loadTopics(LevelType.CEFR, level)
    }

    fun onToeicLevelSelected(level: String) {
        setState { copy(selectedToeicLevel = level) }
        loadTopics(LevelType.TOEIC, level)
    }

    fun onJlptLevelSelected(level: String) {
        setState { copy(selectedJlptLevel = level) }
        loadTopics(LevelType.JLPT, level)
    }

    // ── Lesson Navigation ──
    fun onAnswerChanged(index: Int, answer: String) {
        setState {
            copy(userAnswers = userAnswers + (index to answer))
        }
        saveCurrentProgress()
    }

    fun onCheckAnswer(index: Int) {
        val state = currentState
        val lesson = state.lesson ?: return
        val level = state.selectedLevelText
        val sentence = lesson.sentences.getOrNull(index) ?: return
        val userAnswer = state.userAnswers[index] ?: ""

        setState {
            copy(
                checkedAnswers = checkedAnswers + index,
                explainingSentenceIndex = index,
                error = null
            )
        }

        viewModelScope.launch {
            explainSentence("user", sentence.vietnamese, sentence.english, userAnswer, level).fold(
                ifLeft = { err ->
                    setState {
                        copy(
                            explainingSentenceIndex = null,
                            error = "Không thể tải giải thích từ AI: ${err.message}"
                        )
                    }
                },
                ifRight = { explanation ->
                    setState {
                        copy(
                            explainingSentenceIndex = null,
                            explanations = explanations + (index to explanation)
                        )
                    }
                    saveCurrentProgress()
                }
            )
        }
    }

    fun onNextSentence() = setState {
        val next = (currentSentenceIndex + 1).coerceAtMost((lesson?.sentences?.size ?: 1) - 1)
        copy(currentSentenceIndex = next)
    }

    // ── Start Lesson ──
    fun onStartLesson() {
        val topic = currentState.selectedTopic
        val level = currentState.selectedLevelText
        val isGuest = prefs.getUserName() == "Khách"

        viewModelScope.launch {
            try {
                val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                val nowLocal = Instant.fromEpochMilliseconds(now).toLocalDateTime(TimeZone.currentSystemDefault())
                val currentYear = nowLocal.year
                val currentMonth = nowLocal.monthNumber

                val historyList = historyRepository.getAllHistory().first()
                val monthlyUsage = historyList.count { item ->
                    val itemLocal = Instant.fromEpochMilliseconds(item.createdAt).toLocalDateTime(TimeZone.currentSystemDefault())
                    itemLocal.year == currentYear && itemLocal.monthNumber == currentMonth
                }

                val hasCustomApiKey = prefs.getApiKey().trim().isNotEmpty()
                val isPaid = prefs.isPaid()
                val limit = if (isPaid) 99999 else (if (hasCustomApiKey) 20 else 5)

                if (monthlyUsage >= limit) {
                    val limitMsg = if (isGuest) {
                        "Chế độ dùng thử tối đa 5 bài học/tháng. Vui lòng đăng nhập để tiếp tục!"
                    } else if (!hasCustomApiKey) {
                        "Tài khoản chưa cấu hình API Key chỉ được tối đa 5 bài học/tháng. Vui lòng cấu hình API Key trong phần Profile để có 20 bài học/tháng!"
                    } else {
                        "Hạn ngạch sử dụng API Key tối đa 20 bài học/tháng đã hết!"
                    }
                    setState { copy(error = limitMsg) }
                    return@launch
                }
            } catch (e: Exception) {
                // Ignore
            }

            setState {
                copy(
                    isGeneratingLesson = true,
                    error = null,
                    lesson = null,
                    feedback = null,
                    userAnswers = emptyMap(),
                    checkedAnswers = emptySet(),
                    currentSentenceIndex = 0,
                    explanations = emptyMap()
                )
            }

            // Save to history DB (IN_PROGRESS)
            currentLessonHistoryId = try {
                teacherHistoryRepository.startLesson(topic, level)
            } catch (e: Exception) { null }

            generateLesson("user", topic, level).fold(
                ifLeft = { err -> setState { copy(isGeneratingLesson = false, error = err.message) } },
                ifRight = { lesson ->
                    setState { copy(isGeneratingLesson = false, lesson = lesson) }
                    saveCurrentProgress()
                }
            )
        }
    }

    // ── Submit for Evaluation ──
    fun onSubmitLesson() {
        val state = currentState
        val lesson = state.lesson ?: return
        val level = state.selectedLevelText
        val answers = lesson.sentences.mapIndexed { i, s -> (state.userAnswers[i] ?: "") to s.english }
        val sentences = lesson.sentences.map { it.vietnamese }
        setState { copy(isEvaluating = true, error = null) }
        viewModelScope.launch {
            evaluateLesson("user", state.selectedTopic, answers, sentences, level).fold(
                ifLeft = { err -> setState { copy(isEvaluating = false, error = err.message) } },
                ifRight = { fb ->
                    setState { copy(isEvaluating = false, feedback = fb) }
                    // Update history to COMPLETED with score and full snapshot
                    currentLessonHistoryId?.let { id ->
                        try {
                            teacherHistoryRepository.completeLesson(
                                id = id,
                                score = fb.score,
                                lesson = lesson,
                                userAnswers = state.userAnswers,
                                explanations = state.explanations,
                                feedback = fb
                            )
                        } catch (e: Exception) { /* ignore */ }
                    }
                }
            )
        }
    }

    fun onExplainSentence(index: Int) {
        val state = currentState
        if (state.explanations.containsKey(index)) return

        val lesson = state.lesson ?: return
        val level = state.selectedLevelText
        val sentence = lesson.sentences.getOrNull(index) ?: return
        val userAnswer = state.userAnswers[index] ?: ""

        setState {
            copy(
                explainingSentenceIndex = index,
                error = null
            )
        }

        viewModelScope.launch {
            explainSentence("user", sentence.vietnamese, sentence.english, userAnswer, level).fold(
                ifLeft = { err ->
                    setState {
                        copy(
                            explainingSentenceIndex = null,
                            error = "Không thể tải giải thích từ AI: ${err.message}"
                        )
                    }
                },
                ifRight = { explanation ->
                    setState {
                        copy(
                            explainingSentenceIndex = null,
                            explanations = explanations + (index to explanation)
                        )
                    }
                    saveCurrentProgress()
                }
            )
        }
    }

    fun onReviewLesson(history: LessonHistory) {
        viewModelScope.launch {
            try {
                ensureDatabaseSeeded()

                val isToeic = history.level.startsWith("TOEIC")
                val isJlpt = history.level.startsWith("N")
                val isHsk = history.level.startsWith("HSK")
                val expectedType = if (isToeic) LevelType.TOEIC else if (isJlpt) LevelType.JLPT else if (isHsk) LevelType.HSK else LevelType.CEFR
                val levelValue = if (isToeic) history.level.removePrefix("TOEIC ").trim() else history.level
                
                val systemCode = when (expectedType) {
                    LevelType.CEFR -> "CEFR"
                    LevelType.TOEIC -> "TOEIC"
                    LevelType.JLPT -> "JLPT"
                    LevelType.HSK -> "HSK"
                }
                
                val compatible = getCompatibleLevels(levelValue)
                val list = topicRepository.getTopics(systemCode, compatible)
                val topicIndex = list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)

                val teacherHistory = teacherHistoryRepository.parseHistory(
                    id = history.id,
                    topic = history.topic,
                    level = history.level,
                    score = history.score,
                    lessonJson = history.lessonJson,
                    userAnswersJson = history.userAnswersJson,
                    explanationsJson = history.explanationsJson,
                    feedbackJson = history.feedbackJson
                )

                setState {
                    copy(
                        topics = list,
                        selectedTopicIndex = topicIndex,
                        levelType = expectedType,
                        selectedCefrLevel = if (expectedType == LevelType.CEFR) levelValue else selectedCefrLevel,
                        selectedToeicLevel = if (expectedType == LevelType.TOEIC) levelValue else selectedToeicLevel,
                        selectedJlptLevel = if (expectedType == LevelType.JLPT) levelValue else selectedJlptLevel,
                        selectedHskLevel = if (expectedType == LevelType.HSK) levelValue else selectedHskLevel,
                        lesson = teacherHistory.lesson,
                        userAnswers = teacherHistory.userAnswers,
                        explanations = teacherHistory.explanations,
                        feedback = teacherHistory.feedback,
                        currentSentenceIndex = 0,
                        checkedAnswers = teacherHistory.userAnswers.keys
                    )
                }
            } catch (e: Exception) {
                setState { copy(error = "Không thể tải lại lịch sử bài học: ${e.message}") }
            }
        }
    }

    fun onReTestLesson(history: LessonHistory) {
        val isToeic = history.level.startsWith("TOEIC")
        val isJlpt = history.level.startsWith("N")
        val isHsk = history.level.startsWith("HSK")
        val expectedType = if (isToeic) LevelType.TOEIC else if (isJlpt) LevelType.JLPT else if (isHsk) LevelType.HSK else LevelType.CEFR
        val levelValue = if (isToeic) history.level.removePrefix("TOEIC ").trim() else history.level
        
        viewModelScope.launch {
            try {
                ensureDatabaseSeeded()

                val systemCode = when (expectedType) {
                    LevelType.CEFR -> "CEFR"
                    LevelType.TOEIC -> "TOEIC"
                    LevelType.JLPT -> "JLPT"
                    LevelType.HSK -> "HSK"
                }

                val compatible = getCompatibleLevels(levelValue)
                val list = topicRepository.getTopics(systemCode, compatible)
                val topicIndex = list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)

                setState {
                    copy(
                        topics = list,
                        selectedTopicIndex = topicIndex,
                        levelType = expectedType,
                        selectedCefrLevel = if (expectedType == LevelType.CEFR) levelValue else selectedCefrLevel,
                        selectedToeicLevel = if (expectedType == LevelType.TOEIC) levelValue else selectedToeicLevel,
                        selectedJlptLevel = if (expectedType == LevelType.JLPT) levelValue else selectedJlptLevel,
                        selectedHskLevel = if (expectedType == LevelType.HSK) levelValue else selectedHskLevel
                    )
                }
                onStartLesson()
            } catch (e: Exception) {
                setState { copy(error = "Không thể thi lại bài học: ${e.message}") }
            }
        }
    }

    fun onDismissError() = setState { copy(error = null) }
    fun onReset() = setState { TeacherUiState() }

    private fun saveCurrentProgress() {
        val id = currentLessonHistoryId ?: return
        val lesson = currentState.lesson ?: return
        viewModelScope.launch {
            try {
                teacherHistoryRepository.updateLessonProgress(
                    id = id,
                    lesson = lesson,
                    userAnswers = currentState.userAnswers,
                    explanations = currentState.explanations
                )
            } catch (e: Exception) { /* ignore */ }
        }
    }

    fun onResumeLesson(history: LessonHistory) {
        viewModelScope.launch {
            try {
                ensureDatabaseSeeded()

                val isToeic = history.level.startsWith("TOEIC")
                val isJlpt = history.level.startsWith("N")
                val isHsk = history.level.startsWith("HSK")
                val expectedType = if (isToeic) LevelType.TOEIC else if (isJlpt) LevelType.JLPT else if (isHsk) LevelType.HSK else LevelType.CEFR
                val levelValue = if (isToeic) history.level.removePrefix("TOEIC ").trim() else history.level
                
                val systemCode = when (expectedType) {
                    LevelType.CEFR -> "CEFR"
                    LevelType.TOEIC -> "TOEIC"
                    LevelType.JLPT -> "JLPT"
                    LevelType.HSK -> "HSK"
                }

                val compatible = getCompatibleLevels(levelValue)
                val list = topicRepository.getTopics(systemCode, compatible)
                val topicIndex = list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)

                val teacherHistory = teacherHistoryRepository.parseHistory(
                    id = history.id,
                    topic = history.topic,
                    level = history.level,
                    score = history.score,
                    lessonJson = history.lessonJson,
                    userAnswersJson = history.userAnswersJson,
                    explanationsJson = history.explanationsJson,
                    feedbackJson = history.feedbackJson
                )

                currentLessonHistoryId = history.id

                setState {
                    copy(
                        topics = list,
                        selectedTopicIndex = topicIndex,
                        levelType = expectedType,
                        selectedCefrLevel = if (expectedType == LevelType.CEFR) levelValue else selectedCefrLevel,
                        selectedToeicLevel = if (expectedType == LevelType.TOEIC) levelValue else selectedToeicLevel,
                        selectedJlptLevel = if (expectedType == LevelType.JLPT) levelValue else selectedJlptLevel,
                        selectedHskLevel = if (expectedType == LevelType.HSK) levelValue else selectedHskLevel,
                        lesson = teacherHistory.lesson,
                        userAnswers = teacherHistory.userAnswers,
                        explanations = teacherHistory.explanations,
                        feedback = null,
                        currentSentenceIndex = 0,
                        checkedAnswers = teacherHistory.explanations.keys
                    )
                }
            } catch (e: Exception) {
                setState { copy(error = "Không thể tiếp tục bài học dở dang: ${e.message}") }
            }
        }
    }

    fun setCustomTopicMode(enabled: Boolean) = setState { copy(isCustomTopicMode = enabled) }
    fun onCustomTopicInputChanged(input: String) = setState { copy(customTopicInput = input) }

    fun fetchMonthlyUsage(onResult: (usage: Int, limit: Int) -> Unit) {
        viewModelScope.launch {
            try {
                val now = kotlinx.datetime.Clock.System.now().toEpochMilliseconds()
                val nowLocal = Instant.fromEpochMilliseconds(now).toLocalDateTime(TimeZone.currentSystemDefault())
                val currentYear = nowLocal.year
                val currentMonth = nowLocal.monthNumber

                val historyList = historyRepository.getAllHistory().first()
                val monthlyUsage = historyList.count { item ->
                    val itemLocal = Instant.fromEpochMilliseconds(item.createdAt).toLocalDateTime(TimeZone.currentSystemDefault())
                    itemLocal.year == currentYear && itemLocal.monthNumber == currentMonth
                }

                val hasCustomApiKey = prefs.getApiKey().trim().isNotEmpty()
                val isPaid = prefs.isPaid()
                val limit = if (isPaid) 99999 else (if (hasCustomApiKey) 20 else 5)
                onResult(monthlyUsage, limit)
            } catch (e: Exception) {
                onResult(0, 5)
            }
        }
    }
}

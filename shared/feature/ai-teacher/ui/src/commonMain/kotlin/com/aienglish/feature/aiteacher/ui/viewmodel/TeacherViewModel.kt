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
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

enum class LevelType {
    CEFR, TOEIC, JLPT, HSK, DELE, SIELE, HINDI, DELF, DALF, GOETHE, TESTDAF, TOPIK
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
    val selectedDeleLevel: String = "B1",
    val selectedSieleLevel: String = "B1",
    val selectedHindiLevel: String = "B1",
    val selectedDelfLevel: String = "B1",
    val selectedDalfLevel: String = "C1",
    val selectedGoetheLevel: String = "B1",
    val selectedTestdafLevel: String = "TDN4",
    val selectedTopikLevel: String = "TOPIK3",
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
    val sentenceScores: Map<Int, Int> = emptyMap(),

    // Error
    val error: String? = null
) {
    val selectedTopic: String get() = if (isCustomTopicMode) customTopicInput else topics.getOrNull(selectedTopicIndex)?.name ?: ""
    val selectedLevelText: String get() = when (levelType) {
        LevelType.CEFR -> selectedCefrLevel
        LevelType.TOEIC -> "TOEIC $selectedToeicLevel"
        LevelType.JLPT -> selectedJlptLevel
        LevelType.HSK -> selectedHskLevel
        LevelType.DELE -> "DELE $selectedDeleLevel"
        LevelType.SIELE -> "SIELE $selectedSieleLevel"
        LevelType.HINDI -> "Hindi $selectedHindiLevel"
        LevelType.DELF -> "DELF $selectedDelfLevel"
        LevelType.DALF -> "DALF $selectedDalfLevel"
        LevelType.GOETHE -> "Goethe $selectedGoetheLevel"
        LevelType.TESTDAF -> "TestDaF $selectedTestdafLevel"
        LevelType.TOPIK -> selectedTopikLevel
    }
}

class TeacherViewModel(
    private val generateLesson: GenerateEnglishLessonUseCase,
    private val evaluateLesson: EvaluateEnglishLessonUseCase,
    private val explainSentence: ExplainSentenceUseCase,
    private val teacherHistoryRepository: TeacherHistoryRepository,
    private val topicRepository: TopicRepository,
    private val prefs: AppPreferences,
    private val historyRepository: HistoryRepository
) : BaseViewModel<TeacherUiState>(TeacherUiState()) {

    // Track current lesson DB id for history update
    private var currentLessonHistoryId: Long? = null
    private var loadTopicsJob: Job? = null

    init {
        val target = prefs.getLanguageTarget() // "en", "ja", "zh", "es", "hi", "fr", "de", "ko"
        val expectedType = when (target) {
            "ja" -> LevelType.JLPT
            "zh" -> LevelType.HSK
            "es" -> LevelType.DELE
            "hi" -> LevelType.HINDI
            "fr" -> LevelType.DELF
            "de" -> LevelType.GOETHE
            "ko" -> LevelType.TOPIK
            else -> LevelType.CEFR
        }
        val expectedLevel = when (expectedType) {
            LevelType.JLPT -> currentState.selectedJlptLevel
            LevelType.HSK -> currentState.selectedHskLevel
            LevelType.DELE -> currentState.selectedDeleLevel
            LevelType.SIELE -> currentState.selectedSieleLevel
            LevelType.HINDI -> currentState.selectedHindiLevel
            LevelType.DELF -> currentState.selectedDelfLevel
            LevelType.DALF -> currentState.selectedDalfLevel
            LevelType.GOETHE -> currentState.selectedGoetheLevel
            LevelType.TESTDAF -> currentState.selectedTestdafLevel
            LevelType.TOPIK -> currentState.selectedTopikLevel
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
        val currentDeleTopics = topicRepository.getTopics("DELE", listOf("A1"))
        val currentHindiTopics = topicRepository.getTopics("HINDI", listOf("A1"))
        val currentFrenchTopics = topicRepository.getTopics("DELF", listOf("A1"))
        val currentGermanTopics = topicRepository.getTopics("GOETHE", listOf("A1"))
        val currentKoreanTopics = topicRepository.getTopics("TOPIK", listOf("TOPIK1"))
        if (currentTopics.isEmpty() || currentJapaneseTopics.size < 120 || currentChineseTopics.isEmpty() || 
            currentToeicTopics.isEmpty() || currentDeleTopics.isEmpty() || currentHindiTopics.isEmpty() ||
            currentFrenchTopics.isEmpty() || currentGermanTopics.isEmpty() || currentKoreanTopics.isEmpty()) {
            topicRepository.seedDatabase()
        }
    }

    private fun loadTopics(levelType: LevelType, levelCode: String) {
        val systemCode = when (levelType) {
            LevelType.CEFR -> "CEFR"
            LevelType.TOEIC -> "TOEIC"
            LevelType.JLPT -> "JLPT"
            LevelType.HSK -> "HSK"
            LevelType.DELE -> "DELE"
            LevelType.SIELE -> "SIELE"
            LevelType.HINDI -> "HINDI"
            LevelType.DELF -> "DELF"
            LevelType.DALF -> "DALF"
            LevelType.GOETHE -> "GOETHE"
            LevelType.TESTDAF -> "TESTDAF"
            LevelType.TOPIK -> "TOPIK"
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
                "es" -> LevelType.DELE
                "hi" -> LevelType.HINDI
                "fr" -> LevelType.DELF
                "de" -> LevelType.GOETHE
                "ko" -> LevelType.TOPIK
                else -> LevelType.CEFR
            }
            val expectedLevel = when (expectedType) {
                LevelType.JLPT -> currentState.selectedJlptLevel
                LevelType.HSK -> currentState.selectedHskLevel
                LevelType.DELE -> currentState.selectedDeleLevel
                LevelType.SIELE -> currentState.selectedSieleLevel
                LevelType.HINDI -> currentState.selectedHindiLevel
                LevelType.DELF -> currentState.selectedDelfLevel
                LevelType.DALF -> currentState.selectedDalfLevel
                LevelType.GOETHE -> currentState.selectedGoetheLevel
                LevelType.TESTDAF -> currentState.selectedTestdafLevel
                LevelType.TOPIK -> currentState.selectedTopikLevel
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
            LevelType.DELE -> currentState.selectedDeleLevel
            LevelType.SIELE -> currentState.selectedSieleLevel
            LevelType.HINDI -> currentState.selectedHindiLevel
            LevelType.DELF -> currentState.selectedDelfLevel
            LevelType.DALF -> currentState.selectedDalfLevel
            LevelType.GOETHE -> currentState.selectedGoetheLevel
            LevelType.TESTDAF -> currentState.selectedTestdafLevel
            LevelType.TOPIK -> currentState.selectedTopikLevel
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

    fun onDeleLevelSelected(level: String) {
        setState { copy(selectedDeleLevel = level) }
        loadTopics(LevelType.DELE, level)
    }

    fun onSieleLevelSelected(level: String) {
        setState { copy(selectedSieleLevel = level) }
        loadTopics(LevelType.SIELE, level)
    }

    fun onHindiLevelSelected(level: String) {
        setState { copy(selectedHindiLevel = level) }
        loadTopics(LevelType.HINDI, level)
    }

    fun onDelfLevelSelected(level: String) {
        setState { copy(selectedDelfLevel = level) }
        loadTopics(LevelType.DELF, level)
    }

    fun onDalfLevelSelected(level: String) {
        setState { copy(selectedDalfLevel = level) }
        loadTopics(LevelType.DALF, level)
    }

    fun onGoetheLevelSelected(level: String) {
        setState { copy(selectedGoetheLevel = level) }
        loadTopics(LevelType.GOETHE, level)
    }

    fun onTestdafLevelSelected(level: String) {
        setState { copy(selectedTestdafLevel = level) }
        loadTopics(LevelType.TESTDAF, level)
    }

    fun onTopikLevelSelected(level: String) {
        setState { copy(selectedTopikLevel = level) }
        loadTopics(LevelType.TOPIK, level)
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
                ifRight = { result ->
                    setState {
                        copy(
                            explainingSentenceIndex = null,
                            explanations = explanations + (index to result.explanation),
                            sentenceScores = sentenceScores + (index to result.score)
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
                    explanations = emptyMap(),
                    sentenceScores = emptyMap()
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
                ifRight = { result ->
                    setState {
                        copy(
                            explainingSentenceIndex = null,
                            explanations = explanations + (index to result.explanation),
                            sentenceScores = sentenceScores + (index to result.score)
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

                val (expectedType, levelValue) = parseLevelTypeAndValue(history.level)
                
                val systemCode = when (expectedType) {
                    LevelType.CEFR -> "CEFR"
                    LevelType.TOEIC -> "TOEIC"
                    LevelType.JLPT -> "JLPT"
                    LevelType.HSK -> "HSK"
                    LevelType.DELE -> "DELE"
                    LevelType.SIELE -> "SIELE"
                    LevelType.HINDI -> "HINDI"
                    LevelType.DELF -> "DELF"
                    LevelType.DALF -> "DALF"
                    LevelType.GOETHE -> "GOETHE"
                    LevelType.TESTDAF -> "TESTDAF"
                    LevelType.TOPIK -> "TOPIK"
                }
                
                val compatible = getCompatibleLevels(levelValue)
                val list = topicRepository.getTopics(systemCode, compatible)
                val topicIndex = list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)

                val teacherHistory = teacherHistoryRepository.parseHistory(
                    id = history.id,
                    topic = history.topic,
                    level = history.level,
                    score = history.score,
                    lessonJson = history.lessonData,
                    userAnswersJson = history.userAnswersData,
                    explanationsJson = history.explanationsData,
                    feedbackJson = history.feedbackData
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
                        selectedDeleLevel = if (expectedType == LevelType.DELE) levelValue else selectedDeleLevel,
                        selectedSieleLevel = if (expectedType == LevelType.SIELE) levelValue else selectedSieleLevel,
                        selectedHindiLevel = if (expectedType == LevelType.HINDI) levelValue else selectedHindiLevel,
                        selectedDelfLevel = if (expectedType == LevelType.DELF) levelValue else selectedDelfLevel,
                        selectedDalfLevel = if (expectedType == LevelType.DALF) levelValue else selectedDalfLevel,
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
        val (expectedType, levelValue) = parseLevelTypeAndValue(history.level)
        
        viewModelScope.launch {
            try {
                ensureDatabaseSeeded()

                val systemCode = when (expectedType) {
                    LevelType.CEFR -> "CEFR"
                    LevelType.TOEIC -> "TOEIC"
                    LevelType.JLPT -> "JLPT"
                    LevelType.HSK -> "HSK"
                    LevelType.DELE -> "DELE"
                    LevelType.SIELE -> "SIELE"
                    LevelType.HINDI -> "HINDI"
                    LevelType.DELF -> "DELF"
                    LevelType.DALF -> "DALF"
                    LevelType.GOETHE -> "GOETHE"
                    LevelType.TESTDAF -> "TESTDAF"
                    LevelType.TOPIK -> "TOPIK"
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
                        selectedHskLevel = if (expectedType == LevelType.HSK) levelValue else selectedHskLevel,
                        selectedDeleLevel = if (expectedType == LevelType.DELE) levelValue else selectedDeleLevel,
                        selectedSieleLevel = if (expectedType == LevelType.SIELE) levelValue else selectedSieleLevel,
                        selectedHindiLevel = if (expectedType == LevelType.HINDI) levelValue else selectedHindiLevel,
                        selectedDelfLevel = if (expectedType == LevelType.DELF) levelValue else selectedDelfLevel,
                        selectedDalfLevel = if (expectedType == LevelType.DALF) levelValue else selectedDalfLevel
                    )
                }
                onStartLesson()
            } catch (e: Exception) {
                setState { copy(error = "Không thể thi lại bài học: ${e.message}") }
            }
        }
    }

    fun onDismissError() = setState { copy(error = null) }
    fun onReset() {
        val target = prefs.getLanguageTarget()
        val expectedType = when (target) {
            "ja" -> LevelType.JLPT
            "zh" -> LevelType.HSK
            "es" -> LevelType.DELE
            "hi" -> LevelType.HINDI
            "fr" -> LevelType.DELF
            else -> LevelType.CEFR
        }
        val expectedLevel = when (expectedType) {
            LevelType.JLPT -> currentState.selectedJlptLevel
            LevelType.HSK -> currentState.selectedHskLevel
            LevelType.DELE -> currentState.selectedDeleLevel
            LevelType.SIELE -> currentState.selectedSieleLevel
            LevelType.HINDI -> currentState.selectedHindiLevel
            LevelType.DELF -> currentState.selectedDelfLevel
            LevelType.DALF -> currentState.selectedDalfLevel
            else -> currentState.selectedCefrLevel
        }
        currentLessonHistoryId = null
        setState {
            copy(
                targetLanguage = target,
                levelType = expectedType,
                lesson = null,
                userAnswers = emptyMap(),
                checkedAnswers = emptySet(),
                currentSentenceIndex = 0,
                feedback = null,
                isGeneratingLesson = false,
                error = null
            )
        }
        loadTopics(expectedType, expectedLevel)
    }

    private fun parseLevelTypeAndValue(level: String): Pair<LevelType, String> {
        val isToeic = level.startsWith("TOEIC")
        val isJlpt = level.startsWith("N")
        val isHsk = level.startsWith("HSK")
        val isDele = level.startsWith("DELE")
        val isSiele = level.startsWith("SIELE")
        val isHindi = level.startsWith("Hindi")
        val isDelf = level.startsWith("DELF")
        val isDalf = level.startsWith("DALF")
        val isGoethe = level.startsWith("Goethe")
        val isTestdaf = level.startsWith("TestDaF")
        val isTopik = level.startsWith("TOPIK")
        
        val expectedType = when {
            isToeic -> LevelType.TOEIC
            isJlpt -> LevelType.JLPT
            isHsk -> LevelType.HSK
            isDele -> LevelType.DELE
            isSiele -> LevelType.SIELE
            isHindi -> LevelType.HINDI
            isDelf -> LevelType.DELF
            isDalf -> LevelType.DALF
            isGoethe -> LevelType.GOETHE
            isTestdaf -> LevelType.TESTDAF
            isTopik -> LevelType.TOPIK
            else -> LevelType.CEFR
        }
        val levelValue = when {
            isToeic -> level.removePrefix("TOEIC ").trim()
            isDele -> level.removePrefix("DELE ").trim()
            isSiele -> level.removePrefix("SIELE ").trim()
            isHindi -> level.removePrefix("Hindi ").trim()
            isDelf -> level.removePrefix("DELF ").trim()
            isDalf -> level.removePrefix("DALF ").trim()
            isGoethe -> level.removePrefix("Goethe ").trim()
            isTestdaf -> level.removePrefix("TestDaF ").trim()
            else -> level
        }
        return expectedType to levelValue
    }

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

                val (expectedType, levelValue) = parseLevelTypeAndValue(history.level)
                
                val systemCode = when (expectedType) {
                    LevelType.CEFR -> "CEFR"
                    LevelType.TOEIC -> "TOEIC"
                    LevelType.JLPT -> "JLPT"
                    LevelType.HSK -> "HSK"
                    LevelType.DELE -> "DELE"
                    LevelType.SIELE -> "SIELE"
                    LevelType.HINDI -> "HINDI"
                    LevelType.DELF -> "DELF"
                    LevelType.DALF -> "DALF"
                    LevelType.GOETHE -> "GOETHE"
                    LevelType.TESTDAF -> "TESTDAF"
                    LevelType.TOPIK -> "TOPIK"
                }

                val compatible = getCompatibleLevels(levelValue)
                val list = topicRepository.getTopics(systemCode, compatible)
                val topicIndex = list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)

                val teacherHistory = teacherHistoryRepository.parseHistory(
                    id = history.id,
                    topic = history.topic,
                    level = history.level,
                    score = history.score,
                    lessonJson = history.lessonData,
                    userAnswersJson = history.userAnswersData,
                    explanationsJson = history.explanationsData,
                    feedbackJson = history.feedbackData
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
                        selectedDeleLevel = if (expectedType == LevelType.DELE) levelValue else selectedDeleLevel,
                        selectedSieleLevel = if (expectedType == LevelType.SIELE) levelValue else selectedSieleLevel,
                        selectedHindiLevel = if (expectedType == LevelType.HINDI) levelValue else selectedHindiLevel,
                        selectedDelfLevel = if (expectedType == LevelType.DELF) levelValue else selectedDelfLevel,
                        selectedDalfLevel = if (expectedType == LevelType.DALF) levelValue else selectedDalfLevel,
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

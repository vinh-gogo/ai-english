package com.aienglish.feature.aiteacher.ui.viewmodel

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.aiteacher.application.usecase.EvaluateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.application.usecase.ExplainSentenceUseCase
import com.aienglish.feature.aiteacher.application.usecase.GenerateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.TopicItem
import com.aienglish.feature.aiteacher.domain.repository.TopicRepository
import com.aienglish.feature.aiteacher.domain.repository.TeacherHistoryRepository
import com.aienglish.feature.aiteacher.domain.resource.JsonResourceReader
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.domain.repository.HistoryRepository
import com.aienglish.feature.aiteacher.ui.screen.getCompatibleLevels
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

enum class LevelType {
    CEFR, TOEIC, JLPT
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
    val targetLanguage: String = "en",

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
    val selectedTopic: String get() = topics.getOrNull(selectedTopicIndex)?.name ?: ""
    val selectedLevelText: String get() = when (levelType) {
        LevelType.CEFR -> selectedCefrLevel
        LevelType.TOEIC -> "TOEIC $selectedToeicLevel"
        LevelType.JLPT -> selectedJlptLevel
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

    init {
        val target = prefs.getLanguageTarget() // "en" or "ja"
        val expectedType = if (target == "ja") LevelType.JLPT else LevelType.CEFR
        val expectedLevel = if (expectedType == LevelType.JLPT) currentState.selectedJlptLevel else currentState.selectedCefrLevel
        setState {
            copy(
                targetLanguage = target,
                levelType = expectedType
            )
        }
        loadTopics(expectedType, expectedLevel)
    }

    private suspend fun ensureDatabaseSeeded() {
        val currentTopics = topicRepository.getTopics("CEFR", listOf("A1"))
        if (currentTopics.isEmpty()) {
            val systemsStr = jsonResourceReader.readSystemsJson()
            val levelsStr = jsonResourceReader.readLevelsJson()
            val englishStr = jsonResourceReader.readEnglishTopicsJson()
            val japaneseStr = jsonResourceReader.readJapaneseTopicsJson()
            topicRepository.seedDatabase(systemsStr, levelsStr, englishStr, japaneseStr)
        }
    }

    private fun loadTopics(levelType: LevelType, levelCode: String) {
        val systemCode = when (levelType) {
            LevelType.CEFR -> "CEFR"
            LevelType.TOEIC -> "TOEIC"
            LevelType.JLPT -> "JLPT"
        }
        viewModelScope.launch {
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

    fun syncTargetLanguage() {
        val target = prefs.getLanguageTarget() // "en" or "ja"
        val currentTarget = currentState.targetLanguage
        if (target != currentTarget) {
            val expectedType = if (target == "ja") LevelType.JLPT else LevelType.CEFR
            val expectedLevel = if (expectedType == LevelType.JLPT) currentState.selectedJlptLevel else currentState.selectedCefrLevel
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
        }
        setState { copy(levelType = type) }
        loadTopics(type, activeLevel)
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
            if (isGuest) {
                try {
                    val historyList = historyRepository.getAllHistory().first()
                    if (historyList.isNotEmpty()) {
                        setState {
                            copy(
                                error = "Chế độ dùng thử tối đa 1 bài học. Vui lòng đăng nhập để tiếp tục học không giới hạn!"
                            )
                        }
                        return@launch
                    }
                } catch (e: Exception) {
                    // Ignore
                }
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
                val expectedType = if (isToeic) LevelType.TOEIC else if (isJlpt) LevelType.JLPT else LevelType.CEFR
                val levelValue = if (isToeic) history.level.removePrefix("TOEIC ").trim() else history.level
                
                val systemCode = when (expectedType) {
                    LevelType.CEFR -> "CEFR"
                    LevelType.TOEIC -> "TOEIC"
                    LevelType.JLPT -> "JLPT"
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
        val expectedType = if (isToeic) LevelType.TOEIC else if (isJlpt) LevelType.JLPT else LevelType.CEFR
        val levelValue = if (isToeic) history.level.removePrefix("TOEIC ").trim() else history.level
        
        viewModelScope.launch {
            try {
                ensureDatabaseSeeded()

                val systemCode = when (expectedType) {
                    LevelType.CEFR -> "CEFR"
                    LevelType.TOEIC -> "TOEIC"
                    LevelType.JLPT -> "JLPT"
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
                        selectedJlptLevel = if (expectedType == LevelType.JLPT) levelValue else selectedJlptLevel
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
                val expectedType = if (isToeic) LevelType.TOEIC else if (isJlpt) LevelType.JLPT else LevelType.CEFR
                val levelValue = if (isToeic) history.level.removePrefix("TOEIC ").trim() else history.level
                
                val systemCode = when (expectedType) {
                    LevelType.CEFR -> "CEFR"
                    LevelType.TOEIC -> "TOEIC"
                    LevelType.JLPT -> "JLPT"
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
}

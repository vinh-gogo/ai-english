package com.aienglish.feature.aiteacher.ui.viewmodel

import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.aiteacher.application.usecase.EvaluateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.application.usecase.ExplainSentenceUseCase
import com.aienglish.feature.aiteacher.application.usecase.GenerateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.Topics
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

enum class LevelType {
    CEFR, TOEIC
}

data class TeacherUiState(
    // Topic selection
    val selectedTopicIndex: Int = 0,
    val topicSearchQuery: String = "",
    val levelType: LevelType = LevelType.CEFR,
    val selectedCefrLevel: String = "B1",
    val selectedToeicLevel: String = "500",

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
    val selectedTopic: String get() = Topics.list.getOrNull(selectedTopicIndex)?.name ?: ""
    val selectedLevelText: String get() = if (levelType == LevelType.CEFR) selectedCefrLevel else "TOEIC $selectedToeicLevel"
}

class TeacherViewModel(
    private val generateLesson: GenerateEnglishLessonUseCase,
    private val evaluateLesson: EvaluateEnglishLessonUseCase,
    private val explainSentence: ExplainSentenceUseCase,
    private val historyRepository: HistoryRepository
) : BaseViewModel<TeacherUiState>(TeacherUiState()) {

    // Track current lesson DB id for history update
    private var currentLessonHistoryId: Long? = null

    // ── Topic Selection ──
    fun onTopicIndexSelected(index: Int) = setState { copy(selectedTopicIndex = index) }
    fun onTopicSearchChange(query: String) = setState { copy(topicSearchQuery = query) }
    fun onLevelTypeSelected(type: LevelType) = setState {
        val activeLevel = if (type == LevelType.CEFR) selectedCefrLevel else selectedToeicLevel
        val firstIdx = Topics.list.indexOfFirst { it.levelCode == activeLevel }.coerceAtLeast(0)
        copy(levelType = type, selectedTopicIndex = firstIdx)
    }
    fun onCefrLevelSelected(level: String) = setState {
        val firstIdx = Topics.list.indexOfFirst { it.levelCode == level }.coerceAtLeast(0)
        copy(selectedCefrLevel = level, selectedTopicIndex = firstIdx)
    }
    fun onToeicLevelSelected(level: String) = setState {
        val firstIdx = Topics.list.indexOfFirst { it.levelCode == level }.coerceAtLeast(0)
        copy(selectedToeicLevel = level, selectedTopicIndex = firstIdx)
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
        viewModelScope.launch {
            // Save to history DB (IN_PROGRESS)
            currentLessonHistoryId = try {
                historyRepository.startLesson(topic, level)
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
                            val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
                            val lessonJson = json.encodeToString(lesson)
                            val userAnswersJson = json.encodeToString(state.userAnswers)
                            val explanationsJson = json.encodeToString(state.explanations)
                            val feedbackJson = json.encodeToString(fb)

                            historyRepository.completeLesson(
                                id = id,
                                score = fb.score,
                                lessonJson = lessonJson,
                                userAnswersJson = userAnswersJson,
                                explanationsJson = explanationsJson,
                                feedbackJson = feedbackJson
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
        val lessonJson = history.lessonJson ?: return
        val userAnswersJson = history.userAnswersJson ?: "{}"
        val explanationsJson = history.explanationsJson ?: "{}"
        val feedbackJson = history.feedbackJson ?: return

        val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
        try {
            val lesson = json.decodeFromString<EnglishLesson>(lessonJson)
            val userAnswers = json.decodeFromString<Map<Int, String>>(userAnswersJson)
            val explanations = json.decodeFromString<Map<Int, String>>(explanationsJson)
            val feedback = json.decodeFromString<EnglishFeedback>(feedbackJson)

            val topicIndex = Topics.list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)
            val isToeic = history.level.startsWith("TOEIC")
            val levelValue = if (isToeic) history.level.removePrefix("TOEIC ").trim() else history.level

            setState {
                copy(
                    selectedTopicIndex = topicIndex,
                    levelType = if (isToeic) LevelType.TOEIC else LevelType.CEFR,
                    selectedCefrLevel = if (!isToeic) levelValue else selectedCefrLevel,
                    selectedToeicLevel = if (isToeic) levelValue else selectedToeicLevel,
                    lesson = lesson,
                    userAnswers = userAnswers,
                    explanations = explanations,
                    feedback = feedback,
                    currentSentenceIndex = 0,
                    checkedAnswers = userAnswers.keys
                )
            }
        } catch (e: Exception) {
            setState { copy(error = "Không thể tải lại lịch sử bài học: ${e.message}") }
        }
    }

    fun onReTestLesson(history: LessonHistory) {
        val topicIndex = Topics.list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)
        val isToeic = history.level.startsWith("TOEIC")
        val levelValue = if (isToeic) history.level.removePrefix("TOEIC ").trim() else history.level

        setState {
            copy(
                selectedTopicIndex = topicIndex,
                levelType = if (isToeic) LevelType.TOEIC else LevelType.CEFR,
                selectedCefrLevel = if (!isToeic) levelValue else selectedCefrLevel,
                selectedToeicLevel = if (isToeic) levelValue else selectedToeicLevel
            )
        }
        onStartLesson()
    }

    fun onDismissError() = setState { copy(error = null) }
    fun onReset() = setState { TeacherUiState() }

    private fun saveCurrentProgress() {
        val id = currentLessonHistoryId ?: return
        val lesson = currentState.lesson ?: return
        viewModelScope.launch {
            try {
                val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
                val lessonJson = json.encodeToString(lesson)
                val userAnswersJson = json.encodeToString(currentState.userAnswers)
                val explanationsJson = json.encodeToString(currentState.explanations)
                historyRepository.updateLessonProgress(id, lessonJson, userAnswersJson, explanationsJson)
            } catch (e: Exception) { /* ignore */ }
        }
    }

    fun onResumeLesson(history: LessonHistory) {
        val lessonJson = history.lessonJson ?: return
        val userAnswersJson = history.userAnswersJson ?: "{}"
        val explanationsJson = history.explanationsJson ?: "{}"

        val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }
        try {
            val lesson = json.decodeFromString<EnglishLesson>(lessonJson)
            val userAnswers = json.decodeFromString<Map<Int, String>>(userAnswersJson)
            val explanations = json.decodeFromString<Map<Int, String>>(explanationsJson)

            val topicIndex = Topics.list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)
            val isToeic = history.level.startsWith("TOEIC")
            val levelValue = if (isToeic) history.level.removePrefix("TOEIC ").trim() else history.level

            currentLessonHistoryId = history.id

            setState {
                copy(
                    selectedTopicIndex = topicIndex,
                    levelType = if (isToeic) LevelType.TOEIC else LevelType.CEFR,
                    selectedCefrLevel = if (!isToeic) levelValue else selectedCefrLevel,
                    selectedToeicLevel = if (isToeic) levelValue else selectedToeicLevel,
                    lesson = lesson,
                    userAnswers = userAnswers,
                    explanations = explanations,
                    feedback = null,
                    currentSentenceIndex = 0,
                    checkedAnswers = explanations.keys
                )
            }
        } catch (e: Exception) {
            setState { copy(error = "Không thể tiếp tục bài học dở dang: ${e.message}") }
        }
    }
}

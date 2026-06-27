package com.aienglish.feature.aiteacher.ui.viewmodel

import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.aiteacher.application.usecase.EvaluateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.application.usecase.ExplainSentenceUseCase
import com.aienglish.feature.aiteacher.application.usecase.GenerateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.Topics
import com.aienglish.feature.aiteacher.domain.repository.HistoryRepository
import kotlinx.coroutines.launch

data class TeacherUiState(
    // Topic selection
    val selectedTopicIndex: Int = 0,
    val topicSearchQuery: String = "",
    val selectedCefrLevel: String = "B1",

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
    val isExplaining: Boolean = false,
    val selectedExplainIndex: Int? = null,
    val explanations: Map<Int, String> = emptyMap(),

    // Error
    val error: String? = null
) {
    val selectedTopic: String get() = Topics.list.getOrNull(selectedTopicIndex) ?: ""
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
    fun onCefrLevelSelected(level: String) = setState { copy(selectedCefrLevel = level) }

    // ── Lesson Navigation ──
    fun onAnswerChanged(index: Int, answer: String) = setState {
        copy(userAnswers = userAnswers + (index to answer))
    }

    fun onCheckAnswer(index: Int) {
        val state = currentState
        val lesson = state.lesson ?: return
        val level = state.selectedCefrLevel
        val sentence = lesson.sentences.getOrNull(index) ?: return
        val userAnswer = state.userAnswers[index] ?: ""

        setState {
            copy(
                checkedAnswers = checkedAnswers + index,
                isExplaining = true,
                selectedExplainIndex = index,
                error = null
            )
        }

        viewModelScope.launch {
            explainSentence("user", sentence.vietnamese, sentence.english, userAnswer, level).fold(
                ifLeft = { err ->
                    setState {
                        copy(
                            isExplaining = false,
                            error = "Không thể tải giải thích từ AI: ${err.message}"
                        )
                    }
                },
                ifRight = { explanation ->
                    setState {
                        copy(
                            isExplaining = false,
                            explanations = explanations + (index to explanation)
                        )
                    }
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
        val level = currentState.selectedCefrLevel
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
                ifRight = { lesson -> setState { copy(isGeneratingLesson = false, lesson = lesson) } }
            )
        }
    }

    // ── Submit for Evaluation ──
    fun onSubmitLesson() {
        val state = currentState
        val lesson = state.lesson ?: return
        val level = state.selectedCefrLevel
        val answers = lesson.sentences.mapIndexed { i, s -> (state.userAnswers[i] ?: "") to s.english }
        val sentences = lesson.sentences.map { it.vietnamese }
        setState { copy(isEvaluating = true, error = null) }
        viewModelScope.launch {
            evaluateLesson("user", state.selectedTopic, answers, sentences, level).fold(
                ifLeft = { err -> setState { copy(isEvaluating = false, error = err.message) } },
                ifRight = { fb ->
                    setState { copy(isEvaluating = false, feedback = fb) }
                    // Update history to COMPLETED with score
                    currentLessonHistoryId?.let { id ->
                        try { historyRepository.completeLesson(id, fb.score) } catch (e: Exception) { /* ignore */ }
                    }
                }
            )
        }
    }

    // ── Explain Sentence ──
    fun onExplainSentence(index: Int) {
        val state = currentState
        val lesson = state.lesson ?: return
        val level = state.selectedCefrLevel
        if (state.explanations.containsKey(index)) {
            setState { copy(selectedExplainIndex = index) }
            return
        }
        val sentence = lesson.sentences.getOrNull(index) ?: return
        val userAnswer = state.userAnswers[index] ?: ""
        setState { copy(isExplaining = true, selectedExplainIndex = index, error = null) }
        viewModelScope.launch {
            explainSentence("user", sentence.vietnamese, sentence.english, userAnswer, level).fold(
                ifLeft = { err -> setState { copy(isExplaining = false, error = err.message) } },
                ifRight = { explanation ->
                    setState { copy(isExplaining = false, explanations = explanations + (index to explanation)) }
                }
            )
        }
    }

    fun onCloseExplanation() = setState { copy(selectedExplainIndex = null) }
    fun onDismissError() = setState { copy(error = null) }
    fun onReset() = setState { TeacherUiState() }
}

package com.aienglish.feature.aiteacher.ui.viewmodel

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.common.tts.TtsPlayer
import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.aiteacher.application.usecase.EvaluateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.application.usecase.ExplainSentenceUseCase
import com.aienglish.feature.aiteacher.application.usecase.GenerateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.domain.repository.TeacherHistoryRepository
import com.aienglish.feature.aiteacher.domain.repository.TopicRepository
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TeacherViewModel(
    generateLesson: GenerateEnglishLessonUseCase,
    evaluateLesson: EvaluateEnglishLessonUseCase,
    explainSentence: ExplainSentenceUseCase,
    private val teacherHistoryRepository: TeacherHistoryRepository,
    topicRepository: TopicRepository,
    prefs: AppPreferences,
    historyRepository: HistoryRepository,
    ttsPlayer: TtsPlayer
) : BaseViewModel<TeacherUiState>(TeacherUiState()), TeacherStateUpdater, SharedViewModelContext {

    override val currentUiState: TeacherUiState
        get() = currentState

    override fun updateState(reducer: TeacherUiState.() -> TeacherUiState) {
        setState(reducer)
    }

    override var currentLessonHistoryId: Long? = null
    override var lastTopicSelectionState: TopicSelectionState? = null

    override fun saveCurrentProgress() {
        val id = currentLessonHistoryId ?: return
        val lesson = currentUiState.lesson ?: return
        viewModelScope.launch {
            try {
                teacherHistoryRepository.updateLessonProgress(
                    id = id,
                    lesson = lesson,
                    userAnswers = currentUiState.userAnswers,
                    explanations = currentUiState.explanations
                )
            } catch (e: Exception) { /* ignore */ }
        }
    }

    override fun saveTopicSelectionSnapshot() {
        if (currentUiState.lesson == null && currentUiState.feedback == null) {
            lastTopicSelectionState = TopicSelectionState(
                levelType = currentUiState.levelType,
                selectedLevels = currentUiState.selectedLevels,
                topicSearchQuery = currentUiState.topicSearchQuery,
                isCustomTopicMode = currentUiState.isCustomTopicMode,
                customTopicInput = currentUiState.customTopicInput,
                selectedTopicIndex = currentUiState.selectedTopicIndex
            )
        }
    }

    private val ttsManager = TtsManager(ttsPlayer, this)
    private val topicManager = TopicManager(topicRepository, prefs, ttsManager, viewModelScope, this)
    private val lessonManager = LessonExecutionManager(
        generateLesson, evaluateLesson, explainSentence, teacherHistoryRepository, 
        historyRepository, prefs, viewModelScope, this, this
    )
    private val historyManager = HistoryManager(
        teacherHistoryRepository, topicRepository, viewModelScope, this, this, topicManager, lessonManager
    )

    // ── TTS Methods ──
    val isTtsLoading: StateFlow<Boolean> = ttsManager.isLoading
    fun speak(text: String, languageCode: String) = ttsManager.speak(text, languageCode)
    fun speakTopic(topicName: String) = ttsManager.speakTopic(topicName)
    fun stopSpeaking() = ttsManager.stopSpeaking()
    fun speakExplanation(rawText: String) = ttsManager.speakExplanation(rawText)

    // ── Topic Selection ──
    fun onTopicIndexSelected(index: Int) = topicManager.onTopicIndexSelected(index)
    fun onTopicSearchChange(query: String) = topicManager.onTopicSearchChange(query)
    fun onRemoveTopic(id: String) = topicManager.onRemoveTopic(id)
    fun onLevelTypeSelected(type: LevelType) = topicManager.onLevelTypeSelected(type)
    fun onLevelSelected(level: String) = topicManager.onLevelSelected(level)
    fun setCustomTopicMode(enabled: Boolean) = topicManager.setCustomTopicMode(enabled)
    fun onCustomTopicInputChanged(input: String) = topicManager.onCustomTopicInputChanged(input)
    fun showSaveToDbDialog() = topicManager.showSaveToDbDialog()
    fun dismissSaveToDbDialog() = topicManager.dismissSaveToDbDialog()
    fun saveCustomTopicToDb(name: String) = topicManager.saveCustomTopicToDb(name)
    fun saveRecommendedTopicToDb(topicName: String) = topicManager.saveRecommendedTopicToDb(topicName)

    // ── Lesson Execution ──
    fun onAnswerChanged(index: Int, answer: String) = lessonManager.onAnswerChanged(index, answer)
    fun onCheckAnswer(index: Int) = lessonManager.onCheckAnswer(index)
    fun onNextSentence() = lessonManager.onNextSentence()
    fun onPreviousSentence() = lessonManager.onPreviousSentence()
    fun onStartLesson() = lessonManager.onStartLesson()
    fun onSubmitLesson() = lessonManager.onSubmitLesson()
    fun onExplainSentence(index: Int) = lessonManager.onExplainSentence(index)
    fun onRetryCurrentLesson() = lessonManager.onRetryCurrentLesson()
    fun onReset() = lessonManager.onReset(topicManager)
    fun fetchMonthlyUsage(onResult: (usage: Int, limit: Int) -> Unit) = lessonManager.fetchMonthlyUsage(onResult)

    // ── History & Notes ──
    fun saveQuestionNote(index: Int, note: String) = historyManager.saveQuestionNote(index, note)
    fun updateQuestionNote(noteId: String, sentenceIndex: Int, newNote: String) = historyManager.updateQuestionNote(noteId, sentenceIndex, newNote)
    fun deleteQuestionNote(noteId: String, sentenceIndex: Int) = historyManager.deleteQuestionNote(noteId, sentenceIndex)
    fun openSavedNotes() = historyManager.openSavedNotes()
    fun closeSavedNotes() = historyManager.closeSavedNotes()
    fun onReviewLesson(history: LessonHistory) = historyManager.onReviewLesson(history)
    fun onReTestLesson(history: LessonHistory) = historyManager.onReTestLesson(history)
    fun onResumeLesson(history: LessonHistory) = historyManager.onResumeLesson(history)

    // ── Utilities ──
    fun clearError() = setState { copy(error = null) }
    fun clearInfoMessage() = setState { copy(infoMessage = null) }
    fun onDismissError() = clearError()
}

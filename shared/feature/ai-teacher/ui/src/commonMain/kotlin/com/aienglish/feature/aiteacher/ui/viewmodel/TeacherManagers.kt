package com.aienglish.feature.aiteacher.ui.viewmodel

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.common.tts.TtsPlayer
import com.aienglish.feature.aiteacher.application.usecase.EvaluateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.application.usecase.ExplainSentenceUseCase
import com.aienglish.feature.aiteacher.application.usecase.GenerateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.domain.model.getCompatibleLevels
import com.aienglish.feature.aiteacher.domain.repository.TeacherHistoryRepository
import com.aienglish.feature.aiteacher.domain.repository.TopicRepository
import com.aienglish.feature.history.domain.model.LessonHistory
import com.aienglish.feature.history.domain.repository.HistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

interface TeacherStateUpdater {
    val currentUiState: TeacherUiState
    fun updateState(reducer: TeacherUiState.() -> TeacherUiState)
}

interface SharedViewModelContext {
    var currentLessonHistoryId: Long?
    var lastTopicSelectionState: TopicSelectionState?
    fun saveCurrentProgress()
    fun saveTopicSelectionSnapshot()
}

class TtsManager(
    private val ttsPlayer: TtsPlayer,
    private val updater: TeacherStateUpdater
) {
    val isLoading = ttsPlayer.isLoading

    fun speak(text: String, languageCode: String) {
        ttsPlayer.play(text, languageCode)
    }

    fun speakTopic(topicName: String) {
        val cleanText = topicName.replace("**", "").trim()
        val ttsLang = if (updater.currentUiState.targetLanguage == "vi") "en" else "vi"
        ttsPlayer.play(cleanText, ttsLang)
    }

    fun stopSpeaking() = ttsPlayer.stop()

    fun speakExplanation(rawText: String) {
        val normalizedText = rawText.replace(Regex("\\n{3,}"), "\n\n").trim()
        ttsPlayer.playMixed(normalizedText)
    }
}

class TopicManager(
    private val topicRepository: TopicRepository,
    private val prefs: AppPreferences,
    private val ttsManager: TtsManager,
    private val scope: CoroutineScope,
    private val updater: TeacherStateUpdater
) {
    private var isDatabaseSeeded = false
    private var loadTopicsJob: Job? = null

    init {
        val target = prefs.getLanguageTarget()
        val expectedType = LevelType.fromTargetLanguage(target)
        val expectedLevel = updater.currentUiState.selectedLevels[expectedType] ?: expectedType.defaultLevel
        updater.updateState {
            copy(targetLanguage = target, levelType = expectedType)
        }
        loadTopics(expectedType, expectedLevel)

        scope.launch {
            prefs.languageTargetFlow.collect { newTarget ->
                syncTargetLanguage(newTarget)
            }
        }
    }

    suspend fun ensureDatabaseSeeded() {
        if (isDatabaseSeeded) return
        val currentTopics = topicRepository.getTopics("CEFR", listOf("A1"))
        val currentJapaneseTopics = topicRepository.getTopics("JLPT", listOf("N5"))
        val currentChineseTopics = topicRepository.getTopics("HSK", listOf("HSK1"))
        val currentToeicTopics = topicRepository.getTopics("TOEIC", listOf("300"))
        val currentDeleTopics = topicRepository.getTopics("DELE", listOf("A1"))
        val currentHindiTopics = topicRepository.getTopics("HINDI", listOf("A1"))
        val currentFrenchTopics = topicRepository.getTopics("DELF", listOf("A1"))
        val currentGermanTopics = topicRepository.getTopics("GOETHE", listOf("A1"))
        val currentKoreanTopics = topicRepository.getTopics("TOPIK", listOf("TOPIK1"))
        val currentVietTopics = topicRepository.getTopics("VIET", listOf("TIEU_HOC", "TRUNG_HOC", "DAI_HOC"))
        val hasBoldTopic = currentToeicTopics.any { it.name.contains("**") }
        if (currentTopics.isEmpty() || !hasBoldTopic || currentJapaneseTopics.size < 120 || currentChineseTopics.isEmpty() ||
            currentToeicTopics.isEmpty() || currentDeleTopics.isEmpty() || currentHindiTopics.isEmpty() ||
            currentFrenchTopics.isEmpty() || currentGermanTopics.isEmpty() || currentKoreanTopics.isEmpty() ||
            currentVietTopics.size < 400) {
            topicRepository.seedDatabase()
        }
        isDatabaseSeeded = true
    }

    fun loadTopics(levelType: LevelType, levelCode: String, restoreTopicIndex: Int? = null) {
        val systemCode = levelType.systemCode
        loadTopicsJob?.cancel()
        loadTopicsJob = scope.launch {
            try {
                ensureDatabaseSeeded()

                val compatible = getCompatibleLevels(levelCode)
                val rawList = topicRepository.getTopics(systemCode, compatible)
                val grouped = rawList.groupBy { it.levelCode }
                val sortedList = compatible.reversed().flatMap { lvl ->
                    val topicsInLvl = grouped[lvl] ?: emptyList()
                    val (aiTopics, normalTopics) = topicsInLvl.partition { it.isAiRecommended }
                    aiTopics.shuffled() + normalTopics.shuffled()
                }

                val allLevels = levelType.levels
                val allTopics = topicRepository.getTopics(systemCode, allLevels)
                val rawCounts = allTopics.groupBy { it.levelCode }.mapValues { it.value.size }
                val cumulativeCounts = allLevels.associateWith { lvl ->
                    val lvlCompatible = getCompatibleLevels(lvl)
                    lvlCompatible.sumOf { rawCounts[it] ?: 0 }
                }

                updater.updateState {
                    copy(
                        topics = sortedList,
                        selectedTopicIndex = restoreTopicIndex?.coerceIn(0, sortedList.lastIndex)?.coerceAtLeast(0) ?: 0,
                        topicCounts = cumulativeCounts
                    )
                }
            } catch (e: Exception) {
                updater.updateState { copy(error = "Không thể tải chủ đề từ database: ${e.message}") }
            }
        }
    }

    private fun syncTargetLanguage(target: String) {
        val currentTarget = updater.currentUiState.targetLanguage
        if (target != currentTarget) {
            val expectedType = LevelType.fromTargetLanguage(target)
            val expectedLevel = updater.currentUiState.selectedLevels[expectedType] ?: expectedType.defaultLevel
            updater.updateState {
                copy(targetLanguage = target, levelType = expectedType)
            }
            loadTopics(expectedType, expectedLevel)
        }
    }

    fun onTopicIndexSelected(index: Int) {
        updater.updateState { copy(selectedTopicIndex = index) }
        val selectedTopic = updater.currentUiState.topics.getOrNull(index) ?: return
        ttsManager.speakTopic(selectedTopic.name)
    }

    fun onTopicSearchChange(query: String) = updater.updateState { copy(topicSearchQuery = query) }

    fun onRemoveTopic(id: String) {
        val systemCode = updater.currentUiState.levelType.systemCode
        scope.launch {
            topicRepository.deleteTopic(id, systemCode)
            loadTopics(updater.currentUiState.levelType, updater.currentUiState.currentLevelCode)
        }
    }

    fun onLevelTypeSelected(type: LevelType) {
        val activeLevel = updater.currentUiState.selectedLevels[type] ?: type.defaultLevel
        updater.updateState { copy(levelType = type) }
        loadTopics(type, activeLevel)
    }

    fun onLevelSelected(level: String) {
        val newMap = updater.currentUiState.selectedLevels.toMutableMap()
        newMap[updater.currentUiState.levelType] = level
        updater.updateState { copy(selectedLevels = newMap) }
        loadTopics(updater.currentUiState.levelType, level)
    }

    fun setCustomTopicMode(enabled: Boolean) {
        updater.updateState { copy(isCustomTopicMode = enabled) }
    }

    fun onCustomTopicInputChanged(input: String) {
        updater.updateState { copy(customTopicInput = input) }
    }

    fun showSaveToDbDialog() {
        if (updater.currentUiState.isCustomTopicMode && updater.currentUiState.customTopicInput.isNotBlank()) {
            updater.updateState { copy(showSaveToDbDialog = true) }
        }
    }

    fun dismissSaveToDbDialog() {
        updater.updateState { copy(showSaveToDbDialog = false) }
    }

    fun saveCustomTopicToDb(name: String) {
        val state = updater.currentUiState
        if (!state.isCustomTopicMode || name.isBlank()) return
        
        val systemCode = state.levelType.systemCode
        val levelCode = state.currentLevelCode
        
        scope.launch {
            try {
                topicRepository.addCustomTopic(
                    systemCode = systemCode,
                    levelCode = levelCode,
                    name = name.trim()
                )
                updater.updateState { 
                    copy(
                        showSaveToDbDialog = false,
                        infoMessage = "Đã lưu chủ đề vào danh sách"
                    ) 
                }
                loadTopics(state.levelType, levelCode)
            } catch (e: Exception) {
                updater.updateState { 
                    copy(
                        showSaveToDbDialog = false,
                        error = "Lỗi khi lưu chủ đề: ${e.message}"
                    ) 
                }
            }
        }
    }

    fun saveRecommendedTopicToDb(topicName: String) {
        val state = updater.currentUiState
        val systemCode = state.levelType.systemCode
        val levelCode = state.currentLevelCode
        
        scope.launch {
            try {
                val cleanName = topicName.replace(Regex("^\\[TOPIC_RECOMMEND\\]\\s*"), "").trim()
                topicRepository.addCustomTopic(
                    systemCode = systemCode,
                    levelCode = levelCode,
                    name = cleanName,
                    isAiRecommended = true
                )
                updater.updateState { 
                    copy(infoMessage = "Đã lưu gợi ý vào danh sách chủ đề") 
                }
                loadTopics(state.levelType, levelCode)
            } catch (e: Exception) {
                updater.updateState { 
                    copy(error = "Lỗi khi lưu gợi ý: ${e.message}") 
                }
            }
        }
    }
}

class LessonExecutionManager(
    private val generateLesson: GenerateEnglishLessonUseCase,
    private val evaluateLesson: EvaluateEnglishLessonUseCase,
    private val explainSentence: ExplainSentenceUseCase,
    private val teacherHistoryRepository: TeacherHistoryRepository,
    private val historyRepository: HistoryRepository,
    private val prefs: AppPreferences,
    private val scope: CoroutineScope,
    private val updater: TeacherStateUpdater,
    private val sharedContext: SharedViewModelContext
) {
    fun onAnswerChanged(index: Int, answer: String) {
        updater.updateState {
            copy(userAnswers = userAnswers + (index to answer))
        }
        sharedContext.saveCurrentProgress()
    }

    fun onCheckAnswer(index: Int) {
        val state = updater.currentUiState
        val lesson = state.lesson ?: return
        val level = state.selectedLevelText
        val sentence = lesson.sentences.getOrNull(index) ?: return
        val userAnswer = state.userAnswers[index] ?: ""

        updater.updateState {
            copy(
                checkedAnswers = checkedAnswers + index,
                explainingSentenceIndex = index,
                error = null
            )
        }

        scope.launch {
            explainSentence("user", sentence.vietnamese, sentence.english, userAnswer, level).fold(
                ifLeft = { err ->
                    updater.updateState {
                        copy(
                            explainingSentenceIndex = null,
                            error = "Không thể tải giải thích từ AI: ${err.message}"
                        )
                    }
                },
                ifRight = { result ->
                    updater.updateState {
                        copy(
                            explainingSentenceIndex = null,
                            explanations = explanations + (index to result.explanation)
                        )
                    }
                    sharedContext.saveCurrentProgress()
                }
            )
        }
    }

    fun onNextSentence() = updater.updateState {
        val next = (currentSentenceIndex + 1).coerceAtMost((lesson?.sentences?.size ?: 1) - 1)
        copy(currentSentenceIndex = next)
    }

    fun onPreviousSentence() = updater.updateState {
        val prev = (currentSentenceIndex - 1).coerceAtLeast(0)
        copy(currentSentenceIndex = prev)
    }

    fun onStartLesson() {
        sharedContext.saveTopicSelectionSnapshot()
        val topic = updater.currentUiState.selectedTopic
        val level = updater.currentUiState.selectedLevelText
        val isGuest = prefs.getUserName() == "Khách"

        scope.launch {
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
                    updater.updateState { copy(error = limitMsg) }
                    return@launch
                }
            } catch (e: Exception) {
                // Ignore
            }

            updater.updateState {
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

            val topicId = updater.currentUiState.selectedTopicId
            sharedContext.currentLessonHistoryId = try {
                teacherHistoryRepository.startLesson(topic, level, topicId)
            } catch (e: Exception) { null }

            generateLesson("user", topic, level).fold(
                ifLeft = { err -> updater.updateState { copy(isGeneratingLesson = false, error = err.message) } },
                ifRight = { lesson ->
                    updater.updateState { copy(isGeneratingLesson = false, lesson = lesson) }
                    sharedContext.saveCurrentProgress()
                }
            )
        }
    }

    fun onSubmitLesson() {
        val state = updater.currentUiState
        val lesson = state.lesson ?: return
        val level = state.selectedLevelText
        val answers = lesson.sentences.mapIndexed { i, s -> (state.userAnswers[i] ?: "") to s.english }
        val sentences = lesson.sentences.map { it.vietnamese }
        updater.updateState { copy(isEvaluating = true, error = null) }
        scope.launch {
            evaluateLesson("user", state.selectedTopic, answers, sentences, level).fold(
                ifLeft = { err -> updater.updateState { copy(isEvaluating = false, error = err.message) } },
                ifRight = { fb ->
                    updater.updateState { copy(isEvaluating = false, feedback = fb) }
                    sharedContext.currentLessonHistoryId?.let { id ->
                        try {
                            teacherHistoryRepository.completeLesson(
                                id = id,
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
        val state = updater.currentUiState
        if (state.explanations.containsKey(index) || state.explainingSentenceIndex == index) return

        val lesson = state.lesson ?: return
        val level = state.selectedLevelText
        val sentence = lesson.sentences.getOrNull(index) ?: return
        val userAnswer = state.userAnswers[index] ?: ""

        updater.updateState {
            copy(
                explainingSentenceIndex = index,
                error = null
            )
        }

        scope.launch {
            explainSentence("user", sentence.vietnamese, sentence.english, userAnswer, level).fold(
                ifLeft = { err ->
                    updater.updateState {
                        copy(
                            explainingSentenceIndex = null,
                            error = "Không thể tải giải thích từ AI: ${err.message}"
                        )
                    }
                },
                ifRight = { result ->
                    updater.updateState {
                        copy(
                            explainingSentenceIndex = null,
                            explanations = explanations + (index to result.explanation)
                        )
                    }
                    sharedContext.saveCurrentProgress()
                }
            )
        }
    }

    fun onRetryCurrentLesson() {
        updater.updateState {
            copy(
                userAnswers = emptyMap(),
                checkedAnswers = emptySet(),
                currentSentenceIndex = 0,
                feedback = null,
                error = null
            )
        }
    }

    fun onReset(topicManager: TopicManager) {
        val target = prefs.getLanguageTarget()
        val snapshot = sharedContext.lastTopicSelectionState
        if (snapshot != null && target == updater.currentUiState.targetLanguage) {
            sharedContext.currentLessonHistoryId = null
            updater.updateState {
                copy(
                    targetLanguage = target,
                    levelType = snapshot.levelType,
                    selectedLevels = snapshot.selectedLevels,
                    topicSearchQuery = snapshot.topicSearchQuery,
                    isCustomTopicMode = snapshot.isCustomTopicMode,
                    customTopicInput = snapshot.customTopicInput,
                    lesson = null,
                    userAnswers = emptyMap(),
                    checkedAnswers = emptySet(),
                    currentSentenceIndex = 0,
                    explanations = emptyMap(),
                    feedback = null,
                    isGeneratingLesson = false,
                    error = null
                )
            }
            topicManager.loadTopics(snapshot.levelType, snapshot.selectedLevels[snapshot.levelType] ?: snapshot.levelType.defaultLevel, snapshot.selectedTopicIndex)
        } else {
            sharedContext.currentLessonHistoryId = null
            updater.updateState {
                copy(
                    lesson = null,
                    userAnswers = emptyMap(),
                    checkedAnswers = emptySet(),
                    currentSentenceIndex = 0,
                    explanations = emptyMap(),
                    feedback = null,
                    isGeneratingLesson = false,
                    error = null
                )
            }
            val expectedType = LevelType.fromTargetLanguage(target)
            val expectedLevel = updater.currentUiState.selectedLevels[expectedType] ?: expectedType.defaultLevel
            topicManager.loadTopics(expectedType, expectedLevel)
        }
    }

    fun fetchMonthlyUsage(onResult: (usage: Int, limit: Int) -> Unit) {
        scope.launch {
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

class HistoryManager(
    private val teacherHistoryRepository: TeacherHistoryRepository,
    private val topicRepository: TopicRepository,
    private val scope: CoroutineScope,
    private val updater: TeacherStateUpdater,
    private val sharedContext: SharedViewModelContext,
    private val topicManager: TopicManager,
    private val lessonManager: LessonExecutionManager
) {
    fun saveQuestionNote(index: Int, note: String) {
        val historyId = sharedContext.currentLessonHistoryId ?: return
        scope.launch {
            try {
                teacherHistoryRepository.saveQuestionNote(historyId, index, note)
                val notes = teacherHistoryRepository.getQuestionNotes(historyId, index)
                val newCounts = updater.currentUiState.savedNoteCounts.toMutableMap()
                newCounts[index] = notes.size
                updater.updateState { 
                    copy(
                        infoMessage = "Đã lưu cấu trúc",
                        savedNoteCounts = newCounts,
                        savedNotes = if (showSavedNotesBottomSheet) notes else savedNotes
                    ) 
                }
            } catch (e: Exception) { /* ignore */ }
        }
    }

    fun updateQuestionNote(noteId: String, sentenceIndex: Int, newNote: String) {
        val historyId = sharedContext.currentLessonHistoryId ?: return
        if (newNote.isBlank()) return
        scope.launch {
            try {
                teacherHistoryRepository.updateQuestionNote(noteId, newNote)
                updater.updateState { copy(infoMessage = "Đã cập nhật cấu trúc") }
                val notes = teacherHistoryRepository.getQuestionNotes(historyId, sentenceIndex)
                updater.updateState { copy(savedNotes = notes) }
            } catch (e: Exception) { /* ignore */ }
        }
    }

    fun deleteQuestionNote(noteId: String, sentenceIndex: Int) {
        val historyId = sharedContext.currentLessonHistoryId ?: return
        scope.launch {
            try {
                teacherHistoryRepository.deleteQuestionNote(noteId)
                val notes = teacherHistoryRepository.getQuestionNotes(historyId, sentenceIndex)
                val newCounts = updater.currentUiState.savedNoteCounts.toMutableMap()
                newCounts[sentenceIndex] = notes.size
                updater.updateState { 
                    copy(savedNotes = notes, savedNoteCounts = newCounts) 
                }
            } catch (e: Exception) { /* ignore */ }
        }
    }

    fun openSavedNotes(index: Int) {
        val historyId = sharedContext.currentLessonHistoryId ?: return
        scope.launch {
            try {
                val notes = teacherHistoryRepository.getQuestionNotes(historyId, index)
                updater.updateState { copy(savedNotes = notes, showSavedNotesBottomSheet = true) }
            } catch (e: Exception) { /* ignore */ }
        }
    }

    fun closeSavedNotes() {
        updater.updateState { copy(showSavedNotesBottomSheet = false, savedNotes = emptyList()) }
    }

    fun onReviewLesson(history: LessonHistory) {
        sharedContext.saveTopicSelectionSnapshot()
        scope.launch {
            try {
                topicManager.ensureDatabaseSeeded()
                val (expectedType, levelValue) = LevelType.parseLevelTypeAndValue(history.level)
                val systemCode = expectedType.systemCode
                val compatible = getCompatibleLevels(levelValue)
                val list = topicRepository.getTopics(systemCode, compatible)
                val topicIndex = list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)
                val teacherHistory = teacherHistoryRepository.loadHistory(history.id)
                if (teacherHistory == null) {
                    updater.updateState { copy(error = "Không thể tải lại lịch sử bài học") }
                    return@launch
                }
                val newMap = updater.currentUiState.selectedLevels.toMutableMap()
                newMap[expectedType] = levelValue

                updater.updateState {
                    copy(
                        topics = list,
                        selectedTopicIndex = topicIndex,
                        levelType = expectedType,
                        selectedLevels = newMap,
                        lesson = teacherHistory.lesson,
                        userAnswers = teacherHistory.userAnswers,
                        explanations = teacherHistory.explanations,
                        feedback = teacherHistory.feedback,
                        currentSentenceIndex = 0,
                        checkedAnswers = teacherHistory.userAnswers.keys
                    )
                }

                val newCounts = mutableMapOf<Int, Int>()
                val sentenceCount = teacherHistory.lesson?.sentences?.size ?: 0
                for (i in 0 until sentenceCount) {
                    val c = teacherHistoryRepository.getQuestionNotes(history.id, i).size
                    if (c > 0) newCounts[i] = c
                }
                updater.updateState { copy(savedNoteCounts = newCounts) }
            } catch (e: Exception) {
                updater.updateState { copy(error = "Không thể tải lại lịch sử bài học: ${e.message}") }
            }
        }
    }

    fun onResumeLesson(history: LessonHistory) {
        sharedContext.saveTopicSelectionSnapshot()
        scope.launch {
            try {
                topicManager.ensureDatabaseSeeded()

                val (expectedType, levelValue) = LevelType.parseLevelTypeAndValue(history.level)
                val systemCode = expectedType.systemCode
                val compatible = getCompatibleLevels(levelValue)
                val list = topicRepository.getTopics(systemCode, compatible)
                val topicIndex = list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)

                val teacherHistory = teacherHistoryRepository.loadHistory(history.id)
                if (teacherHistory == null) {
                    updater.updateState { copy(error = "Không thể tiếp tục bài học dở dang") }
                    return@launch
                }

                sharedContext.currentLessonHistoryId = history.id

                val totalSentences = teacherHistory.lesson?.sentences?.size ?: 0
                val nextIndex = teacherHistory.lesson?.sentences?.indices?.firstOrNull { idx ->
                    !teacherHistory.explanations.containsKey(idx)
                } ?: if (totalSentences > 0) totalSentences - 1 else 0

                val newMap = updater.currentUiState.selectedLevels.toMutableMap()
                newMap[expectedType] = levelValue

                updater.updateState {
                    copy(
                        topics = list,
                        selectedTopicIndex = topicIndex,
                        levelType = expectedType,
                        selectedLevels = newMap,
                        lesson = teacherHistory.lesson,
                        userAnswers = teacherHistory.userAnswers,
                        explanations = teacherHistory.explanations,
                        feedback = null,
                        currentSentenceIndex = nextIndex,
                        checkedAnswers = teacherHistory.explanations.keys
                    )
                }

                val newCounts = mutableMapOf<Int, Int>()
                val sentenceCount = teacherHistory.lesson?.sentences?.size ?: 0
                for (i in 0 until sentenceCount) {
                    val c = teacherHistoryRepository.getQuestionNotes(history.id, i).size
                    if (c > 0) newCounts[i] = c
                }
                updater.updateState { copy(savedNoteCounts = newCounts) }
            } catch (e: Exception) {
                updater.updateState { copy(error = "Không thể tiếp tục bài học dở dang: ${e.message}") }
            }
        }
    }

    fun onReTestLesson(history: LessonHistory) {
        sharedContext.saveTopicSelectionSnapshot()
        val (expectedType, levelValue) = LevelType.parseLevelTypeAndValue(history.level)
        scope.launch {
            try {
                topicManager.ensureDatabaseSeeded()
                val systemCode = expectedType.systemCode
                val compatible = getCompatibleLevels(levelValue)
                val list = topicRepository.getTopics(systemCode, compatible)
                val topicIndex = list.indexOfFirst { it.name == history.topic }.coerceAtLeast(0)
                val newMap = updater.currentUiState.selectedLevels.toMutableMap()
                newMap[expectedType] = levelValue
                updater.updateState {
                    copy(
                        topics = list,
                        selectedTopicIndex = topicIndex,
                        levelType = expectedType,
                        selectedLevels = newMap
                    )
                }
                lessonManager.onStartLesson()
            } catch (e: Exception) {
                updater.updateState { copy(error = "Không thể thi lại bài học: ${e.message}") }
            }
        }
    }
}

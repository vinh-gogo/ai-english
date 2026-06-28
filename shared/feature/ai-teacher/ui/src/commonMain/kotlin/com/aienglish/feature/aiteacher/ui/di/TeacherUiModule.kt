package com.aienglish.feature.aiteacher.ui.di

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.feature.aiteacher.application.usecase.EvaluateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.application.usecase.ExplainSentenceUseCase
import com.aienglish.feature.aiteacher.application.usecase.GenerateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import com.aienglish.feature.aiteacher.domain.repository.TeacherHistoryRepository
import com.aienglish.feature.aiteacher.domain.repository.TopicRepository
import org.koin.dsl.module

val teacherUiModule = module {
    single {
        TeacherViewModel(
            generateLesson = get(),
            evaluateLesson = get(),
            explainSentence = get(),
            teacherHistoryRepository = get(),
            topicRepository = get(),
            jsonResourceReader = get(),
            prefs = get(),
            historyRepository = get()
        )
    }
}

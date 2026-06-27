package com.aienglish.feature.aiteacher.ui.di

import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.feature.aiteacher.application.usecase.EvaluateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.application.usecase.ExplainSentenceUseCase
import com.aienglish.feature.aiteacher.application.usecase.GenerateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.domain.repository.HistoryRepository
import com.aienglish.feature.aiteacher.ui.viewmodel.HistoryViewModel
import com.aienglish.feature.aiteacher.ui.viewmodel.ProfileViewModel
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import org.koin.dsl.module

val teacherUiModule = module {
    single {
        TeacherViewModel(
            generateLesson = get<GenerateEnglishLessonUseCase>(),
            evaluateLesson = get<EvaluateEnglishLessonUseCase>(),
            explainSentence = get<ExplainSentenceUseCase>(),
            historyRepository = get<HistoryRepository>()
        )
    }
    single {
        ProfileViewModel(prefs = get<AppPreferences>())
    }
    single {
        HistoryViewModel(historyRepository = get<HistoryRepository>())
    }
}

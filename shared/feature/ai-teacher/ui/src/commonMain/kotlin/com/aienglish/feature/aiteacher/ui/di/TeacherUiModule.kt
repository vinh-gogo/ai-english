package com.aienglish.feature.aiteacher.ui.di

import com.aienglish.feature.aiteacher.application.usecase.EvaluateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.application.usecase.ExplainSentenceUseCase
import com.aienglish.feature.aiteacher.application.usecase.GenerateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.ui.viewmodel.TeacherViewModel
import com.aienglish.feature.history.domain.repository.HistoryRepository
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
}

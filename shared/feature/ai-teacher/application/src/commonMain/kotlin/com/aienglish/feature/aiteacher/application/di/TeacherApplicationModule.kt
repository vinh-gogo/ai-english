package com.aienglish.feature.aiteacher.application.di

import com.aienglish.feature.aiteacher.application.usecase.EvaluateEnglishLessonUseCase
import com.aienglish.feature.aiteacher.application.usecase.ExplainSentenceUseCase
import com.aienglish.feature.aiteacher.application.usecase.GenerateEnglishLessonUseCase
import org.koin.dsl.module

val teacherApplicationModule = module {
    factory { GenerateEnglishLessonUseCase(get()) }
    factory { EvaluateEnglishLessonUseCase(get()) }
    factory { ExplainSentenceUseCase(get()) }
}

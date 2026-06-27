package com.aienglish.feature.aiteacher.application.usecase

import com.aienglish.core.common.result.AppResult
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.repository.TeacherRepository

class GenerateEnglishLessonUseCase(private val repository: TeacherRepository) {
    suspend operator fun invoke(
        userId: String,
        topic: String,
        level: String
    ): AppResult<EnglishLesson> = repository.generateLesson(userId, topic, level)
}

package com.aienglish.feature.aiteacher.application.usecase

import com.aienglish.core.common.result.AppResult
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.repository.TeacherRepository

class EvaluateEnglishLessonUseCase(private val repository: TeacherRepository) {
    suspend operator fun invoke(
        userId: String,
        topic: String,
        answers: List<Pair<String, String>>,
        sentences: List<String>,
        level: String
    ): AppResult<EnglishFeedback> = repository.evaluateLesson(userId, topic, answers, sentences, level)
}

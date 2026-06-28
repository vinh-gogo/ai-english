package com.aienglish.feature.aiteacher.application.usecase

import com.aienglish.core.common.result.AppResult
import com.aienglish.feature.aiteacher.domain.repository.TeacherRepository

import com.aienglish.feature.aiteacher.domain.model.SentenceExplanation

class ExplainSentenceUseCase(private val repository: TeacherRepository) {
    suspend operator fun invoke(
        userId: String,
        vietnamese: String,
        targetEnglish: String,
        userAnswer: String,
        level: String
    ): AppResult<SentenceExplanation> = repository.explainSentence(userId, vietnamese, targetEnglish, userAnswer, level)
}

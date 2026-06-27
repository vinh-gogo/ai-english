package com.aienglish.feature.aiteacher.domain.repository

import com.aienglish.core.common.result.AppResult
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson

interface TeacherRepository {
    suspend fun generateLesson(
        userId: String,
        topic: String,
        level: String
    ): AppResult<EnglishLesson>

    suspend fun evaluateLesson(
        userId: String,
        topic: String,
        answers: List<Pair<String, String>>, // userAnswer, targetEnglish
        sentences: List<String>, // Vietnamese sentences
        level: String
    ): AppResult<EnglishFeedback>

    suspend fun explainSentence(
        userId: String,
        vietnamese: String,
        targetEnglish: String,
        userAnswer: String,
        level: String
    ): AppResult<String>
}

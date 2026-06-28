package com.aienglish.feature.aiteacher.domain.model

data class TeacherLessonHistory(
    val id: Long,
    val topic: String,
    val level: String,
    val score: Int?,
    val lesson: EnglishLesson?,
    val userAnswers: Map<Int, String>,
    val explanations: Map<Int, String>,
    val feedback: EnglishFeedback?
)

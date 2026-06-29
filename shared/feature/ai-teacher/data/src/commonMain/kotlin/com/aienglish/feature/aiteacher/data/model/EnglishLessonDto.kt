package com.aienglish.feature.aiteacher.data.model

import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.model.EnglishSentence
import com.aienglish.feature.aiteacher.domain.model.EnglishToken
import com.aienglish.feature.aiteacher.domain.model.SentenceExplanation
import kotlinx.serialization.Serializable

@Serializable
data class EnglishLessonDto(
    val sentences: List<EnglishSentenceDto>
) {
    fun toDomain(): EnglishLesson = EnglishLesson(
        sentences = sentences.map { it.toDomain() }
    )
}

@Serializable
data class EnglishSentenceDto(
    val vietnamese: String,
    val english: String,
    val alternatives: List<String> = emptyList(),
    val tokens: List<EnglishTokenDto> = emptyList(),
    val explanation: String = ""
) {
    fun toDomain(): EnglishSentence = EnglishSentence(
        vietnamese = vietnamese,
        english = english,
        alternatives = alternatives,
        tokens = tokens.map { it.toDomain() },
        explanation = explanation
    )
}

@Serializable
data class EnglishTokenDto(
    val word: String,
    val phonetic: String = "",
    val partOfSpeech: String = ""
) {
    fun toDomain(): EnglishToken = EnglishToken(
        word = word,
        phonetic = phonetic,
        partOfSpeech = partOfSpeech
    )
}

@Serializable
data class EnglishFeedbackDto(
    val score: Int,
    val review: String,
    val sentenceScores: List<Int> = emptyList()
) {
    fun toDomain(): EnglishFeedback = EnglishFeedback(
        score = score,
        review = review,
        sentenceScores = sentenceScores
    )
}

@Serializable
data class SentenceExplanationDto(
    val score: Int,
    val explanation: String
) {
    fun toDomain(): SentenceExplanation = SentenceExplanation(
        score = score,
        explanation = explanation
    )
}

// Domain to DTO mapping functions
fun EnglishLesson.toDto(): EnglishLessonDto = EnglishLessonDto(
    sentences = sentences.map { it.toDto() }
)

fun EnglishSentence.toDto(): EnglishSentenceDto = EnglishSentenceDto(
    vietnamese = vietnamese,
    english = english,
    alternatives = alternatives,
    tokens = tokens.map { it.toDto() },
    explanation = explanation
)

fun EnglishToken.toDto(): EnglishTokenDto = EnglishTokenDto(
    word = word,
    phonetic = phonetic,
    partOfSpeech = partOfSpeech
)

fun EnglishFeedback.toDto(): EnglishFeedbackDto = EnglishFeedbackDto(
    score = score,
    review = review,
    sentenceScores = sentenceScores
)

package com.aienglish.feature.grammargraph.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GrammarNode(
    val id: String,
    val pattern: String,
    val meaning: String,
    val explanation: String,
    val example: String,
    val level: String = "B1",
    val isSaved: Boolean = false,
    val historyId: String? = null
)

package com.aienglish.feature.grammargraph.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AiGraphResponse(
    val nodes: List<AiNodeResponse> = emptyList(),
    val edges: List<AiEdgeResponse> = emptyList()
)

@Serializable
data class AiNodeResponse(
    val id: String, // String to support decimals like "1.1"
    val pattern: String,
    val meaning: String,
    val explanation: String,
    val example: String
)

@Serializable
data class AiEdgeResponse(
    val from: String,
    val to: String,
    val relation: String,
    val note: String? = null
)

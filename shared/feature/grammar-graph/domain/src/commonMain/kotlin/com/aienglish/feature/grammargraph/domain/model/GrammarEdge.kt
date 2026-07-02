package com.aienglish.feature.grammargraph.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GrammarEdge(
    val id: String,
    val fromGrammarId: String,
    val toGrammarId: String,
    val edgeType: String = "PREREQUISITE"
)

package com.aienglish.feature.grammargraph.domain.repository

import com.aienglish.feature.grammargraph.domain.model.GrammarEdge
import com.aienglish.feature.grammargraph.domain.model.GrammarNode

interface GrammarGraphRepository {
    suspend fun getGrammarNodes(): List<GrammarNode>
    suspend fun getGrammarEdges(): List<GrammarEdge>
}

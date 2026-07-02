package com.aienglish.feature.grammargraph.application.usecase

import com.aienglish.feature.grammargraph.domain.model.GrammarEdge
import com.aienglish.feature.grammargraph.domain.model.GrammarNode
import com.aienglish.feature.grammargraph.domain.repository.GrammarGraphRepository

class GetGrammarGraphUseCase(
    private val repository: GrammarGraphRepository
) {
    suspend fun getNodes(): List<GrammarNode> {
        return repository.getGrammarNodes()
    }

    suspend fun getEdges(): List<GrammarEdge> {
        return repository.getGrammarEdges()
    }
}

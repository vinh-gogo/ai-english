package com.aienglish.feature.grammargraph.ui.viewmodel

import androidx.compose.ui.geometry.Offset
import com.aienglish.core.common.viewmodel.BaseViewModel
import com.aienglish.feature.grammargraph.application.usecase.GetGrammarGraphUseCase
import com.aienglish.feature.grammargraph.domain.model.GrammarEdge
import com.aienglish.feature.grammargraph.domain.model.GrammarNode
import com.aienglish.feature.grammargraph.domain.repository.GrammarGraphRepository
import kotlinx.coroutines.launch

data class GrammarGraphUiState(
    val isLoading: Boolean = false,
    val nodes: List<GrammarNode> = emptyList(),
    val edges: List<GrammarEdge> = emptyList(),
    val selectedNode: GrammarNode? = null,
    val focusedConnections: Set<String> = emptySet(),
    val nodePositions: Map<String, Offset> = emptyMap(),
    val selectedGroup: String = "Tất cả",
    val groups: List<String> = listOf("Tất cả", "Cơ bản", "Nâng cao"),
    val editingGroup: String? = null,
    val allSavedNodes: List<GrammarNode> = emptyList(),
    val isConfigMode: Boolean = false
)

class GrammarGraphViewModel(
    private val getGrammarGraph: GetGrammarGraphUseCase,
    private val repository: GrammarGraphRepository
) : BaseViewModel<GrammarGraphUiState>(GrammarGraphUiState()) {

    fun selectGroup(group: String) {
        setState {
            copy(
                selectedGroup = group,
                selectedNode = null,
                focusedConnections = emptySet(),
                editingGroup = null
            )
        }
    }

    fun addGroup() {
        val newGroupName = "Nhóm mới ${currentState.groups.size - 1}"
        viewModelScope.launch {
            repository.addGrammarGroup(newGroupName)
            val updatedGroups = repository.getGrammarGroups()
            setState {
                copy(
                    groups = updatedGroups,
                    selectedGroup = newGroupName,
                    selectedNode = null,
                    focusedConnections = emptySet(),
                    editingGroup = newGroupName
                )
            }
        }
    }

    fun startEditingGroup(group: String) {
        if (group == "Tất cả" || group == "+") return
        setState {
            copy(editingGroup = group)
        }
    }

    fun renameGroup(oldName: String, newName: String) {
        val trimmed = newName.trim()
        if (trimmed.isEmpty() || trimmed == oldName || trimmed in currentState.groups) {
            setState { copy(editingGroup = null) }
            return
        }
        viewModelScope.launch {
            repository.renameGrammarGroup(oldName, trimmed)
            val updatedGroups = repository.getGrammarGroups()
            val updatedSelected = if (currentState.selectedGroup == oldName) trimmed else currentState.selectedGroup
            setState {
                copy(
                    groups = updatedGroups,
                    selectedGroup = updatedSelected,
                    editingGroup = null
                )
            }
        }
    }

    fun cancelEditing() {
        setState { copy(editingGroup = null) }
    }

    fun toggleConfigMode() {
        setState { copy(isConfigMode = !isConfigMode) }
    }

    fun deleteGroup(name: String) {
        viewModelScope.launch {
            repository.deleteGrammarGroup(name)
            val updatedGroups = repository.getGrammarGroups()
            val nextSelectedGroup = if (currentState.selectedGroup == name) "Tất cả" else currentState.selectedGroup
            setState {
                copy(
                    groups = updatedGroups,
                    selectedGroup = nextSelectedGroup
                )
            }
        }
    }

    fun loadGraph(targetLanguage: String) {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val nodes = getGrammarGraph.getNodes().filter {
                    if (targetLanguage == "vi") {
                        it.id in listOf("S_COCOP", "PAST_DA", "FUTURE_SE", "MUST_PHAI", "COND_NEU")
                    } else {
                        it.id in listOf("S_V_O", "S_V_ADJ", "PRES_PERF", "MODAL", "COND_1", "USED_TO", "WISH_PAST")
                    }
                }
                
                val edges = getGrammarGraph.getEdges().filter { edge ->
                    nodes.any { it.id == edge.fromGrammarId } && nodes.any { it.id == edge.toGrammarId }
                }

                // Get dynamic groups list from DB
                val groups = repository.getGrammarGroups()

                // Layout initial coordinates nicely
                val positions = nodes.mapIndexed { index, node ->
                    val x = 100f + (index % 2) * 420f + (if (index % 3 == 0) 40f else 0f)
                    val y = 80f + (index / 2) * 300f
                    node.id to Offset(x, y)
                }.toMap()

                val allDbNodes = getGrammarGraph.getNodes()
                val savedNodes = allDbNodes.filter { it.isSaved }

                setState {
                    copy(
                        isLoading = false,
                        nodes = nodes,
                        edges = edges,
                        nodePositions = positions,
                        selectedNode = null,
                        focusedConnections = emptySet(),
                        groups = groups,
                        allSavedNodes = savedNodes
                    )
                }
            } catch (e: Exception) {
                setState { copy(isLoading = false) }
            }
        }
    }

    fun updateNodePosition(nodeId: String, offset: Offset) {
        setState {
            copy(nodePositions = nodePositions + (nodeId to offset))
        }
    }

    fun addSavedNodeToGraph(node: GrammarNode) {
        if (currentState.nodes.any { it.id == node.id }) return
        
        // Compute layout coordinates relative to center of virtual workspace
        val x = 1000f + (kotlin.random.Random.nextFloat() - 0.5f) * 200f
        val y = 800f + (kotlin.random.Random.nextFloat() - 0.5f) * 200f
        
        viewModelScope.launch {
            try {
                val allEdges = getGrammarGraph.getEdges()
                val newEdges = allEdges.filter { edge ->
                    (edge.fromGrammarId == node.id && currentState.nodes.any { it.id == edge.toGrammarId }) ||
                    (edge.toGrammarId == node.id && currentState.nodes.any { it.id == edge.fromGrammarId })
                }
                
                setState {
                    copy(
                        nodes = nodes + node,
                        edges = edges + newEdges,
                        nodePositions = nodePositions + (node.id to Offset(x, y))
                    )
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    fun removeNodeFromGraph(nodeId: String) {
        setState {
            val updatedNodes = nodes.filter { it.id != nodeId }
            val updatedEdges = edges.filter { it.fromGrammarId != nodeId && it.toGrammarId != nodeId }
            val updatedPositions = nodePositions - nodeId
            copy(
                nodes = updatedNodes,
                edges = updatedEdges,
                nodePositions = updatedPositions,
                selectedNode = null,
                focusedConnections = emptySet()
            )
        }
    }

    fun selectNode(node: GrammarNode?) {
        if (node == null) {
            setState { copy(selectedNode = null, focusedConnections = emptySet()) }
            return
        }
        
        val connectedIds = mutableSetOf<String>()
        currentState.edges.forEach { edge ->
            if (edge.fromGrammarId == node.id) {
                connectedIds.add(edge.toGrammarId)
            }
            if (edge.toGrammarId == node.id) {
                connectedIds.add(edge.fromGrammarId)
            }
        }
        connectedIds.add(node.id)

        setState {
            copy(
                selectedNode = node,
                focusedConnections = connectedIds
            )
        }
    }
}

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
    val groups: List<String> = listOf("Tất cả"),
    val editingGroup: String? = null,
    val allSavedNodes: List<GrammarNode> = emptyList(),
    val isConfigMode: Boolean = false,
    val groupNodeIds: Map<String, List<String>> = emptyMap(),
    val isGeneratingAi: Boolean = false,
    val aiErrorMessage: String? = null
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
            val updatedGroupNodeIds = currentState.groupNodeIds + (newGroupName to emptyList())
            setState {
                copy(
                    groups = updatedGroups,
                    selectedGroup = newGroupName,
                    selectedNode = null,
                    focusedConnections = emptySet(),
                    editingGroup = newGroupName,
                    groupNodeIds = updatedGroupNodeIds
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
            val updatedGroupNodeIds = currentState.groupNodeIds.toMutableMap()
            val existingIds = updatedGroupNodeIds.remove(oldName)
            if (existingIds != null) {
                updatedGroupNodeIds[trimmed] = existingIds
            }
            setState {
                copy(
                    groups = updatedGroups,
                    selectedGroup = updatedSelected,
                    editingGroup = null,
                    groupNodeIds = updatedGroupNodeIds
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
            val updatedGroupNodeIds = currentState.groupNodeIds - name
            setState {
                copy(
                    groups = updatedGroups,
                    selectedGroup = nextSelectedGroup,
                    groupNodeIds = updatedGroupNodeIds
                )
            }
        }
    }

    fun loadGraph(targetLanguage: String) {
        setState { copy(isLoading = true) }
        viewModelScope.launch {
            try {
                // Get dynamic groups list from DB, ensure "Tất cả" is always present
                val dbGroups = repository.getGrammarGroups()
                val groups = if (dbGroups.contains("Tất cả")) dbGroups else listOf("Tất cả") + dbGroups

                // Load custom group mappings from DB
                val groupNodeIdsMap = mutableMapOf<String, List<String>>()
                groups.forEach { group ->
                    if (group != "Tất cả") {
                        val nodeIds = repository.getNodesForGroup(group)
                        groupNodeIdsMap[group] = nodeIds
                    }
                }

                val allDbNodes = getGrammarGraph.getNodes()
                val savedNodes = allDbNodes.filter { it.isSaved }

                val defaultNodes = allDbNodes.filter {
                    it.id in listOf("S_V_O", "S_V_ADJ", "PRES_PERF", "MODAL", "COND_1", "USED_TO", "WISH_PAST")
                }

                // Map of all custom node IDs currently assigned to any group
                val customGroupMappedIds = groupNodeIdsMap.values.flatten().toSet()

                // Combined nodes list that must be available in memory (default ones + any nodes mapped to user groups + all saved nodes)
                val nodes = (defaultNodes + allDbNodes.filter { it.id in customGroupMappedIds } + savedNodes).distinctBy { it.id }

                val edges = getGrammarGraph.getEdges().filter { edge ->
                    nodes.any { it.id == edge.fromGrammarId } && nodes.any { it.id == edge.toGrammarId }
                }

                // Layout coordinates nicely. If a node position is already saved/known keep it, otherwise compute a layout slot.
                val positions = nodes.mapIndexed { index, node ->
                    val x = 100f + (index % 2) * 420f + (if (index % 3 == 0) 40f else 0f)
                    val y = 80f + (index / 2) * 300f
                    node.id to (currentState.nodePositions[node.id] ?: Offset(x, y))
                }.toMap()

                setState {
                    copy(
                        isLoading = false,
                        nodes = nodes,
                        edges = edges,
                        nodePositions = positions,
                        selectedNode = null,
                        focusedConnections = emptySet(),
                        groups = groups,
                        allSavedNodes = savedNodes,
                        groupNodeIds = groupNodeIdsMap
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
        val selected = currentState.selectedGroup
        
        // Compute layout coordinates relative to center of virtual workspace
        val x = 1000f + (kotlin.random.Random.nextFloat() - 0.5f) * 200f
        val y = 800f + (kotlin.random.Random.nextFloat() - 0.5f) * 200f
        
        viewModelScope.launch {
            try {
                // If it is a custom group, save mapping to database
                if (selected != "Tất cả") {
                    repository.addNodeToGroup(selected, node)
                }
                
                // Update local map state
                val updatedGroupNodeIds = if (selected != "Tất cả") {
                    val currentIds = currentState.groupNodeIds[selected] ?: emptyList()
                    currentState.groupNodeIds + (selected to (currentIds + node.id).distinct())
                } else {
                    currentState.groupNodeIds
                }
                
                setState {
                    copy(
                        nodes = if (nodes.any { it.id == node.id }) nodes else nodes + node,
                        nodePositions = nodePositions + (node.id to Offset(x, y)),
                        groupNodeIds = updatedGroupNodeIds
                    )
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    fun removeNodeFromGraph(nodeId: String) {
        val selected = currentState.selectedGroup
        viewModelScope.launch {
            try {
                if (selected != "Tất cả") {
                    repository.removeNodeFromGroup(selected, nodeId)
                }
                
                val updatedGroupNodeIds = if (selected != "Tất cả") {
                    val currentIds = currentState.groupNodeIds[selected] ?: emptyList()
                    currentState.groupNodeIds + (selected to currentIds.filter { it != nodeId })
                } else {
                    currentState.groupNodeIds
                }
                
                setState {
                    val updatedNodes = nodes.filter { it.id != nodeId }
                    val updatedEdges = edges.filter { it.fromGrammarId != nodeId && it.toGrammarId != nodeId }
                    val updatedPositions = nodePositions - nodeId
                    copy(
                        nodes = updatedNodes,
                        edges = updatedEdges,
                        nodePositions = updatedPositions,
                        selectedNode = null,
                        focusedConnections = emptySet(),
                        groupNodeIds = updatedGroupNodeIds
                    )
                }
            } catch (e: Exception) {
                // Ignore
            }
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
    
    fun generateAiGraphForCurrentGroup(targetLanguage: String) {
        val group = currentState.selectedGroup
        if (group == "Tất cả") {
            setState { copy(aiErrorMessage = "Tính năng sinh AI chỉ hỗ trợ cho Custom Group để tránh quá tải.") }
            return
        }
        
        val groupNodeIds = currentState.groupNodeIds[group] ?: emptyList()
        val nodesToProcess = currentState.nodes.filter { it.id in groupNodeIds }
        
        if (nodesToProcess.isEmpty()) {
            setState { copy(aiErrorMessage = "Nhóm này chưa có kiến thức nào để phân tích.") }
            return
        }
        
        if (nodesToProcess.size > 20) {
            setState { copy(aiErrorMessage = "Nhóm có quá nhiều kiến thức (>20). Vui lòng chia nhỏ để AI phân tích chính xác hơn.") }
            return
        }
        
        viewModelScope.launch {
            setState { copy(isGeneratingAi = true, aiErrorMessage = null) }
            try {
                val aiResponse = repository.generateAiGraph(nodesToProcess)
                repository.saveAiGeneratedGraph(aiResponse, nodesToProcess, group)
                
                // Reload graph
                loadGraph(targetLanguage)
            } catch (e: Exception) {
                setState { copy(aiErrorMessage = "Lỗi khi phân tích bằng AI: ${e.message}") }
            } finally {
                setState { copy(isGeneratingAi = false) }
            }
        }
    }

    fun clearAiError() {
        setState { copy(aiErrorMessage = null) }
    }
}

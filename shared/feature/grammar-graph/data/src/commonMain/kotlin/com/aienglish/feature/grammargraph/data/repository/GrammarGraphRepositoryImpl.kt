package com.aienglish.feature.grammargraph.data.repository

import app.cash.sqldelight.db.SqlDriver
import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.grammargraph.domain.model.GrammarEdge
import com.aienglish.feature.grammargraph.domain.model.GrammarNode
import com.aienglish.feature.grammargraph.domain.repository.GrammarGraphRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class GrammarGraphRepositoryImpl(
    private val database: AIEnglishDatabase,
    private val driver: SqlDriver
) : GrammarGraphRepository {

    private val queries = database.lessonHistoryQueries



    override suspend fun getGrammarNodes(): List<GrammarNode> = withContext(Dispatchers.IO) {
        
        val dbNodes = try {
            queries.getAllGrammarStructures().executeAsList().map {
                GrammarNode(
                    id = it.id,
                    pattern = it.pattern,
                    meaning = it.meaning_key,
                    explanation = it.explanation_key,
                    example = it.example,
                    level = it.level,
                    isSaved = false
                )
            }
        } catch (e: Exception) {
            emptyList()
        }

        // Saved notes from DB to check if bookmarked
        val savedNoteRows = try {
            queries.getAllSavedQuestionNotes().executeAsList()
        } catch (e: Exception) {
            emptyList()
        }

        val mappedNodes = dbNodes.map { node ->
            val isSaved = savedNoteRows.any { row -> 
                row.note.contains(node.pattern, ignoreCase = true) || 
                row.note.contains(node.id, ignoreCase = true)
            }
            node.copy(isSaved = isSaved)
        }

        // Filter out saved notes that do not match any pre-seeded structures
        val unmatchedNoteRows = savedNoteRows.filter { row ->
            mappedNodes.none { node ->
                row.note.contains(node.pattern, ignoreCase = true) || 
                row.note.contains(node.id, ignoreCase = true) ||
                node.pattern.contains(row.note, ignoreCase = true)
            }
        }.distinctBy { it.note }

        // Dynamically synthesize nodes for custom saved structures so they can be added to the graph
        val dynamicNodes = unmatchedNoteRows.map { row ->
            GrammarNode(
                id = row.id,
                pattern = row.note,
                meaning = "Cấu trúc đã lưu",
                explanation = "Cấu trúc ngữ pháp đã lưu từ lịch sử bài học của bạn.",
                example = "Nhấp để chọn và học cấu trúc này.",
                level = "B1",
                isSaved = true
            )
        }

        mappedNodes + dynamicNodes
    }

    override suspend fun getGrammarEdges(): List<GrammarEdge> = withContext(Dispatchers.IO) {
        
        try {
            queries.getAllGrammarEdges().executeAsList().map {
                GrammarEdge(
                    id = it.id,
                    fromGrammarId = it.from_grammar_id,
                    toGrammarId = it.to_grammar_id,
                    edgeType = it.edge_type
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getGrammarGroups(): List<String> = withContext(Dispatchers.IO) {
        
        try {
            queries.getAllGrammarGroups().executeAsList().map { it.name }
        } catch (e: Exception) {
            listOf("Tất cả", "Cơ bản", "Nâng cao")
        }
    }

    @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
    override suspend fun addGrammarGroup(name: String): Unit = withContext(Dispatchers.IO) {
        
        try {
            queries.insertGrammarGroup(
                id = kotlin.uuid.Uuid.random().toString(),
                name = name,
                created_at = Clock.System.now().toEpochMilliseconds()
            )
        } catch (e: Exception) {
            // Ignore duplicate insert errors
        }
        Unit
    }

    override suspend fun renameGrammarGroup(oldName: String, newName: String): Unit = withContext(Dispatchers.IO) {
        try {
            queries.updateGrammarGroupName(newName = newName, oldName = oldName)
        } catch (e: Exception) {
            // Ignore error
        }
        Unit
    }

    override suspend fun deleteGrammarGroup(name: String): Unit = withContext(Dispatchers.IO) {
        try {
            queries.deleteGrammarGroup(name)
        } catch (e: Exception) {
            // Ignore error
        }
        Unit
    }

    override suspend fun getNodesForGroup(groupName: String): List<String> = withContext(Dispatchers.IO) {
        try {
            queries.getNodesForGroupName(groupName).executeAsList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addNodeToGroup(groupName: String, node: GrammarNode): Unit = withContext(Dispatchers.IO) {
        try {
            val allStructures = queries.getAllGrammarStructures().executeAsList()
            val exists = allStructures.any { it.id == node.id }
            if (!exists) {
                queries.insertGrammarStructure(
                    id = node.id,
                    pattern = node.pattern,
                    meaning_key = node.meaning,
                    explanation_key = node.explanation,
                    example = node.example,
                    level = node.level,
                    created_at = Clock.System.now().toEpochMilliseconds()
                )
            }
            queries.insertGroupNodeMapByName(groupName = groupName, grammarId = node.id)
        } catch (e: Exception) {
            // Ignore
        }
        Unit
    }

    override suspend fun removeNodeFromGroup(groupName: String, grammarId: String): Unit = withContext(Dispatchers.IO) {
        try {
            queries.deleteGroupNodeMapByName(groupName = groupName, grammarId = grammarId)
        } catch (e: Exception) {
            // Ignore
        }
        Unit
    }

    companion object {
    }
}

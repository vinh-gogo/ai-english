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

    @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
    private suspend fun checkAndSeedDatabase() = withContext(Dispatchers.IO) {
        // Safe check to verify that all columns (like example and level) and tables exist
        try {
            val allStructures = queries.getAllGrammarStructures().executeAsList()
            queries.getAllGrammarEdges().executeAsList()
            queries.getAllGrammarGroups().executeAsList()
            queries.getNodesForGroupName("Tất cả").executeAsList()
            
            // If database contains old Vietnamese nodes or legacy groups, force recreate tables to clean them up
            val hasVietnamese = allStructures.any { it.id == "MUST_PHAI" || it.id == "S_COCOP" }
            val allGroups = queries.getAllGrammarGroups().executeAsList()
            val hasLegacyGroups = allGroups.any { it.name == "Cơ bản" || it.name == "Nâng cao" }
            if (hasVietnamese || hasLegacyGroups) {
                throw IllegalStateException("Purging legacy database structures.")
            }
        } catch (e: Exception) {
            // Recreate grammar tables if there is a schema mismatch
            try {
                database.transaction {
                    driver.execute(null, "DROP TABLE IF EXISTS grammar_group_node_map", 0)
                    driver.execute(null, "DROP TABLE IF EXISTS grammar_edges", 0)
                    driver.execute(null, "DROP TABLE IF EXISTS grammar_structures", 0)
                    driver.execute(null, "DROP TABLE IF EXISTS grammar_groups", 0)
                    
                    // Recreate tables with correct latest schema
                    AIEnglishDatabase.Schema.create(driver)
                }
            } catch (ex: Exception) {
                // Ignore failure
            }
        }

        // 1. Seed grammar structures (nodes) if empty
        val existingNodes = try { queries.getAllGrammarStructures().executeAsList() } catch (e: Exception) { emptyList() }
        if (existingNodes.isEmpty()) {
            defaultPredefinedNodes.forEach { node ->
                try {
                    queries.insertGrammarStructure(
                        id = node.id,
                        pattern = node.pattern,
                        meaning_key = node.meaning,
                        explanation_key = node.explanation,
                        example = node.example,
                        level = node.level,
                        created_at = Clock.System.now().toEpochMilliseconds()
                    )
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }

        // 2. Seed grammar edges if empty
        val existingEdges = try { queries.getAllGrammarEdges().executeAsList() } catch (e: Exception) { emptyList() }
        if (existingEdges.isEmpty()) {
            defaultPredefinedEdges.forEach { edge ->
                try {
                    queries.insertGrammarEdge(
                        id = edge.id,
                        from_grammar_id = edge.fromGrammarId,
                        to_grammar_id = edge.toGrammarId,
                        edge_type = edge.edgeType,
                        created_at = Clock.System.now().toEpochMilliseconds()
                    )
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }

        // 3. Seed grammar groups if empty
        val existingGroups = try { queries.getAllGrammarGroups().executeAsList() } catch (e: Exception) { emptyList() }
        if (existingGroups.isEmpty()) {
            listOf("Tất cả").forEachIndexed { index, groupName ->
                try {
                    queries.insertGrammarGroup(
                        id = kotlin.uuid.Uuid.random().toString(),
                        name = groupName,
                        created_at = Clock.System.now().toEpochMilliseconds() + index
                    )
                } catch (e: Exception) {
                    // Ignore
                }
            }
        }
    }

    override suspend fun getGrammarNodes(): List<GrammarNode> = withContext(Dispatchers.IO) {
        checkAndSeedDatabase()
        
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
        val savedNotes = try {
            queries.getAllSavedQuestionNotes().executeAsList().map { it.note }
        } catch (e: Exception) {
            emptyList()
        }

        val mappedNodes = dbNodes.map { node ->
            val isSaved = savedNotes.any { note -> 
                note.contains(node.pattern, ignoreCase = true) || 
                note.contains(node.id, ignoreCase = true)
            }
            node.copy(isSaved = isSaved)
        }

        // Filter out saved notes that do not match any pre-seeded structures
        val unmatchedNotes = savedNotes.filter { note ->
            mappedNodes.none { node ->
                note.contains(node.pattern, ignoreCase = true) || 
                note.contains(node.id, ignoreCase = true) ||
                node.pattern.contains(note, ignoreCase = true)
            }
        }.distinct()

        // Dynamically synthesize nodes for custom saved structures so they can be added to the graph
        val dynamicNodes = unmatchedNotes.map { note ->
            GrammarNode(
                id = note,
                pattern = note,
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
        checkAndSeedDatabase()
        
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
        checkAndSeedDatabase()
        
        try {
            queries.getAllGrammarGroups().executeAsList().map { it.name }
        } catch (e: Exception) {
            listOf("Tất cả", "Cơ bản", "Nâng cao")
        }
    }

    @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
    override suspend fun addGrammarGroup(name: String): Unit = withContext(Dispatchers.IO) {
        checkAndSeedDatabase()
        
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
        checkAndSeedDatabase()
        try {
            queries.updateGrammarGroupName(newName = newName, oldName = oldName)
        } catch (e: Exception) {
            // Ignore error
        }
        Unit
    }

    override suspend fun deleteGrammarGroup(name: String): Unit = withContext(Dispatchers.IO) {
        checkAndSeedDatabase()
        try {
            queries.deleteGrammarGroup(name)
        } catch (e: Exception) {
            // Ignore error
        }
        Unit
    }

    override suspend fun getNodesForGroup(groupName: String): List<String> = withContext(Dispatchers.IO) {
        checkAndSeedDatabase()
        try {
            queries.getNodesForGroupName(groupName).executeAsList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addNodeToGroup(groupName: String, grammarId: String): Unit = withContext(Dispatchers.IO) {
        checkAndSeedDatabase()
        try {
            val allStructures = queries.getAllGrammarStructures().executeAsList()
            val exists = allStructures.any { it.id == grammarId }
            if (!exists) {
                queries.insertGrammarStructure(
                    id = grammarId,
                    pattern = grammarId,
                    meaning_key = "Cấu trúc đã lưu",
                    explanation_key = "Cấu trúc ngữ pháp đã lưu từ lịch sử bài học của bạn.",
                    example = "Nhấp để chọn và học cấu trúc này.",
                    level = "B1",
                    created_at = Clock.System.now().toEpochMilliseconds()
                )
            }
            queries.insertGroupNodeMapByName(groupName = groupName, grammarId = grammarId)
        } catch (e: Exception) {
            // Ignore
        }
        Unit
    }

    override suspend fun removeNodeFromGroup(groupName: String, grammarId: String): Unit = withContext(Dispatchers.IO) {
        checkAndSeedDatabase()
        try {
            queries.deleteGroupNodeMapByName(groupName = groupName, grammarId = grammarId)
        } catch (e: Exception) {
            // Ignore
        }
        Unit
    }

    companion object {
        val defaultPredefinedNodes = listOf(
            // English default nodes
            GrammarNode(
                id = "S_V_O",
                pattern = "S + V + O",
                meaning = "Cấu trúc câu cơ bản gồm Chủ ngữ + Động từ + Tân ngữ.",
                explanation = "Dùng để diễn tả một hành động thông thường tác động lên một đối tượng trực tiếp.",
                example = "I love learning English.",
                level = "A1"
            ),
            GrammarNode(
                id = "S_V_ADJ",
                pattern = "S + linking V + Adj",
                meaning = "Cấu trúc miêu tả trạng thái, tính chất.",
                explanation = "Linking verb (như be, look, seem, feel) liên kết chủ ngữ với tính từ bổ nghĩa cho chủ ngữ.",
                example = "She looks beautiful today.",
                level = "A1"
            ),
            GrammarNode(
                id = "PRES_PERF",
                pattern = "S + has/have + V3/ed",
                meaning = "Thì hiện tại hoàn thành.",
                explanation = "Dùng để diễn tả hành động đã xảy ra trong quá khứ kéo dài đến hiện tại hoặc để lại kết quả ở hiện tại.",
                example = "They have lived here for 5 years.",
                level = "B1"
            ),
            GrammarNode(
                id = "MODAL",
                pattern = "S + Mod + V",
                meaning = "Động từ khuyết thiếu.",
                explanation = "Dùng để chỉ khả năng, sự bắt buộc, lời khuyên (can, must, should, would).",
                example = "You should study harder.",
                level = "A2"
            ),
            GrammarNode(
                id = "COND_1",
                pattern = "If + S + V(s/es), S + will + V",
                meaning = "Câu điều kiện loại 1.",
                explanation = "Diễn tả giả thiết có thể xảy ra ở hiện tại hoặc tương lai.",
                example = "If it rains, we will stay home.",
                level = "B1"
            ),
            GrammarNode(
                id = "USED_TO",
                pattern = "S + used to + V",
                meaning = "Thói quen trong quá khứ.",
                explanation = "Diễn tả một hành động thường xuyên làm trong quá khứ nhưng nay không còn nữa.",
                example = "We used to play football when we were kids.",
                level = "A2"
            ),
            GrammarNode(
                id = "WISH_PAST",
                pattern = "S + wish + S + V2/ed",
                meaning = "Câu ước muốn ở hiện tại.",
                explanation = "Diễn tả mong muốn trái ngược với thực tế ở hiện tại.",
                example = "I wish I had more free time.",
                level = "B2"
            )
        )

        val defaultPredefinedEdges = listOf(
            GrammarEdge("e1", "S_V_O", "MODAL"),
            GrammarEdge("e2", "S_V_O", "PRES_PERF"),
            GrammarEdge("e3", "MODAL", "COND_1"),
            GrammarEdge("e4", "S_V_O", "S_V_ADJ"),
            GrammarEdge("e5", "PRES_PERF", "USED_TO"),
            GrammarEdge("e6", "COND_1", "WISH_PAST")
        )
    }
}

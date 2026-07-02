package com.aienglish.feature.grammargraph.data.repository

import com.aienglish.core.database.AIEnglishDatabase
import com.aienglish.feature.grammargraph.domain.model.GrammarEdge
import com.aienglish.feature.grammargraph.domain.model.GrammarNode
import com.aienglish.feature.grammargraph.domain.repository.GrammarGraphRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class GrammarGraphRepositoryImpl(
    private val database: AIEnglishDatabase
) : GrammarGraphRepository {

    private val queries = database.lessonHistoryQueries

    @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
    private suspend fun checkAndSeedDatabase() = withContext(Dispatchers.IO) {
        // 1. Seed grammar structures (nodes) if empty
        val existingNodes = queries.getAllGrammarStructures().executeAsList()
        if (existingNodes.isEmpty()) {
            defaultPredefinedNodes.forEach { node ->
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
        }

        // 2. Seed grammar edges if empty
        val existingEdges = queries.getAllGrammarEdges().executeAsList()
        if (existingEdges.isEmpty()) {
            defaultPredefinedEdges.forEach { edge ->
                queries.insertGrammarEdge(
                    id = edge.id,
                    from_grammar_id = edge.fromGrammarId,
                    to_grammar_id = edge.toGrammarId,
                    edge_type = edge.edgeType,
                    created_at = Clock.System.now().toEpochMilliseconds()
                )
            }
        }

        // 3. Seed grammar groups if empty
        val existingGroups = queries.getAllGrammarGroups().executeAsList()
        if (existingGroups.isEmpty()) {
            listOf("Tất cả", "Cơ bản", "Nâng cao").forEachIndexed { index, groupName ->
                queries.insertGrammarGroup(
                    id = kotlin.uuid.Uuid.random().toString(),
                    name = groupName,
                    created_at = Clock.System.now().toEpochMilliseconds() + index
                )
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

        dbNodes.map { node ->
            val isSaved = savedNotes.any { note -> 
                note.contains(node.pattern, ignoreCase = true) || 
                note.contains(node.id, ignoreCase = true)
            }
            node.copy(isSaved = isSaved)
        }
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
            val group = queries.getAllGrammarGroups().executeAsList().find { it.name == oldName }
            if (group != null) {
                queries.updateGrammarGroupName(newName, group.id)
            }
        } catch (e: Exception) {
            // Ignore error
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
            ),
            
            // Vietnamese default nodes
            GrammarNode(
                id = "S_COCOP",
                pattern = "S + là + Noun/Adj",
                meaning = "Cấu trúc định nghĩa hệ từ 'là'.",
                explanation = "Liên kết chủ ngữ với danh từ hoặc tính từ để giới thiệu hoặc miêu tả danh tính.",
                example = "Anh ấy là kỹ sư.",
                level = "A1"
            ),
            GrammarNode(
                id = "PAST_DA",
                pattern = "S + đã + V",
                meaning = "Trạng thái quá khứ.",
                explanation = "Thêm từ chỉ thời gian 'đã' trước động từ để diễn tả hành động đã xảy ra.",
                example = "Chúng tôi đã hoàn thành bài tập.",
                level = "A1"
            ),
            GrammarNode(
                id = "FUTURE_SE",
                pattern = "S + sẽ + V",
                meaning = "Trạng thái tương lai.",
                explanation = "Thêm từ 'sẽ' trước động từ để chỉ một dự định hoặc kế hoạch trong tương lai.",
                example = "Ngày mai tôi sẽ đi Hà Nội.",
                level = "A2"
            ),
            GrammarNode(
                id = "MUST_PHAI",
                pattern = "S + phải + V",
                meaning = "Cấu trúc bắt buộc.",
                explanation = "Biểu thị bổn phận, trách nhiệm bắt buộc phải thực hiện hành động.",
                example = "Bạn phải làm bài tập về nhà.",
                level = "A2"
            ),
            GrammarNode(
                id = "COND_NEU",
                pattern = "Nếu + S + V, thì + S + V",
                meaning = "Câu điều kiện.",
                explanation = "Diễn tả mối quan hệ nguyên nhân - kết quả, giả thiết - hệ quả.",
                example = "Nếu trời mưa thì tôi nghỉ học.",
                level = "B1"
            )
        )

        val defaultPredefinedEdges = listOf(
            GrammarEdge("e1", "S_V_O", "MODAL"),
            GrammarEdge("e2", "S_V_O", "PRES_PERF"),
            GrammarEdge("e3", "MODAL", "COND_1"),
            GrammarEdge("e4", "S_V_O", "S_V_ADJ"),
            GrammarEdge("e5", "PRES_PERF", "USED_TO"),
            GrammarEdge("e6", "COND_1", "WISH_PAST"),
            
            // Vietnamese Edges
            GrammarEdge("e7", "S_V_O", "PAST_DA"),
            GrammarEdge("e8", "S_V_O", "FUTURE_SE"),
            GrammarEdge("e9", "S_V_O", "S_COCOP"),
            GrammarEdge("e10", "FUTURE_SE", "COND_NEU"),
            GrammarEdge("e11", "PAST_DA", "MUST_PHAI")
        )
    }
}

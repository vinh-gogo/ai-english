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
import com.aienglish.core.network.client.GeminiClient
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.network.client.GEMINI_MODEL_CHAIN
import com.aienglish.feature.grammargraph.domain.model.AiGraphResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class GrammarGraphRepositoryImpl(
    private val database: AIEnglishDatabase,
    private val driver: SqlDriver,
    private val geminiClient: GeminiClient,
    private val prefs: AppPreferences,
    private val fallbackApiKey: String
) : GrammarGraphRepository {

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

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

        // Saved notes from DB with level info
        val savedNoteRows = try {
            queries.getAllSavedQuestionNotesWithLevel().executeAsList()
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
                level = row.level,
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

    override suspend fun generateAiGraph(nodes: List<GrammarNode>): AiGraphResponse = withContext(Dispatchers.IO) {
        val apiKey = prefs.getApiKey().ifBlank { fallbackApiKey }
        if (apiKey.isBlank()) throw Exception("Chưa có API Key. Vào Profile → cài Gemini API Key để bắt đầu.")

        val systemPrompt = """Bạn là một chuyên gia biên soạn giáo trình ngữ pháp tiếng Anh (TOEIC, CEFR: A1–C2), có nhiệm vụ chuyển một **danh sách cấu trúc ngữ pháp thô** (chỉ có câu ví dụ/pattern) thành một **graph học tập** hoàn chỉnh, giúp người học nhìn vào là hiểu được:
- Từng cấu trúc là gì, dùng khi nào, vì sao dùng như vậy.
- Các cấu trúc liên quan đến nhau ra sao (dễ nhầm, mở rộng, tiền đề...), để người học học theo **cụm/nhóm** thay vì học rời rạc.

## QUY TẮC TÁCH NODE (SPLITTING)
Nếu một `pattern` đầu vào quá phức tạp, gộp nhiều ý ngữ pháp khác nhau, hoặc có nhiều biến thể quan trọng (khẳng định/phủ định/nghi vấn, hoặc nhiều cách dùng khác hẳn nhau), hãy **tách thành nhiều node con** để dễ học hơn, thay vì nhồi hết vào 1 node.
Quy tắc đặt id cho node tách: Node gốc giữ nguyên `id` ban đầu (số nguyên). Node tách thêm dùng id dạng thập phân nối tiếp: `1.1`, `1.2`, ... Luôn tạo edge nối node gốc với các node tách.

## ĐỊNH DẠNG ĐẦU RA (BẮT BUỘC)
Chỉ trả về **duy nhất một khối JSON hợp lệ**, không kèm giải thích. Schema:
{
  "nodes": [
    {
      "id": "1",
      "pattern": "S + have/has + been + V-ing + for/since + ...",
      "meaning": "...",
      "explanation": "...",
      "example": "..."
    }
  ],
  "edges": [
    {
      "from": "1",
      "to": "2",
      "relation": "contrast",
      "note": "Phân biệt với thì quá khứ đơn vì nhấn mạnh tính tiếp diễn đến hiện tại"
    }
  ]
}"""

        val inputList = nodes.mapIndexed { index, node -> 
            mapOf("id" to (index + 1).toString(), "pattern" to node.pattern)
        }
        val userPrompt = json.encodeToString(mapOf("nodes" to inputList))

        val rawResponse = geminiClient.generateContent(
            apiKey = apiKey,
            systemPrompt = systemPrompt,
            userPrompt = userPrompt,
            model = GEMINI_MODEL_CHAIN.first()
        )

        val cleanedJson = rawResponse.trim()
            .removePrefix("```json").removePrefix("```")
            .removeSuffix("```")
            .trim()
            
        json.decodeFromString<AiGraphResponse>(cleanedJson)
    }

    @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
    override suspend fun saveAiGeneratedGraph(response: AiGraphResponse, originalNodes: List<GrammarNode>, groupName: String) = withContext(Dispatchers.IO) {
        val idMap = mutableMapOf<String, String>()
        
        queries.transaction {
            response.nodes.forEach { aiNode ->
                val isSplitNode = aiNode.id.contains(".")
                
                if (!isSplitNode) {
                    val originalIndex = aiNode.id.toIntOrNull()?.minus(1) ?: return@forEach
                    val originalNode = originalNodes.getOrNull(originalIndex) ?: return@forEach
                    
                    idMap[aiNode.id] = originalNode.id
                    
                    queries.insertGrammarStructure(
                        id = originalNode.id,
                        pattern = aiNode.pattern,
                        meaning_key = aiNode.meaning,
                        explanation_key = aiNode.explanation,
                        example = aiNode.example,
                        level = originalNode.level,
                        created_at = Clock.System.now().toEpochMilliseconds()
                    )
                } else {
                    val newUuid = kotlin.uuid.Uuid.random().toString()
                    idMap[aiNode.id] = newUuid
                    
                    val parentId = aiNode.id.split(".")[0]
                    val parentIndex = parentId.toIntOrNull()?.minus(1)
                    val parentLevel = parentIndex?.let { originalNodes.getOrNull(it)?.level } ?: "B1"

                    queries.insertGrammarStructure(
                        id = newUuid,
                        pattern = aiNode.pattern,
                        meaning_key = aiNode.meaning,
                        explanation_key = aiNode.explanation,
                        example = aiNode.example,
                        level = parentLevel,
                        created_at = Clock.System.now().toEpochMilliseconds()
                    )
                    
                    queries.insertGroupNodeMapByName(groupName = groupName, grammarId = newUuid)
                }
            }
            
            response.edges.forEach { edge ->
                val fromUuid = idMap[edge.from] ?: return@forEach
                val toUuid = idMap[edge.to] ?: return@forEach
                
                queries.insertGrammarEdge(
                    id = kotlin.uuid.Uuid.random().toString(),
                    from_grammar_id = fromUuid,
                    to_grammar_id = toUuid,
                    edge_type = edge.relation,
                    created_at = Clock.System.now().toEpochMilliseconds()
                )
            }
        }
    }

    companion object {
    }
}

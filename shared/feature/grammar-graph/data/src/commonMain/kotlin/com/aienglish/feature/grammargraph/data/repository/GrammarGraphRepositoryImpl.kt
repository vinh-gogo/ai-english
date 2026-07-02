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
            val matchingRow = savedNoteRows.firstOrNull { row -> 
                row.note.contains(node.pattern, ignoreCase = true) || 
                row.note.contains(node.id, ignoreCase = true)
            }
            node.copy(
                isSaved = matchingRow != null,
                historyId = matchingRow?.history_id?.toString()
            )
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
                isSaved = true,
                historyId = row.history_id.toString()
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

## 2. DỮ LIỆU ĐẦU VÀO

Bạn sẽ nhận một JSON gồm danh sách `nodes`, mỗi node có:
- `id`: số nguyên, định danh duy nhất.
- `pattern`: một câu ví dụ hoặc cụm ngữ pháp thô, CHƯA có công thức chuẩn hóa, nghĩa, giải thích.

Ví dụ input:
```json
{
  "nodes": [
    { "id": 1, "pattern": "I have been learning English for 3 years." },
    { "id": 2, "pattern": "I learnt English yesterday." },
    { "id": 3, "pattern": "I had learnt English before I moved to US." }
  ]
}
```

## 3. NHIỆM VỤ

Với mỗi node đầu vào, bạn phải:

1. **Chuẩn hóa `pattern`** thành công thức ngữ pháp tổng quát (VD: `"S + have/has + been + V-ing + for/since + ..."`), không chỉ giữ nguyên câu ví dụ.
2. **Điền đầy đủ 4 trường**: `pattern`, `meaning`, `explanation`, `example`.
3. **Tách node nếu cần** (xem mục 4).
4. **Tạo `edges`** thể hiện quan hệ giữa các node (xem mục 5).

## 4. QUY TẮC TÁCH NODE (SPLITTING)

Nếu một `pattern` đầu vào quá phức tạp, gộp nhiều ý ngữ pháp khác nhau, hoặc có nhiều biến thể quan trọng (khẳng định/phủ định/nghi vấn, hoặc nhiều cách dùng khác hẳn nhau), hãy **tách thành nhiều node con** để dễ học hơn, thay vì nhồi hết vào 1 node.

Quy tắc đặt id cho node tách:
- Node gốc giữ nguyên `id` ban đầu, dùng cho ý chính/phổ biến nhất.
- Node tách thêm dùng id dạng thập phân nối tiếp: `1.1`, `1.2`, ... (id có thể là string nếu cần) để dễ truy vết nguồn gốc.
- Luôn tạo edge nối node gốc với các node tách (quan hệ `"variant_of"` hoặc `"part_of"`, xem mục 5).

**Không tách quá nhỏ** — chỉ tách khi thực sự giúp người học dễ hiểu hơn (nguyên tắc: mỗi node chỉ chứa **một ý ngữ pháp duy nhất, học được trong 1-2 phút**).

## 5. QUY TẮC TẠO EDGES (LIÊN KẾT)

Tạo edge `{ "from": <id>, "to": <id>, "relation": <string>, "note": <string, optional> }` khi hai node có liên quan sư phạm rõ ràng. Dùng các loại `relation` sau (bổ sung nếu cần, nhưng ưu tiên tái dùng):

| relation        | Ý nghĩa                                                             |
|-----------------|----------------------------------------------------------------------|
| `prerequisite`  | Node `from` nên học trước node `to` (nền tảng → nâng cao)            |
| `contrast`      | Hai cấu trúc dễ nhầm lẫn, cần phân biệt (VD: Simple Past vs Present Perfect) |
| `extension`     | Node `to` là biến thể mở rộng/phức tạp hơn của node `from`           |
| `variant_of`    | Node `to` là một biến thể được tách ra từ node `from` (dùng khi tách node ở mục 4) |
| `related_topic` | Cùng chủ đề ngữ pháp lớn (VD: cùng thuộc nhóm "thì hoàn thành")      |
| `combination`   | Hai cấu trúc thường được dùng kết hợp trong cùng một câu             |

Nguyên tắc:
- Chỉ tạo edge khi quan hệ **thực sự giúp ích cho việc học** (tránh graph rối, quá nhiều liên kết vô nghĩa).
- Ưu tiên các cặp cùng nhóm thì (tenses), cùng cấu trúc câu điều kiện, cùng nhóm mệnh đề quan hệ, v.v.
- Với các cấu trúc là "thì" (tenses), luôn cân nhắc `contrast` với thì hay bị nhầm và `prerequisite` với thì đơn giản hơn.
- Đồ thị có thể có nhiều hơn 1 edge giữa các cụm node khác nhau (không bắt buộc là cây/tuyến tính).

## 6. QUY TẮC ĐIỀN NỘI DUNG TỪNG TRƯỜNG

### `pattern`
- Công thức ngữ pháp tổng quát, dùng ký hiệu chuẩn: `S` (chủ ngữ), `V` (động từ), `O` (tân ngữ), `V-ing`, `V-ed/V3`, `have/has`, `will`, v.v.
- Ngắn gọn, dễ nhớ, đúng chuẩn ngữ pháp học thuật.

### `meaning`
- 1 câu tiếng Việt ngắn gọn nêu **cấu trúc này dùng để diễn đạt điều gì** (không phải dịch nghĩa câu ví dụ).
- VD: "Diễn tả hành động bắt đầu trong quá khứ và vẫn tiếp diễn đến hiện tại."

### `explanation`
- Giải thích chi tiết hơn: **khi nào dùng**, dấu hiệu nhận biết (từ khóa như `since`, `for`, `by the time`...), lưu ý dễ sai, phân biệt nhanh với cấu trúc gần giống (nếu có).
- Viết dễ hiểu cho người học trình độ tương ứng (A1-C2 tùy độ khó pattern), 2-4 câu.

### `example`
- Câu ví dụ hoàn chỉnh, tự nhiên, khác với `pattern` đầu vào nếu đầu vào đã là câu ví dụ (tránh trùng lặp), có thể giữ câu gốc làm ví dụ chính nếu phù hợp và thêm 1 ví dụ phụ nếu cần.
- Có thể là mảng nếu muốn nhiều ví dụ (khẳng định/phủ định/nghi vấn), nhưng mặc định là 1 câu nếu không yêu cầu khác.

## 7. YÊU CẦU SƯ PHẠM TỔNG THỂ

- Ưu tiên **nhóm các node liên quan gần nhau** về mặt logic quan hệ (qua edges), để khi hiển thị, người học thấy rõ cụm chủ đề (VD: cụm "các thì hoàn thành", cụm "câu điều kiện", cụm "mệnh đề quan hệ").
- Không tạo node/edge trùng lặp thông tin.
- Ưu tiên độ khó tăng dần nếu có thể suy ra từ dữ liệu (dùng `prerequisite`).
- Ngôn ngữ diễn giải: **tiếng Việt**, thuật ngữ ngữ pháp có thể kèm tiếng Anh trong ngoặc.

## 8. ĐỊNH DẠNG ĐẦU RA (BẮT BUỘC)

Chỉ trả về **duy nhất một khối JSON hợp lệ**, không kèm giải thích, không markdown code fence, không văn bản thừa. Schema:

```json
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
}
```

Lưu ý:
- `id` giữ nguyên kiểu dữ liệu với input gốc trừ khi là node tách (dùng dạng `"1.1"`).
- Nếu một node gốc bị tách, node gốc trong output **không được biến mất** — vẫn phải xuất hiện với nội dung của ý chính.
- Không tự ý thêm node ngữ pháp không có trong input (trừ node tách hợp lệ theo mục 4).
- Đảm bảo mọi `from`/`to` trong `edges` đều trỏ đến `id` tồn tại trong `nodes`.
"""

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

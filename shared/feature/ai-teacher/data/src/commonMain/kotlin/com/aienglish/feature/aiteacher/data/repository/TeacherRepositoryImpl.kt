package com.aienglish.feature.aiteacher.data.repository

import arrow.core.Either
import com.aienglish.core.common.dispatchers.AppDispatchers
import com.aienglish.core.common.prefs.AppPreferences
import com.aienglish.core.common.result.AppError
import com.aienglish.core.common.result.AppResult
import com.aienglish.core.network.client.GeminiClient
import com.aienglish.core.network.client.GEMINI_MODEL_CHAIN
import com.aienglish.feature.aiteacher.domain.model.EnglishFeedback
import com.aienglish.feature.aiteacher.domain.model.EnglishLesson
import com.aienglish.feature.aiteacher.domain.repository.TeacherRepository
import com.aienglish.feature.aiteacher.data.model.EnglishLessonDto
import com.aienglish.feature.aiteacher.data.model.EnglishFeedbackDto
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class TeacherRepositoryImpl(
    private val geminiClient: GeminiClient,
    private val dispatchers: AppDispatchers,
    private val prefs: AppPreferences,
    private val fallbackApiKey: String   // from local.properties / BuildConfig
) : TeacherRepository {

    private val json = Json { ignoreUnknownKeys = true; coerceInputValues = true }

    // Always get the freshest API key — user may update it in Profile without restart
    private fun resolveApiKey(): String {
        val saved = prefs.getApiKey()
        return if (saved.isNotBlank()) saved else fallbackApiKey
    }

    private fun sanitizeJson(raw: String): String {
        return raw.trim()
            .removePrefix("```json").removePrefix("```")
            .removeSuffix("```")
            .trim()
    }

    override suspend fun generateLesson(
        userId: String,
        topic: String,
        level: String
    ): AppResult<EnglishLesson> = withContext(dispatchers.io) {
        try {
            val apiKey = resolveApiKey()
            if (apiKey.isBlank()) {
                return@withContext Either.Left(
                    AppError.Network("Chưa có API Key. Vào Profile → cài Gemini API Key để bắt đầu.")
                )
            }

            val isJapanese = level.startsWith("N")
            val systemPrompt = if (isJapanese) {
                """
                    Bạn là Sensei AI - giáo viên tiếng Nhật chuyên nghiệp và tận tâm.
                    Nhiệm vụ của bạn là hỗ trợ người dùng học tiếng Nhật qua phương pháp dịch câu ngắn hàng ngày.
                    Khi người dùng chọn một chủ đề, bạn phải tạo ra đúng 10 câu tiếng Việt ngắn liên quan tới chủ đề đó, kèm theo bản dịch tiếng Nhật và phân tích từ vựng cho từng câu.

                    BẠN BẮT BUỘC PHẢI TẠO CÁC CÂU CÓ ĐỘ KHÓ VÀ NGỮ PHÁP PHÙ HỢP CHÍNH XÁC VỚI TRÌNH ĐỘ JLPT: $level.
                    - Nếu trình độ là N5 hoặc N4: Sử dụng từ vựng sơ cấp đơn giản, ngữ pháp cơ bản (ví dụ: ~desu, ~masu, trợ từ cơ bản), câu ngắn gọn, dễ hiểu.
                    - Nếu trình độ là N3: Sử dụng ngữ pháp trung cấp (ví dụ: ~koto ga aru, ~nakereba naranai, thể bị động/sai khiến cơ bản), từ vựng giao tiếp tự nhiên và đa dạng hơn.
                    - Nếu trình độ là N2 hoặc N1: Sử dụng ngữ pháp thượng cấp/nâng cao, kính ngữ (keigo), khiêm nhường ngữ, từ vựng học thuật, viết báo, hoặc các lối diễn đạt văn chương trang trọng.

                    BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
                    {
                      "sentences": [
                        {
                          "vietnamese": "Câu tiếng Việt ngắn (ví dụ: 'Xin chào, bạn khỏe không?')",
                          "english": "Bản dịch tiếng Nhật tương ứng (ví dụ: 'こんにちは、お元気ですか？')",
                          "tokens": [
                            { "word": "こんにちは", "phonetic": "konnichiwa", "partOfSpeech": "こんにちは" },
                            { "word": "お元気", "phonetic": "ogenki", "partOfSpeech": "おげんき" },
                            { "word": "です", "phonetic": "desu", "partOfSpeech": "đes" },
                            { "word": "か", "phonetic": "ka", "partOfSpeech": "ka" }
                          ]
                        }
                      ]
                    }

                    LƯU Ý QUAN TRỌNG:
                    1. Đảm bảo toàn bộ câu trả lời chỉ chứa chuỗi JSON hợp lệ. Không viết thêm chữ giải thích bên ngoài JSON.
                    2. Các câu tiếng Việt phải ngắn gọn, tự nhiên, mang tính giao tiếp thực tế và phù hợp với chủ đề được chọn.
                    3. Trong mảng 'tokens', đối với tiếng Nhật:
                       - Trường 'word' chứa từ vựng tiếng Nhật viết bằng chữ Kanji/Kana.
                       - Trường 'phonetic' chứa phiên âm Romaji tương ứng.
                       - Trường 'partOfSpeech' chứa cách đọc viết bằng chữ Kana/Furigana của từ đó.
                """.trimIndent()
            } else {
                """
                    You are an English AI Teacher - a professional and caring English language instructor.
                    Your task is to help Vietnamese learners practice English through daily sentence translation exercises.
                    When the user selects a topic, you must create exactly 10 short Vietnamese sentences related to that topic,
                    along with their English translation and token breakdown.

                    YOU MUST CREATE SENTENCES THAT MATCH EXACTLY THE TARGET ENGLISH LEVEL: $level.
                    - If the level is CEFR (A1, A2, B1, B2, C1, C2):
                      * A1/A2: Use simple vocabulary, basic present/past tense, short sentences, everyday words.
                      * B1: Use intermediate grammar (conditionals, passive voice basics), natural expressions, varied vocabulary.
                      * B2: Use advanced grammar structures, idiomatic expressions, diverse vocabulary and phrasal verbs.
                      * C1/C2: Use academic vocabulary, complex structures, nuanced expressions, and native-like naturalness.
                    - If the level is TOEIC (300, 500, 700, >900):
                      * TOEIC 300: Use very basic workplace/everyday vocabulary, extremely simple sentences.
                      * TOEIC 500: Use basic business communications context, standard grammar, simple emails/announcements phrasing.
                      * TOEIC 700: Use intermediate business/professional vocabulary, compound sentences, common office/business idioms.
                      * TOEIC >900: Use high-level business negotiation/academic vocabulary, complex grammar, formal documents tone.

                    YOU MUST RETURN VALID JSON FOLLOWING EXACTLY THIS STRUCTURE:
                    {
                      "sentences": [
                        {
                          "vietnamese": "Short Vietnamese sentence",
                          "english": "Corresponding English translation",
                          "tokens": [
                            { "word": "Hello", "phonetic": "/həˈloʊ/", "partOfSpeech": "interjection" }
                          ]
                        }
                      ]
                    }

                    CRITICAL RULES:
                    1. Return ONLY valid JSON. No markdown, no explanation, no preamble outside the JSON.
                    2. Vietnamese sentences must be short, natural, and relevant to the chosen topic.
                    3. Tokens must break the English sentence into individual words with IPA phonetics and part of speech.
                """.trimIndent()
            }

            val userPrompt = if (isJapanese) {
                "Hãy tạo 10 câu ôn tập ngắn trình độ JLPT $level cho chủ đề: $topic"
            } else {
                "Create 10 English practice sentences for target level: $level and topic: $topic"
            }

            val rawResponse = geminiClient.generateContent(
                apiKey = apiKey,
                systemPrompt = systemPrompt,
                userPrompt = userPrompt,
                model = GEMINI_MODEL_CHAIN.first()
            )

            val cleanedJson = sanitizeJson(rawResponse)
            val lesson = json.decodeFromString<EnglishLessonDto>(cleanedJson).toDomain()
            Either.Right(lesson)
        } catch (e: Exception) {
            Either.Left(AppError.Network(friendlyError(e.message)))
        }
    }

    override suspend fun evaluateLesson(
        userId: String,
        topic: String,
        answers: List<Pair<String, String>>,
        sentences: List<String>,
        level: String
    ): AppResult<EnglishFeedback> = withContext(dispatchers.io) {
        try {
            val apiKey = resolveApiKey()
            if (apiKey.isBlank()) {
                return@withContext Either.Left(
                    AppError.Network("Chưa có API Key. Vào Profile → cài Gemini API Key.")
                )
            }

            val isJapanese = level.startsWith("N")
            val reports = answers.zip(sentences).mapIndexed { index, (answerAndTarget, vietnamese) ->
                val (userAnswer, targetLang) = answerAndTarget
                val langLabel = if (isJapanese) "tiếng Nhật" else "English"
                """
                Sentence ${index + 1}:
                - Vietnamese original: $vietnamese
                - Target $langLabel: $targetLang
                - Student's answer: ${userAnswer.ifBlank { "(Skipped / No answer)" }}
                """.trimIndent()
            }.joinToString("\n\n")

            val systemPrompt = if (isJapanese) {
                """
                    Bạn là Sensei AI - giáo viên tiếng Nhật.
                    Nhiệm vụ của bạn là đánh giá kết quả dịch câu của học sinh.
                    Học sinh vừa hoàn thành một bài luyện dịch 10 câu cho chủ đề: $topic.

                    BẠN PHẢI ĐÁNH GIÁ SÁT SAO THEO TIÊU CHUẨN TRÌNH ĐỘ JLPT: $level.
                    - Với trình độ N5 hoặc N4: Chấp nhận các cách viết đơn giản, tập trung vào cấu trúc câu cơ bản. Khuyến khích động viên học sinh nhiều hơn.
                    - Với trình độ N3: Đánh giá khắt khe hơn về trợ từ, thể động từ tự nhiên, sắc thái câu trung cấp.
                    - Với trình độ N2 hoặc N1: Yêu cầu cực kỳ khắt khe về từ vựng nâng cao, chữ Hán (Kanji), kính ngữ, cấu trúc câu tự nhiên như người bản xứ. Chỉ ra lỗi sai sắc thái dù là nhỏ nhất.

                    Dưới đây là kết quả làm bài của học sinh:
                    $reports

                    Hãy đánh giá tổng quan, khen ngợi những chỗ học sinh làm tốt, chỉ ra các lỗi sai phổ biến (về trợ từ, chia động từ, từ vựng) nếu có và đề xuất cách cải thiện ngắn gọn.
                    Cuối cùng, cho học sinh một số điểm trên thang điểm 10.

                    BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
                    {
                      "score": 8,
                      "review": "Lời nhận xét chi tiết bằng tiếng Việt có định dạng rõ ràng (sử dụng dấu xuống dòng \n để phân đoạn ý)."
                    }

                    LƯU Ý QUAN TRỌNG:
                    1. Đảm bảo toàn bộ câu trả lời chỉ chứa chuỗi JSON hợp lệ. Không viết thêm chữ giải thích bên ngoài JSON.
                    2. Viết bằng tiếng Việt tự nhiên, mang tính khuyến khích học tập.
                """.trimIndent()
            } else {
                """
                    You are an English AI Teacher evaluating student translation results.
                    The student just completed a 10-sentence translation exercise on topic: $topic.

                    YOU MUST EVALUATE STRICTLY ACCORDING TO THE TARGET LEVEL: $level.
                    - If the level is CEFR (A1, A2, B1, B2, C1, C2):
                      * A1/A2: Accept simple phrasing, focus on basic grammar correctness. Be encouraging and supportive.
                      * B1: Check for proper use of tenses, prepositions, and natural phrasing. Moderate strictness.
                      * B2: Require accurate idiomatic expressions, correct use of phrasal verbs, and varied vocabulary.
                      * C1/C2: Require near-native fluency, academic register, precise word choice, and natural complex sentences.
                    - If the level is TOEIC (300, 500, 700, >900):
                      * TOEIC 300: Check for basic understanding, accept very simple grammar, be highly encouraging.
                      * TOEIC 500: Check for business environment vocabulary, basic grammar correctness.
                      * TOEIC 700: Look for intermediate business vocabulary, appropriate business expressions.
                      * TOEIC >900: Require flawless grammar, advanced vocabulary, and professional business tone.

                    Student's results:
                    $reports

                    Provide feedback in Vietnamese: praise what was done well, point out common errors, suggest improvements.
                    Give a score out of 10.

                    RETURN ONLY VALID JSON (no markdown, no extra text):
                    {
                      "score": 8,
                      "review": "Detailed feedback in Vietnamese with clear paragraphs (use \\n for line breaks)."
                    }
                """.trimIndent()
            }

            val userPrompt = if (isJapanese) {
                "Đánh giá bài làm tiếng Nhật của tôi."
            } else {
                "Please evaluate my English translation results."
            }

            val rawResponse = geminiClient.generateContent(
                apiKey = apiKey,
                systemPrompt = systemPrompt,
                userPrompt = userPrompt,
                model = GEMINI_MODEL_CHAIN.first()
            )

            val cleanedJson = sanitizeJson(rawResponse)
            val feedback = json.decodeFromString<EnglishFeedbackDto>(cleanedJson).toDomain()
            Either.Right(feedback)
        } catch (e: Exception) {
            Either.Left(AppError.Network(friendlyError(e.message)))
        }
    }

    override suspend fun explainSentence(
        userId: String,
        vietnamese: String,
        targetEnglish: String,
        userAnswer: String,
        level: String
    ): AppResult<String> = withContext(dispatchers.io) {
        try {
            val apiKey = resolveApiKey()
            if (apiKey.isBlank()) {
                return@withContext Either.Left(
                    AppError.Network("Chưa có API Key. Vào Profile → cài Gemini API Key.")
                )
            }

            val isJapanese = level.startsWith("N")
            val systemPrompt = if (isJapanese) {
                """
                    Bạn là Sensei AI - giáo viên tiếng Nhật chuyên nghiệp.
                    Nhiệm vụ của bạn là giải thích chi tiết, dễ hiểu ngữ pháp và từ vựng của câu tiếng Nhật dựa trên câu dịch của học sinh, phù hợp với trình độ mục tiêu JLPT: $level.
                    
                    Thông tin câu hỏi:
                    - Câu gốc tiếng Việt: $vietnamese
                    - Bản dịch chuẩn tiếng Nhật: $targetEnglish
                    - Câu trả lời của học sinh: ${userAnswer.ifBlank { "(Học sinh chọn Bỏ qua)" }}
                    
                    Yêu cầu giải thích (Điều chỉnh theo trình độ JLPT $level):
                    1. Hãy đánh giá xem câu của học sinh viết (nếu có) có đúng ngữ pháp và tự nhiên hay không.
                    2. So sánh câu của học sinh với Bản dịch chuẩn, chỉ rõ điểm tốt hoặc các lỗi sai (về chia động từ, trợ từ, từ vựng, sắc thái) nếu học sinh viết sai.
                    3. Nếu học sinh viết đúng, hãy khen ngợi ngắn gọn và giải thích thêm các cấu trúc ngữ pháp sử dụng trong câu để họ hiểu sâu hơn. Giải thích ngữ pháp hãy tập trung vào các điểm ngữ pháp trình độ $level có trong câu.
                    4. Nếu học sinh Bỏ qua, hãy dịch mẫu và phân tích tường nhận ngữ pháp, từ vựng từng phần để học sinh tự học. Giải thích chi tiết các mẫu ngữ pháp tương ứng trình độ $level.
                    5. Viết bằng tiếng Việt tự nhiên, ấm áp, khuyến khích học tập. Trả về văn bản giải thích trực tiếp dưới dạng Markdown (sử dụng các tiêu đề #, ##, ###, in đậm, in nghiêng, list..., không dùng định dạng JSON).
                """.trimIndent()
            } else {
                """
                    You are an English AI Teacher - a professional and caring instructor.
                    Your task is to explain in detail the grammar, vocabulary, and usage of an English sentence,
                    tailored to the student's target level: $level.

                    Sentence information:
                    - Original Vietnamese: $vietnamese
                    - Target English: $targetEnglish
                    - Student's answer: ${userAnswer.ifBlank { "(Student skipped this sentence)" }}

                    Explanation requirements (adjusted for target level $level):
                    1. Evaluate whether the student's answer is grammatically correct and natural.
                    2. Compare with the target English, point out strengths or errors (grammar, word order, vocabulary, tone).
                    3. If the student answered correctly, praise briefly and explain the grammar structures at $level depth.
                    4. If the student skipped, provide a full breakdown of the target sentence, explaining vocabulary and grammar patterns relevant to $level.
                    5. Write in Vietnamese, warm and encouraging. Return as Markdown (use ##, bold, lists - no JSON).
                """.trimIndent()
            }

            val userPrompt = if (isJapanese) {
                "Hãy giải thích chi tiết câu dịch này cho tôi."
            } else {
                "Please explain this English sentence in detail."
            }

            val rawResponse = geminiClient.generateContent(
                apiKey = apiKey,
                systemPrompt = systemPrompt,
                userPrompt = userPrompt,
                model = GEMINI_MODEL_CHAIN.first()
            )
            Either.Right(rawResponse.trim())
        } catch (e: Exception) {
            Either.Left(AppError.Network(friendlyError(e.message)))
        }
    }

    private fun friendlyError(msg: String?): String {
        if (msg == null) return "Lỗi không xác định"
        return when {
            "API_KEY_INVALID" in msg || "400" in msg && "key" in msg.lowercase() ->
                "API Key không hợp lệ. Vào Profile để cập nhật key mới."
            "QUOTA_EXCEEDED" in msg || "429" in msg ->
                "Vượt quota Gemini miễn phí. Thử lại sau 1 phút hoặc dùng API Key khác."
            "PERMISSION_DENIED" in msg || "403" in msg ->
                "API Key bị từ chối. Kiểm tra lại key trong Profile."
            "503" in msg || "overloaded" in msg.lowercase() ->
                "Gemini đang quá tải. Đã thử tất cả models dự phòng. Thử lại sau ít phút."
            "Empty Gemini response" in msg ->
                "AI không trả về kết quả. Thử lại hoặc đổi chủ đề."
            else -> msg
        }
    }
}

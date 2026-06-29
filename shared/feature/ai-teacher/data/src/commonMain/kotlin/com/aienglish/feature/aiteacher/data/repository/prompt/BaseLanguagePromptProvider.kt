package com.aienglish.feature.aiteacher.data.repository.prompt

abstract class BaseLanguagePromptProvider : LanguagePromptProvider {
    abstract val aiName: String
    abstract val targetLanguageName: String
    abstract val certName: String
    
    abstract val exampleSource: String
    abstract val exampleTarget: String
    abstract val exampleTokens: String
    
    abstract val tokenWordDesc: String
    abstract val tokenPhoneticDesc: String
    abstract val tokenPartOfSpeechDesc: String
    
    abstract val punctuationExamples: String
    abstract val mergeExample1: String
    abstract val mergeExample2: String
    
    abstract val evalCommonErrorsEn: String
    abstract val evalCommonErrorsVi: String

    open fun getExplainRequirementsEn(level: String): String = ""
    open fun getExplainRequirementsVi(level: String): String = ""
    
    abstract fun getExplainErrorsEn(level: String): String
    abstract fun getExplainErrorsVi(level: String): String
    
    abstract fun getLevelGuidelines(level: String): String
    abstract fun getEvalLevelGuidelines(level: String): String

    override fun getGenerateLessonSystemPrompt(level: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeLabel = if (isEn) "tiếng Anh" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "You are $aiName - a professional and caring $targetLanguageName language instructor.\n" +
            "Your task is to help learners practice $targetLanguageName through daily sentence translation exercises.\n" +
            "When the user selects a topic, you must create exactly 10 short English sentences related to that topic, along with their $targetLanguageName translation and token breakdown."
        } else {
            "Bạn là $aiName - giáo viên $languageLabel chuyên nghiệp và tận tâm.\n" +
            "Nhiệm vụ của bạn là hỗ trợ người dùng học $languageLabel qua phương pháp dịch câu ngắn hàng ngày.\n" +
            "Khi người dùng chọn một chủ đề, bạn phải tạo ra đúng 10 câu tiếng Việt ngắn liên quan tới chủ đề đó, kèm theo bản dịch $languageLabel tương ứng và phân tích từ vựng cho từng câu."
        }

        return """
            $systemPromptTitle

            BẠN BẮT BUỘC PHẢI TẠO CÁC CÂU CÓ ĐỘ KHÓ VÀ NGỮ PHÁP PHÙ HỢP CHÍNH XÁC VỚI TRÌNH ĐỘ $certName: $level.
            ${getLevelGuidelines(level)}

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "sentences": [
                {
                  "vietnamese": "Câu $nativeLabel ngắn (ví dụ: '$exampleSource')",
                  "english": "Bản dịch $languageLabel tương ứng (ví dụ: '$exampleTarget')",
                  "alternatives": [
                    "Bản dịch đúng thay thế 1 (dùng từ đồng nghĩa, cấu trúc khác)",
                    "Bản dịch đúng thay thế 2"
                  ],
                  "tokens": [
                    $exampleTokens
                  ]
                }
              ]
            }

            LƯU Ý QUAN TRỌNG:
            1. Đảm bảo toàn bộ câu trả lời chỉ chứa chuỗi JSON hợp lệ. Không viết thêm chữ giải thích bên ngoài JSON.
            2. Các câu $nativeLabel phải ngắn gọn, tự nhiên, mang tính giao tiếp thực tế và phù hợp với chủ đề được chọn.
            3. 'alternatives' phải liệt kê 2-4 cách dịch chuẩn khác nhau (từ đồng nghĩa, cách diễn đạt thân mật/trang trọng) cho câu $languageLabel.
            4. Trong mảng 'tokens', hãy gom các từ đi liền nhau thành các cụm từ (chunk/collocation) có ý nghĩa (ví dụ: thay vì tách rời từng chữ, hãy gộp thành các cụm). Đối với mỗi cụm trong $languageLabel:
               - Trường 'word' chứa $tokenWordDesc
               - Trường 'phonetic' chứa $tokenPhoneticDesc
               - Trường 'partOfSpeech' chứa $tokenPartOfSpeechDesc
            5. KHÔNG tạo token riêng cho dấu câu đơn lẻ (ví dụ: $punctuationExamples). Nếu một cụm từ đi kèm dấu câu, hãy gộp dấu câu vào token của cụm từ đó (ví dụ: "$mergeExample1" thay vì "$mergeExample2"). Token dấu câu không có giá trị học tập và làm ngắt quãng luồng hiển thị.
        """.trimIndent()
    }

    override fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String {
        val nativeLabel = if (nativeLanguage == "en") "tiếng Anh" else "tiếng Việt"
        return "Hãy tạo 10 câu ôn tập ngắn trình độ $languageLabel $level bằng $nativeLabel cho chủ đề: $topic"
    }

    override fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "You are $aiName - a $targetLanguageName language teacher.\n" +
            "Your task is to evaluate the student's $targetLanguageName translation results.\n" +
            "The student just completed a 10-sentence translation exercise on topic: $topic."
        } else {
            "Bạn là $aiName - giáo viên $languageLabel.\n" +
            "Nhiệm vụ của bạn là đánh giá kết quả dịch câu của học sinh.\n" +
            "Học sinh vừa hoàn thành một bài luyện dịch 10 câu cho chủ đề: $topic."
        }

        val evalDetails = if (isEn) {
            "Provide feedback in English: praise what was done well, point out common errors ($evalCommonErrorsEn) if any, and suggest improvements.\n" +
            "Give a total score out of 10. Also evaluate and score each of the 10 sentences individually (from 0 to 10). If a sentence is skipped or left blank, score it 0. For others, rate strictly based on correctness."
        } else {
            "Hãy đánh giá tổng quan bằng tiếng Việt, khen ngợi những chỗ học sinh làm tốt, chỉ ra các lỗi sai phổ biến ($evalCommonErrorsVi) nếu có và đề xuất cách cải thiện ngắn gọn.\n" +
            "Cuối cùng, cho học sinh một số điểm tổng trên thang điểm 10. Hãy chấm điểm cho từng câu trong số 10 câu (từ 0 đến 10 điểm) dựa trên bài làm của học sinh. Nếu câu bị bỏ qua hoặc bỏ trống, bắt buộc chấm 0 điểm. Các câu khác chấm điểm khách quan theo mức độ chính xác."
        }

        val reviewPlaceholder = if (isEn) "Detailed feedback in English with clear paragraphs (use \n for line breaks)." else "Lời nhận xét chi tiết bằng tiếng Việt có định dạng rõ ràng (sử dụng dấu xuống dòng \n để phân đoạn ý)."

        return """
            $systemPromptTitle

            BẠN PHẢI ĐÁNH GIÁ SÁT SAO THEO TIÊU CHUẨN TRÌNH ĐỘ $certName: $level.
            ${getEvalLevelGuidelines(level)}

            Dưới đây là kết quả làm bài của học sinh:
            $reports

            $evalDetails

            HƯỚNG DẪN CHẤM ĐIỂM NGHIÊM NGẶT:
            - Tập trung vào SỰ TƯƠNG ĐƯƠNG NGỮ NGHĨA: Nếu câu trả lời của học sinh diễn đạt đúng ý nghĩa một cách tự nhiên (ngay cả khi dùng từ đồng nghĩa, cấu trúc khác, hoặc chủ động/bị động so với đáp án gốc), BẠN BẮT BUỘC phải chấm 10/10.
            - KHÔNG TRỪ ĐIỂM đối với các lỗi nhỏ không ảnh hưởng đến ý nghĩa như: viết hoa/thường, dấu câu (thiếu dấu chấm, phẩy), khoảng trắng dư thừa, hoặc viết tắt thông dụng. Các trường hợp này phải đạt 10/10.
            - Chỉ trừ điểm đối với các lỗi sai ngữ pháp thực sự, dùng sai từ, hoặc dịch sai hoàn toàn ý nghĩa.

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "score": 8,
              "review": "$reviewPlaceholder",
              "sentenceScores": [8, 9, 7, 0, 10, 8, 9, 10, 6, 8]
            }

            LƯU Ý QUAN TRỌNG:
            1. Đảm bảo toàn bộ câu trả lời chỉ chứa chuỗi JSON hợp lệ. Không viết thêm chữ giải thích bên ngoài JSON.
            2. Viết bằng $responseLang tự nhiên, mang tính khuyến khích học tập.
            3. Trường 'sentenceScores' phải là một mảng chứa đúng 10 số nguyên tương ứng với điểm số của Câu 1 đến Câu 10.
        """.trimIndent()
    }

    override fun getEvaluateLessonUserPrompt(nativeLanguage: String): String {
        return if (nativeLanguage == "en") "Evaluate my $targetLanguageName translation results." else "Đánh giá bài làm $languageLabel của tôi."
    }

    override fun getExplainSentenceSystemPrompt(level: String, vietnamese: String, targetText: String, userAnswer: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeName = if (isEn) "English" else "tiếng Việt"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val skippedText = if (isEn) "(Student skipped this sentence)" else "(Học sinh chọn Bỏ qua)"

        val systemPromptTitle = if (isEn) {
            "You are $aiName - a professional and caring $targetLanguageName language instructor.\n" +
            "Your task is to explain in detail the grammar, vocabulary, and usage of a $targetLanguageName sentence based on the student's answer, tailored to target level: $level."
        } else {
            "Bạn là $aiName - giáo viên $languageLabel chuyên nghiệp.\n" +
            "Nhiệm vụ của bạn là giải thích chi tiết, dễ hiểu ngữ pháp và từ vựng của câu $languageLabel dựa trên câu dịch của học sinh, phù hợp với trình độ mục tiêu: $level."
        }

        val explanationPlaceholder = if (isEn) "Detailed explanation text formatted in Markdown." else "Nội dung giải thích chi tiết câu dịch sử dụng định dạng Markdown."

        val reqEn = getExplainRequirementsEn(level)
        val reqVi = getExplainRequirementsVi(level)
        
        val requirements = if (isEn) {
            """
            1. Evaluate whether the student's answer is grammatically correct and natural${if (reqEn.isNotEmpty()) " ($reqEn)" else ""}.
            2. Compare with the target $targetLanguageName, point out strengths or errors (${getExplainErrorsEn(level)}) if incorrect.
            3. If correct, praise briefly and explain grammar structures relevant to $certName level $level.
            4. If skipped, provide translation and detailed grammar/vocabulary breakdown for self-study at $certName level $level.
            5. Write in English, warm and encouraging. Format explanation in Markdown.
            6. In 'score', rate the student's answer from 0 to 10. If skipped or blank, score MUST be 0.
            """.trimIndent()
        } else {
            """
            1. Hãy đánh giá xem câu của học sinh viết (nếu có) có đúng ngữ pháp và tự nhiên hay không${if (reqVi.isNotEmpty()) " ($reqVi)" else ""}.
            2. So sánh câu của học sinh với Bản dịch chuẩn, chỉ rõ điểm tốt hoặc các lỗi sai (${getExplainErrorsVi(level)}) nếu học sinh viết sai.
            3. Nếu học sinh viết đúng, hãy khen ngợi ngắn gọn và giải thích thêm các cấu trúc ngữ pháp sử dụng trong câu để họ hiểu sâu hơn. Giải thích ngữ pháp hãy tập trung vào các điểm ngữ pháp trình độ $level có trong câu.
            4. Nếu học sinh Bỏ qua, hãy dịch mẫu và phân tích tường nhận ngữ pháp, từ vựng từng phần để học sinh tự học. Giải thích chi tiết các mẫu ngữ pháp tương ứng trình độ $level.
            5. Viết bằng tiếng Việt tự nhiên, ấm áp, khuyến khích học tập. Văn bản giải thích sử dụng định dạng Markdown.
            6. Tại trường 'score', chấm điểm câu trả lời của học sinh từ 0 đến 10. Nếu câu trả lời trống hoặc bỏ qua, bắt buộc chấm 0 điểm. Các câu khác chấm điểm khách quan theo mức độ chính xác.
            """.trimIndent()
        }

        return """
            $systemPromptTitle
            
            Thông tin câu hỏi:
            - Câu gốc $nativeName: $vietnamese
            - Bản dịch chuẩn $languageLabel: $targetText
            - Câu trả lời của học sinh: ${userAnswer.ifBlank { skippedText }}
            
            Yêu cầu giải thích (Điều chỉnh theo trình độ $certName $level):
            $requirements

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "score": 8,
              "explanation": "$explanationPlaceholder"
            }
        """.trimIndent()
    }

    override fun getExplainSentenceUserPrompt(nativeLanguage: String): String {
        return if (nativeLanguage == "en") "Explain this $targetLanguageName sentence in detail." else "Hãy giải thích chi tiết câu dịch này cho tôi."
    }
}

package com.aienglish.feature.aiteacher.data.repository.prompt

abstract class BaseLanguagePromptProvider : LanguagePromptProvider {
    abstract val aiName: String
    abstract val targetLanguageName: String
    abstract val targetLanguageCode: String
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
            "Provide feedback in English: praise what was done well, point out common errors ($evalCommonErrorsEn) if any, and suggest improvements."
        } else {
            "Hãy đánh giá tổng quan bằng tiếng Việt, khen ngợi những chỗ học sinh làm tốt, chỉ ra các lỗi sai phổ biến ($evalCommonErrorsVi) nếu có và đề xuất cách cải thiện ngắn gọn."
        }

        val reviewPlaceholder = if (isEn) "Detailed feedback in English with clear paragraphs (use \n for line breaks)." else "Lời nhận xét chi tiết bằng tiếng Việt có định dạng rõ ràng (sử dụng dấu xuống dòng \n để phân đoạn ý)."

        return """
            $systemPromptTitle

            BẠN PHẢI ĐÁNH GIÁ SÁT SAO THEO TIÊU CHUẨN TRÌNH ĐỘ $certName: $level.
            ${getEvalLevelGuidelines(level)}

            Dưới đây là kết quả làm bài của học sinh:
            $reports

            $evalDetails

            HƯỚNG DẪN ĐÁNH GIÁ NGHIÊM NGẶT:
            - Tập trung vào SỰ TƯƠNG ĐƯƠNG NGỮ NGHĨA: Nếu câu trả lời của học sinh diễn đạt đúng ý nghĩa một cách tự nhiên (ngay cả khi dùng từ đồng nghĩa, cấu trúc khác, hoặc chủ động/bị động so với đáp án gốc), hãy khen ngợi.
            - KHÔNG khắt khe với các lỗi nhỏ không ảnh hưởng đến ý nghĩa như: viết hoa/thường, dấu câu (thiếu dấu chấm, phẩy), khoảng trắng dư thừa, hoặc viết tắt thông dụng.
            - Chỉ phê bình đối với các lỗi sai ngữ pháp thực sự, dùng sai từ, hoặc dịch sai hoàn toàn ý nghĩa.

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "review": "$reviewPlaceholder"
            }

            LƯU Ý QUAN TRỌNG:
            1. Đảm bảo toàn bộ câu trả lời chỉ chứa chuỗi JSON hợp lệ. Không viết thêm chữ giải thích bên ngoài JSON.
            2. Viết bằng $responseLang tự nhiên, mang tính khuyến khích học tập.
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
            5. Write in English, warm and encouraging.
            6. At the very end of the explanation, list and explain other acceptable alternative translations or alternative structures (if any), explaining why they are acceptable or why other common options are incorrect in this context.
            """.trimIndent()
        } else {
            """
            1. Hãy đánh giá xem câu của học sinh viết (nếu có) có đúng ngữ pháp và tự nhiên hay không${if (reqVi.isNotEmpty()) " ($reqVi)" else ""}.
            2. So sánh câu của học sinh với Bản dịch chuẩn, chỉ rõ điểm tốt hoặc các lỗi sai (${getExplainErrorsVi(level)}) nếu học sinh viết sai.
            3. Nếu học sinh viết đúng, hãy khen ngợi ngắn gọn và giải thích thêm các cấu trúc ngữ pháp sử dụng trong câu để họ hiểu sâu hơn. Giải thích ngữ pháp hãy tập trung vào các điểm ngữ pháp trình độ $level có trong câu.
            4. Nếu học sinh Bỏ qua, hãy dịch mẫu và phân tích tường nhận ngữ pháp, từ vựng từng phần để học sinh tự học. Giải thích chi tiết các mẫu ngữ pháp tương ứng trình độ $level.
            5. Viết bằng tiếng Việt tự nhiên, ấm áp, khuyến khích học tập.
            6. Ở phần cuối cùng của lời giải thích, hãy liệt kê và giải thích ngắn gọn các phương án dịch/cách diễn đạt thay thế khác (nếu có) có thể chấp nhận được, hoặc giải thích vì sao một số cách dịch phổ biến khác lại chưa tự nhiên/chưa chính xác trong ngữ cảnh này để học sinh tham khảo thêm.
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
              "explanation": [
                { "text": "Đoạn văn bản giải thích ngữ pháp...", "lang": "vi" },
                { "text": "vocabulary_or_example_phrase", "lang": "$targetLanguageCode" }
              ]
            }

            LƯU Ý QUAN TRỌNG VỀ ĐỊNH DẠNG VÀ ĐỘ CHI TIẾT:
            1. Trường 'explanation' BẮT BUỘC phải là một mảng JSON của các đối tượng { "text", "lang" }.
            2. KHÔNG ĐƯỢC phép viết ngắn gọn, rút ngắn hoặc bỏ bớt nội dung giải thích vì lý do định dạng JSON. Lời giải thích phải CỰC KỲ CHI TIẾT, ĐẦY ĐỦ VÀ TOÀN DIỆN (bao gồm giải thích rõ ràng cấu trúc chung, phân tích cặn kẽ ngữ pháp, từ loại, nghĩa, và tất cả từ vựng cốt lõi của câu). Mảng JSON có thể chứa nhiều đối tượng liên tiếp (ví dụ từ 20 đến 40 đối tượng) để truyền tải trọn vẹn lời giải thích đầy đủ này.
            3. KHÔNG ĐƯỢC sử dụng các ký tự định dạng markdown như **, *, _, ###, ` trong bất kỳ trường 'text' nào. Hãy viết chữ trơn hoàn toàn (plain text). Có thể dùng kí tự xuống dòng '\n' để ngắt dòng và dấu gạch đầu dòng '-' để tạo cấu trúc danh sách rõ ràng.
            4. Hãy chia nhỏ nội dung giải thích thành các phân đoạn rất ngắn liên tiếp. Chỉ định 'lang' là "vi" cho tiếng Việt, và '$targetLanguageCode' cho tất cả các từ, cụm từ hoặc câu ví dụ bằng ngôn ngữ mục tiêu.
            5. BẮT BUỘC tách biệt cực kỳ triệt để từng chữ hoặc cụm từ ngôn ngữ mục tiêu ra khỏi câu tiếng Việt giải thích xung quanh thành các đối tượng riêng biệt với đúng thuộc tính 'lang', kể cả khi chúng nằm trong cùng một câu ngắn. Không viết lẫn lộn tiếng Việt và ngôn ngữ mục tiêu trong cùng một đối tượng.
            6. Gộp tất cả trường 'text' của các đối tượng trong mảng theo đúng thứ tự sẽ tạo ra văn bản giải thích hoàn chỉnh, rõ ràng, dễ đọc cho học sinh.
        """.trimIndent()
    }

    override fun getExplainSentenceUserPrompt(nativeLanguage: String): String {
        return if (nativeLanguage == "en") "Explain this $targetLanguageName sentence in detail." else "Hãy giải thích chi tiết câu dịch này cho tôi."
    }
}

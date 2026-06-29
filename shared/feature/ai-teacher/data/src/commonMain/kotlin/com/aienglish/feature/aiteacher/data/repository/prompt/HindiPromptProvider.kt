package com.aienglish.feature.aiteacher.data.repository.prompt

object HindiPromptProvider : LanguagePromptProvider {
    override val languageLabel: String = "tiếng Hindi"

    override fun getGenerateLessonSystemPrompt(level: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeName = if (isEn) "English" else "Vietnamese"
        val nativeLabel = if (isEn) "tiếng Anh" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "You are Guru AI - a professional and caring Hindi language instructor.\n" +
            "Your task is to help learners practice Hindi through daily sentence translation exercises.\n" +
            "When the user selects a topic, you must create exactly 10 short English sentences related to that topic, along with their Hindi (Devanagari script) translation and token breakdown."
        } else {
            "Bạn là Guru AI - giáo viên tiếng Hindi chuyên nghiệp và tận tâm.\n" +
            "Nhiệm vụ của bạn là hỗ trợ người dùng học tiếng Hindi qua phương pháp dịch câu ngắn hàng ngày.\n" +
            "Khi người dùng chọn một chủ đề, bạn phải tạo ra đúng 10 câu tiếng Việt ngắn liên quan tới chủ đề đó, kèm theo bản dịch tiếng Hindi (chữ Devanagari) tương ứng và phân tích từ vựng cho từng câu."
        }

        return """
            $systemPromptTitle

            BẠN BẮT BUỘC PHẢI TẠO CÁC CÂU CÓ ĐỘ KHÓ VÀ NGỮ PHÁP PHÙ HỢP CHÍNH XÁC VỚI TRÌNH ĐỘ HINDI: $level.
            - Nếu trình độ là A1 hoặc A2: Sử dụng từ vựng sinh hoạt hàng ngày, thì hiện tại đơn giản, các câu giới thiệu bản thân đơn giản.
            - Nếu trình độ là B1 hoặc B2: Sử dụng cấu trúc câu phức, thì quá khứ, tương lai, cách dùng trợ động từ và liên từ phong phú hơn.

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "sentences": [
                {
                  "vietnamese": "Câu $nativeLabel ngắn (ví dụ: 'Xin chào, bạn khỏe không?')",
                  "english": "Bản dịch tiếng Hindi tương ứng bằng chữ Devanagari (ví dụ: 'नमस्ते, आप कैसे हैं?')",
                  "tokens": [
                    { "word": "नमस्ते", "phonetic": "namaste", "partOfSpeech": "xin chào" },
                    { "word": "आप", "phonetic": "aap", "partOfSpeech": "bạn (lịch sự)" },
                    { "word": "कैसे", "phonetic": "kaise", "partOfSpeech": "thế nào" },
                    { "word": "हैं", "phonetic": "hain", "partOfSpeech": "thì/là" }
                  ]
                }
              ]
            }

            LƯU Ý QUAN TRỌNG:
            1. Đảm bảo toàn bộ câu trả lời chỉ chứa chuỗi JSON hợp lệ. Không viết thêm chữ giải thích bên ngoài JSON.
            2. Các câu $nativeLabel phải ngắn gọn, tự nhiên, mang tính giao tiếp thực tế và phù hợp với chủ đề được chọn.
            3. Trong mảng 'tokens', đối với tiếng Hindi:
               - Trường 'word' chứa chữ Hindi (Devanagari script).
               - Trường 'phonetic' chứa phiên âm Latinh (transliteration) tương ứng.
               - Trường 'partOfSpeech' chứa ý nghĩa hoặc nghĩa $nativeLabel tương ứng của từ đó.
        """.trimIndent()
    }

    override fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String {
        val nativeLabel = if (nativeLanguage == "en") "tiếng Anh" else "tiếng Việt"
        return "Hãy tạo 10 câu ôn tập ngắn trình độ tiếng Hindi $level bằng $nativeLabel cho chủ đề: $topic"
    }

    override fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "You are Guru AI - a Hindi language teacher.\n" +
            "Your task is to evaluate the student's Hindi translation results.\n" +
            "The student just completed a 10-sentence translation exercise on topic: $topic."
        } else {
            "Bạn là Guru AI - giáo viên tiếng Hindi.\n" +
            "Nhiệm vụ của bạn là đánh giá kết quả dịch câu của học sinh.\n" +
            "Học sinh vừa hoàn thành một bài luyện dịch 10 câu cho chủ đề: $topic."
        }

        val evalDetails = if (isEn) {
            "Provide feedback in English: praise what was done well, point out common errors (postpositions, pronouns, gender/number agreement, verb conjugations) if any, and suggest improvements.\n" +
            "Give a total score out of 10. Also evaluate and score each of the 10 sentences individually (from 0 to 10). If a sentence is skipped or left blank, score it 0. For others, rate strictly based on correctness."
        } else {
            "Hãy đánh giá tổng quan bằng tiếng Việt, khen ngợi những chỗ học sinh làm tốt, chỉ ra các lỗi sai phổ biến (về cấu trúc câu, hậu giới từ, chia động từ) nếu có và đề xuất cách cải thiện ngắn gọn.\n" +
            "Cuối cùng, cho học sinh một số điểm tổng trên thang điểm 10. Hãy chấm điểm cho từng câu trong số 10 câu (từ 0 đến 10 điểm) dựa trên bài làm của học sinh. Nếu câu bị bỏ qua hoặc bỏ trống, bắt buộc chấm 0 điểm. Các câu khác chấm điểm khách quan theo mức độ chính xác."
        }

        val reviewPlaceholder = if (isEn) "Detailed feedback in English with clear paragraphs (use \n for line breaks)." else "Lời nhận xét chi tiết bằng tiếng Việt có định dạng rõ ràng (sử dụng dấu xuống dòng \n để phân đoạn ý)."

        return """
            $systemPromptTitle

            BẠN PHẢI ĐÁNH GIÁ SÁT SAO THEO TIÊU CHUẨN TRÌNH ĐỘ HINDI: $level.
            - Với trình độ A1 hoặc A2: Chấp nhận các câu dịch ngắn gọn, cấu trúc cơ bản, động từ ở thì hiện tại.
            - Với trình độ B1 hoặc B2: Kiểm tra cách dùng hậu giới từ (postpositions), đại từ, chia động từ theo giống/số và sự tự nhiên của câu.

            Dưới đây là kết quả làm bài của học sinh:
            $reports

            $evalDetails

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
        return if (nativeLanguage == "en") "Evaluate my Hindi translation results." else "Đánh giá bài làm tiếng Hindi của tôi."
    }

    override fun getExplainSentenceSystemPrompt(level: String, vietnamese: String, targetText: String, userAnswer: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeName = if (isEn) "English" else "tiếng Việt"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val skippedText = if (isEn) "(Student skipped this sentence)" else "(Học sinh chọn Bỏ qua)"

        val systemPromptTitle = if (isEn) {
            "You are Guru AI - a professional and caring Hindi language instructor.\n" +
            "Your task is to explain in detail the grammar, vocabulary, and usage of a Hindi sentence based on the student's answer, tailored to target level: $level."
        } else {
            "Bạn là Guru AI - giáo viên tiếng Hindi chuyên nghiệp.\n" +
            "Nhiệm vụ của bạn là giải thích chi tiết, dễ hiểu ngữ pháp và từ vựng của câu tiếng Hindi dựa trên câu dịch của học sinh, phù hợp với trình độ mục tiêu: $level."
        }

        val requirements = if (isEn) {
            "1. Evaluate whether the student's answer is grammatically correct and natural.\n" +
            "2. Compare with the target Hindi, point out strengths or errors (postpositions, gender/number, verb conjugations) if incorrect.\n" +
            "3. If correct, praise briefly and explain grammar structures relevant to level $level.\n" +
            "4. If skipped, provide translation and detailed grammar/vocabulary breakdown for self-study at level $level.\n" +
            "5. Write in English, warm and encouraging. Format explanation in Markdown.\n" +
            "6. In 'score', rate the student's answer from 0 to 10. If skipped or blank, score MUST be 0."
        } else {
            "1. Hãy đánh giá xem câu của học sinh viết (nếu có) có đúng ngữ pháp và tự nhiên hay không.\n" +
            "2. So sánh câu của học sinh với Bản dịch chuẩn, chỉ rõ điểm tốt hoặc các lỗi sai (về trật tự từ, cách dùng hậu giới từ, chia động từ) nếu học sinh viết sai.\n" +
            "3. Nếu học sinh viết đúng, hãy khen ngợi ngắn gọn và giải thích thêm các cấu trúc ngữ pháp sử dụng trong câu để họ hiểu sâu hơn.\n" +
            "4. Nếu học sinh Bỏ qua, hãy dịch mẫu và phân tích tường nhận ngữ pháp, từ vựng từng phần để học sinh tự học.\n" +
            "5. Viết bằng tiếng Việt tự nhiên, ấm áp, khuyến khích học tập. Văn bản giải thích sử dụng định dạng Markdown.\n" +
            "6. Tại trường 'score', chấm điểm câu trả lời của học sinh từ 0 đến 10. Nếu câu trả lời trống hoặc bỏ qua, bắt buộc chấm 0 điểm. Các câu khác chấm điểm khách quan theo mức độ chính xác."
        }

        val explanationPlaceholder = if (isEn) "Detailed explanation text formatted in Markdown." else "Nội dung giải thích chi tiết câu dịch sử dụng định dạng Markdown."

        return """
            $systemPromptTitle
            
            Thông tin câu hỏi:
            - Câu gốc $nativeName: $vietnamese
            - Bản dịch chuẩn tiếng Hindi: $targetText
            - Câu trả lời của học sinh: ${userAnswer.ifBlank { skippedText }}
            
            Yêu cầu giải thích (Điều chỉnh theo trình độ $level):
            $requirements

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "score": 8,
              "explanation": "$explanationPlaceholder"
            }
        """.trimIndent()
    }

    override fun getExplainSentenceUserPrompt(nativeLanguage: String): String {
        return if (nativeLanguage == "en") "Explain this Hindi sentence in detail." else "Hãy giải thích chi tiết câu dịch này cho tôi."
    }
}

package com.aienglish.feature.aiteacher.data.repository.prompt

object KoreanPromptProvider : LanguagePromptProvider {
    override val languageLabel: String = "tiếng Hàn"

    override fun getGenerateLessonSystemPrompt(level: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeName = if (isEn) "English" else "Vietnamese"
        val nativeLabel = if (isEn) "tiếng Anh" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "You are Seonsaengnim AI - a professional and caring Korean language instructor.\n" +
            "Your task is to help learners practice Korean through daily sentence translation exercises.\n" +
            "When the user selects a topic, you must create exactly 10 short English sentences related to that topic, along with their Korean translation and token breakdown."
        } else {
            "Bạn là Seonsaengnim AI - giáo viên tiếng Hàn chuyên nghiệp và tận tâm.\n" +
            "Nhiệm vụ của bạn là hỗ trợ người dùng học tiếng Hàn qua phương pháp dịch câu ngắn hàng ngày.\n" +
            "Khi người dùng chọn một chủ đề, bạn phải tạo ra đúng 10 câu tiếng Việt ngắn liên quan tới chủ đề đó, kèm theo bản dịch tiếng Hàn tương ứng và phân tích từ vựng cho từng câu."
        }

        return """
            $systemPromptTitle

            BẠN BẮT BUỘC PHẢI TẠO CÁC CÂU CÓ ĐỘ KHÓ VÀ NGỮ PHÁP PHÙ HỢP CHÍNH XÁC VỚI TRÌNH ĐỘ TOPIK: $level.
            - Nếu trình độ là TOPIK 1 hoặc TOPIK 2 (Sơ cấp): Sử dụng từ vựng sinh hoạt cơ bản, cấu trúc ngữ pháp sơ cấp (ví dụ: -ㅂ니다/습니다, -아요/어요, các đuôi câu xã giao đơn giản), câu ngắn, dễ hiểu.
            - Nếu trình độ là TOPIK 3 hoặc TOPIK 4 (Trung cấp): Sử dụng từ vựng đa dạng hơn, các ngữ pháp liên kết trung cấp (ví dụ: -면서, -거나, -기 때문에, -ㄴ/un 후에), câu ghép và lối diễn đạt tự nhiên trong cuộc sống hay công việc.
            - Nếu trình độ là TOPIK 5 hoặc TOPIK 6 (Cao cấp): Sử dụng các ngữ pháp và từ vựng cao cấp, cấu trúc gián tiếp phức tạp, thành ngữ bốn chữ (사자성어), văn phong trang trọng (độ chính xác cao như viết báo, luận văn hoặc đàm phán thương mại).

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "sentences": [
                {
                  "vietnamese": "Câu $nativeLabel ngắn (ví dụ: 'Xin chào, bạn khỏe không?')",
                  "english": "Bản dịch tiếng Hàn tương ứng (ví dụ: '안녕하세요, 잘 지내세요?')",
                  "tokens": [
                    { "word": "안녕하세요", "phonetic": "annyeonghaseyo", "partOfSpeech": "xin chào" },
                    { "word": "잘", "phonetic": "jal", "partOfSpeech": "tốt/khỏe" },
                    { "word": "지내세요", "phonetic": "jinaeseyo", "partOfSpeech": "sống/trải qua" }
                  ]
                }
              ]
            }

            LƯU Ý QUAN TRỌNG:
            1. Đảm bảo toàn bộ câu trả lời chỉ chứa chuỗi JSON hợp lệ. Không viết thêm chữ giải thích bên ngoài JSON.
            2. Các câu $nativeLabel phải ngắn gọn, tự nhiên, mang tính giao tiếp thực tế và phù hợp với chủ đề được chọn.
            3. Trong mảng 'tokens', đối với tiếng Hàn:
               - Trường 'word' chứa từ vựng bằng chữ Hangul.
               - Trường 'phonetic' chứa phiên âm Romaji tương ứng.
               - Trường 'partOfSpeech' chứa ý nghĩa hoặc nghĩa $nativeLabel tương ứng của từ đó.
        """.trimIndent()
    }

    override fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String {
        val nativeLabel = if (nativeLanguage == "en") "tiếng Anh" else "tiếng Việt"
        return "Hãy tạo 10 câu ôn tập ngắn trình độ tiếng Hàn $level bằng $nativeLabel cho chủ đề: $topic"
    }

    override fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "You are Seonsaengnim AI - a Korean language teacher.\n" +
            "Your task is to evaluate the student's Korean translation results.\n" +
            "The student just completed a 10-sentence translation exercise on topic: $topic."
        } else {
            "Bạn là Seonsaengnim AI - giáo viên tiếng Hàn.\n" +
            "Nhiệm vụ của bạn là đánh giá kết quả dịch câu của học sinh.\n" +
            "Học sinh vừa hoàn thành một bài luyện dịch 10 câu cho chủ đề: $topic."
        }

        val evalDetails = if (isEn) {
            "Provide feedback in English: praise what was done well, point out common errors (particles, honorifics, sentence endings, conjugations) if any, and suggest improvements.\n" +
            "Give a total score out of 10. Also evaluate and score each of the 10 sentences individually (from 0 to 10). If a sentence is skipped or left blank, score it 0. For others, rate strictly based on correctness."
        } else {
            "Hãy đánh giá tổng quan bằng tiếng Việt, khen ngợi những chỗ học sinh làm tốt, chỉ ra các lỗi sai phổ biến (về trợ từ, kính ngữ, chia đuôi câu) nếu có và đề xuất cách cải thiện ngắn gọn.\n" +
            "Cuối cùng, cho học sinh một số điểm tổng trên thang điểm 10. Hãy chấm điểm cho từng câu trong số 10 câu (từ 0 đến 10 điểm) dựa trên bài làm của học sinh. Nếu câu bị bỏ qua hoặc bỏ trống, bắt buộc chấm 0 điểm. Các câu khác chấm điểm khách quan theo mức độ chính xác."
        }

        val reviewPlaceholder = if (isEn) "Detailed feedback in English with clear paragraphs (use \n for line breaks)." else "Lời nhận xét chi tiết bằng tiếng Việt có định dạng rõ ràng (sử dụng dấu xuống dòng \n để phân đoạn ý)."

        return """
            $systemPromptTitle

            BẠN PHẢI ĐÁNH GIÁ SÁT SAO THEO TIÊU CHUẨN TRÌNH ĐỘ TOPIK: $level.
            - Với trình độ TOPIK 1 hoặc TOPIK 2: Chấp nhận các cách viết sơ cấp, động từ chia đuôi câu cơ bản (-아요/어요 hoặc -ㅂ니다/습니다). Động viên học sinh nhiều hơn.
            - Với trình độ TOPIK 3 hoặc TOPIK 4: Đánh giá kĩ vị trí từ vựng, cách kết hợp trợ từ (이/가, 은/는, 을/를) và đuôi câu trung cấp tự nhiên.
            - Với trình độ TOPIK 5 hoặc TOPIK 6: Yêu cầu cực kỳ khắt khe về từ vựng cao cấp, cấu trúc ngữ pháp học thuật, kính ngữ đúng hoàn cảnh, văn phong trôi chảy tự nhiên như người bản xứ.

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
        return if (nativeLanguage == "en") "Evaluate my Korean translation results." else "Đánh giá bài làm tiếng Hàn của tôi."
    }

    override fun getExplainSentenceSystemPrompt(level: String, vietnamese: String, targetText: String, userAnswer: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeName = if (isEn) "English" else "tiếng Việt"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val skippedText = if (isEn) "(Student skipped this sentence)" else "(Học sinh chọn Bỏ qua)"

        val systemPromptTitle = if (isEn) {
            "You are Seonsaengnim AI - a professional and caring Korean language instructor.\n" +
            "Your task is to explain in detail the grammar, vocabulary, and usage of a Korean sentence based on the student's answer, tailored to TOPIK level: $level."
        } else {
            "Bạn là Seonsaengnim AI - giáo viên tiếng Hàn chuyên nghiệp.\n" +
            "Nhiệm vụ của bạn là giải thích chi tiết, dễ hiểu ngữ pháp và từ vựng của câu tiếng Hàn dựa trên câu dịch của học sinh, phù hợp với trình độ mục tiêu TOPIK: $level."
        }

        val requirements = if (isEn) {
            "1. Evaluate whether the student's answer is grammatically correct and natural (especially particles, verb conjugations, honorifics).\n" +
            "2. Compare with the target Korean, point out strengths or errors (word order, tenses, particles) if incorrect.\n" +
            "3. If correct, praise briefly and explain grammar structures relevant to TOPIK level $level.\n" +
            "4. If skipped, provide translation and detailed grammar/vocabulary breakdown for self-study at TOPIK level $level.\n" +
            "5. Write in English, warm and encouraging. Format explanation in Markdown.\n" +
            "6. In 'score', rate the student's answer from 0 to 10. If skipped or blank, score MUST be 0."
        } else {
            "1. Hãy đánh giá xem câu của học sinh viết (nếu có) có đúng ngữ pháp và tự nhiên hay không (đặc biệt lưu ý cách dùng trợ từ, chia đuôi động từ/tính từ, kính ngữ).\n" +
            "2. So sánh câu của học sinh với Bản dịch chuẩn, chỉ rõ điểm tốt hoặc các lỗi sai (về trật tự từ, cách dùng tiểu từ, chia thì động từ) nếu học sinh viết sai.\n" +
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
            - Bản dịch chuẩn tiếng Hàn: $targetText
            - Câu trả lời của học sinh: ${userAnswer.ifBlank { skippedText }}
            
            Yêu cầu giải thích (Điều chỉnh theo trình độ TOPIK $level):
            $requirements

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "score": 8,
              "explanation": "$explanationPlaceholder"
            }
        """.trimIndent()
    }

    override fun getExplainSentenceUserPrompt(nativeLanguage: String): String {
        return if (nativeLanguage == "en") "Explain this Korean sentence in detail." else "Hãy giải thích chi tiết câu dịch này cho tôi."
    }
}

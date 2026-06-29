package com.aienglish.feature.aiteacher.data.repository.prompt

object JapanesePromptProvider : LanguagePromptProvider {
    override val languageLabel: String = "tiếng Nhật"

    override fun getGenerateLessonSystemPrompt(level: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeName = if (isEn) "English" else "Vietnamese"
        val nativeLabel = if (isEn) "tiếng Anh" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "You are Sensei AI - a professional and caring Japanese language instructor.\n" +
            "Your task is to help learners practice Japanese through daily sentence translation exercises.\n" +
            "When the user selects a topic, you must create exactly 10 short English sentences related to that topic, along with their Japanese translation and token breakdown."
        } else {
            "Bạn là Sensei AI - giáo viên tiếng Nhật chuyên nghiệp và tận tâm.\n" +
            "Nhiệm vụ của bạn là hỗ trợ người dùng học tiếng Nhật qua phương pháp dịch câu ngắn hàng ngày.\n" +
            "Khi người dùng chọn một chủ đề, bạn phải tạo ra đúng 10 câu tiếng Việt ngắn liên quan tới chủ đề đó, kèm theo bản dịch tiếng Nhật và phân tích từ vựng cho từng câu."
        }

        return """
            $systemPromptTitle

            BẠN BẮT BUỘC PHẢI TẠO CÁC CÂU CÓ ĐỘ KHÓ VÀ NGỮ PHÁP PHÙ HỢP CHÍNH XÁC VỚI TRÌNH ĐỘ JLPT: $level.
            - Nếu trình độ là N5 hoặc N4: Sử dụng từ vựng sơ cấp đơn giản, ngữ pháp cơ bản (ví dụ: ~desu, ~masu, trợ từ cơ bản), câu ngắn gọn, dễ hiểu.
            - Nếu trình độ là N3: Sử dụng ngữ pháp trung cấp (ví dụ: ~koto ga aru, ~nakereba naranai, thể bị động/sai khiến cơ bản), từ vựng giao tiếp tự nhiên và đa dạng hơn.
            - Nếu trình độ là N2 hoặc N1: Sử dụng ngữ pháp thượng cấp/nâng cao, kính ngữ (keigo), khiêm nhường ngữ, từ vựng học thuật, viết báo, hoặc các lối diễn đạt văn chương trang trọng.

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "sentences": [
                {
                  "vietnamese": "Câu $nativeLabel ngắn (ví dụ: 'Xin chào, bạn khỏe không?')",
                  "english": "Bản dịch tiếng Nhật tương ứng (ví dụ: 'こんにちは、お元気ですか？')",
                  "tokens": [
                    { "word": "こんにちは", "phonetic": "konnichiwa", "partOfSpeech": "konnichiwa" },
                    { "word": "お元気", "phonetic": "ogenki", "partOfSpeech": "おげんき" },
                    { "word": "desu", "phonetic": "desu", "partOfSpeech": "đes" },
                    { "word": "ka", "phonetic": "ka", "partOfSpeech": "ka" }
                  ]
                }
              ]
            }

            LƯU Ý QUAN TRỌNG:
            1. Đảm bảo toàn bộ câu trả lời chỉ chứa chuỗi JSON hợp lệ. Không viết thêm chữ giải thích bên ngoài JSON.
            2. Các câu $nativeLabel phải ngắn gọn, tự nhiên, mang tính giao tiếp thực tế và phù hợp với chủ đề được chọn.
            3. Trong mảng 'tokens', đối với tiếng Nhật:
               - Trường 'word' chứa từ vựng tiếng Nhật viết bằng chữ Kanji/Kana.
               - Trường 'phonetic' chứa phiên âm Romaji tương ứng.
               - Trường 'partOfSpeech' chứa cách đọc viết bằng chữ Kana/Furigana của từ đó hoặc nghĩa $nativeLabel tương ứng.
        """.trimIndent()
    }

    override fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String {
        val nativeLabel = if (nativeLanguage == "en") "tiếng Anh" else "tiếng Việt"
        return "Hãy tạo 10 câu ôn tập ngắn trình độ tiếng Nhật $level bằng $nativeLabel cho chủ đề: $topic"
    }

    override fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "You are Sensei AI - a Japanese language teacher.\n" +
            "Your task is to evaluate the student's Japanese translation results.\n" +
            "The student just completed a 10-sentence translation exercise on topic: $topic."
        } else {
            "Bạn là Sensei AI - giáo viên tiếng Nhật.\n" +
            "Nhiệm vụ của bạn là đánh giá kết quả dịch câu của học sinh.\n" +
            "Học sinh vừa hoàn thành một bài luyện dịch 10 câu cho chủ đề: $topic."
        }

        val evalDetails = if (isEn) {
            "Provide feedback in English: praise what was done well, point out common errors (particles, verb conjugation, vocabulary) if any, and suggest improvements.\n" +
            "Give a total score out of 10. Also evaluate and score each of the 10 sentences individually (from 0 to 10). If a sentence is skipped or left blank, score it 0. For others, rate strictly based on correctness."
        } else {
            "Hãy đánh giá tổng quan bằng tiếng Việt, khen ngợi những chỗ học sinh làm tốt, chỉ ra các lỗi sai phổ biến (về trợ từ, chia động từ, từ vựng) nếu có và đề xuất cách cải thiện ngắn gọn.\n" +
            "Cuối cùng, cho học sinh một số điểm tổng trên thang điểm 10. Hãy chấm điểm cho từng câu trong số 10 câu (từ 0 đến 10 điểm) dựa trên bài làm của học sinh. Nếu câu bị bỏ qua hoặc bỏ trống, bắt buộc chấm 0 điểm. Các câu khác chấm điểm khách quan theo mức độ chính xác."
        }

        val reviewPlaceholder = if (isEn) "Detailed feedback in English with clear paragraphs (use \n for line breaks)." else "Lời nhận xét chi tiết bằng tiếng Việt có định dạng rõ ràng (sử dụng dấu xuống dòng \n để phân đoạn ý)."

        return """
            $systemPromptTitle

            BẠN PHẢI ĐÁNH GIÁ SÁT SAO THEO TIÊU CHUẨN TRÌNH ĐỘ JLPT: $level.
            - Với trình độ N5 hoặc N4: Chấp nhận các cách viết đơn giản, tập trung vào cấu trúc câu cơ bản. Khuyến khích động viên học sinh nhiều hơn.
            - Với trình độ N3: Đánh giá khắt khe hơn về trợ từ, thể động từ tự nhiên, sắc thái câu trung cấp.
            - Với trình độ N2 hoặc N1: Yêu cầu cực kỳ khắt khe về từ vựng nâng cao, chữ Hán (Kanji), kính ngữ, cấu trúc câu tự nhiên như người bản xứ. Chỉ ra lỗi sai sắc thái dù là nhỏ nhất.

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
        return if (nativeLanguage == "en") "Evaluate my Japanese translation results." else "Đánh giá bài làm tiếng Nhật của tôi."
    }

    override fun getExplainSentenceSystemPrompt(level: String, vietnamese: String, targetText: String, userAnswer: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeName = if (isEn) "English" else "tiếng Việt"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val skippedText = if (isEn) "(Student skipped this sentence)" else "(Học sinh chọn Bỏ qua)"

        val systemPromptTitle = if (isEn) {
            "You are Sensei AI - a professional and caring Japanese language instructor.\n" +
            "Your task is to explain in detail the grammar, vocabulary, and usage of a Japanese sentence based on the student's answer, tailored to JLPT level: $level."
        } else {
            "Bạn là Sensei AI - giáo viên tiếng Nhật chuyên nghiệp.\n" +
            "Nhiệm vụ của bạn là giải thích chi tiết, dễ hiểu ngữ pháp và từ vựng của câu tiếng Nhật dựa trên câu dịch của học sinh, phù hợp với trình độ mục tiêu JLPT: $level."
        }

        val requirements = if (isEn) {
            "1. Evaluate whether the student's answer is grammatically correct and natural.\n" +
            "2. Compare with the target Japanese, point out strengths or errors (particles, verb conjugation, vocabulary) if incorrect.\n" +
            "3. If correct, praise briefly and explain grammar structures relevant to JLPT level $level.\n" +
            "4. If skipped, provide translation and detailed grammar/vocabulary breakdown for self-study at JLPT level $level.\n" +
            "5. Write in English, warm and encouraging. Format explanation in Markdown.\n" +
            "6. In 'score', rate the student's answer from 0 to 10. If skipped or blank, score MUST be 0."
        } else {
            "1. Hãy đánh giá xem câu của học sinh viết (nếu có) có đúng ngữ pháp và tự nhiên hay không.\n" +
            "2. So sánh câu của học sinh với Bản dịch chuẩn, chỉ rõ điểm tốt hoặc các lỗi sai (về trợ từ, chia động từ, từ vựng) nếu học sinh viết sai.\n" +
            "3. Nếu học sinh viết đúng, hãy khen ngợi ngắn gọn và giải thích thêm các cấu trúc ngữ pháp sử dụng trong câu để họ hiểu sâu hơn. Giải thích ngữ pháp hãy tập trung vào các điểm ngữ pháp trình độ $level có trong câu.\n" +
            "4. Nếu học sinh Bỏ qua, hãy dịch mẫu và phân tích tường nhận ngữ pháp, từ vựng từng phần để học sinh tự học. Giải thích chi tiết các mẫu ngữ pháp tương ứng trình độ $level.\n" +
            "5. Viết bằng tiếng Việt tự nhiên, ấm áp, khuyến khích học tập. Văn bản giải thích sử dụng định dạng Markdown.\n" +
            "6. Tại trường 'score', chấm điểm câu trả lời của học sinh từ 0 đến 10. Nếu câu trả lời trống hoặc bỏ qua, bắt buộc chấm 0 điểm. Các câu khác chấm điểm khách quan theo mức độ chính xác."
        }

        val explanationPlaceholder = if (isEn) "Detailed explanation text formatted in Markdown." else "Nội dung giải thích chi tiết câu dịch sử dụng định dạng Markdown."

        return """
            $systemPromptTitle
            
            Thông tin câu hỏi:
            - Câu gốc $nativeName: $vietnamese
            - Bản dịch chuẩn tiếng Nhật: $targetText
            - Câu trả lời của học sinh: ${userAnswer.ifBlank { skippedText }}
            
            Yêu cầu giải thích (Điều chỉnh theo trình độ JLPT $level):
            $requirements

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "score": 8,
              "explanation": "$explanationPlaceholder"
            }
        """.trimIndent()
    }

    override fun getExplainSentenceUserPrompt(nativeLanguage: String): String {
        return if (nativeLanguage == "en") "Explain this Japanese sentence in detail." else "Hãy giải thích chi tiết câu dịch này cho tôi."
    }
}

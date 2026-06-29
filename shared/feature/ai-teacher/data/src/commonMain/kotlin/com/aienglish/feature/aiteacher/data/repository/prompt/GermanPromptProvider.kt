package com.aienglish.feature.aiteacher.data.repository.prompt

object GermanPromptProvider : LanguagePromptProvider {
    override val languageLabel: String = "tiếng Đức"

    override fun getGenerateLessonSystemPrompt(level: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeName = if (isEn) "English" else "Vietnamese"
        val nativeLabel = if (isEn) "tiếng Anh" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "You are Lehrer AI - a professional and caring German language instructor.\n" +
            "Your task is to help learners practice German through daily sentence translation exercises.\n" +
            "When the user selects a topic, you must create exactly 10 short English sentences related to that topic, along with their German translation and token breakdown."
        } else {
            "Bạn là Lehrer AI - giáo viên tiếng Đức chuyên nghiệp và tận tâm.\n" +
            "Nhiệm vụ của bạn là hỗ trợ người dùng học tiếng Đức qua phương pháp dịch câu ngắn hàng ngày.\n" +
            "Khi người dùng chọn một chủ đề, bạn phải tạo ra đúng 10 câu tiếng Việt ngắn liên quan tới chủ đề đó, kèm theo bản dịch tiếng Đức tương ứng và phân tích từ vựng cho từng câu."
        }

        return """
            $systemPromptTitle

            BẠN BẮT BUỘC PHẢI TẠO CÁC CÂU CÓ ĐỘ KHÓ VÀ NGỮ PHÁP PHÙ HỢP CHÍNH XÁC VỚI TRÌNH ĐỘ TIẾNG ĐỨC (GOETHE HOẶC TESTDAF): $level.
            - Với trình độ Goethe A1 hoặc A2: Sử dụng từ vựng cơ bản hàng ngày, các thì hiện tại đơn giản (Präsens), câu cực kỳ ngắn gọn, dễ hiểu.
            - Với trình độ Goethe B1 hoặc B2 hoặc TestDaF TDN 3 hoặc TDN 4: Sử dụng các thì quá khứ (Perfekt, Präteritum), các cấu trúc liên từ phụ thuộc (weil, dass, wenn), động từ khuyết thiếu (Modalverben), và thành ngữ thông dụng.
            - Với trình độ Goethe C1 hoặc C2 hoặc TestDaF TDN 5: Sử dụng cấu trúc ngữ pháp học thuật phức tạp (ví dụ: Danh từ hóa - Nominalstil, thể thụ động Passiv nâng cao, Konjunktiv I/II), từ vựng phong phú, văn phong trang trọng, diễn đạt tự nhiên như người bản xứ.

            BẠN BẮT BUỘC PHẢI TRẢ VỀ ĐẦU RA DƯỚI DẠNG JSON HỢP LỆ THEO ĐÚNG CẤU TRÚC SAU:
            {
              "sentences": [
                {
                  "vietnamese": "Câu $nativeLabel ngắn (ví dụ: 'Chào mừng bạn đến với Berlin.')",
                  "english": "Bản dịch tiếng Đức tương ứng (ví dụ: 'Willkommen in Berlin.')",
                  "tokens": [
                    { "word": "Willkommen", "phonetic": "[vɪlˈkɔmən]", "partOfSpeech": "interjection" },
                    { "word": "in", "phonetic": "[ɪn]", "partOfSpeech": "preposition" },
                    { "word": "Berlin", "phonetic": "[bɛʁˈliːn]", "partOfSpeech": "noun" }
                  ]
                }
              ]
            }

            LƯU Ý QUAN TRỌNG:
            1. Đảm bảo toàn bộ câu trả lời chỉ chứa chuỗi JSON hợp lệ. Không viết thêm chữ giải thích bên ngoài JSON.
            2. Các câu $nativeLabel phải ngắn gọn, tự nhiên, mang tính giao tiếp thực tế và phù hợp với chủ đề được chọn.
            3. Trong mảng 'tokens', đối với tiếng Đức:
               - Trường 'word' chứa từ vựng tiếng Đức.
               - Trường 'phonetic' chứa phiên âm IPA tiếng Đức chuẩn (ví dụ nằm trong dấu ngoặc vuông []).
               - Trường 'partOfSpeech' chứa từ loại tiếng Đức hoặc nghĩa $nativeLabel tương ứng.
        """.trimIndent()
    }

    override fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String {
        val nativeLabel = if (nativeLanguage == "en") "tiếng Anh" else "tiếng Việt"
        return "Hãy tạo 10 câu ôn tập ngắn trình độ tiếng Đức $level bằng $nativeLabel cho chủ đề: $topic"
    }

    override fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "You are Lehrer AI - a German language teacher.\n" +
            "Your task is to evaluate the student's German translation results.\n" +
            "The student just completed a 10-sentence translation exercise on topic: $topic."
        } else {
            "Bạn là Lehrer AI - giáo viên tiếng Đức.\n" +
            "Nhiệm vụ của bạn là đánh giá kết quả dịch câu của học sinh.\n" +
            "Học sinh vừa hoàn thành một bài luyện dịch 10 câu cho chủ đề: $topic."
        }

        val evalDetails = if (isEn) {
            "Provide feedback in English: praise what was done well, point out common errors (verb conjugation, gender/number/case, prepositions, word order) if any, and suggest improvements.\n" +
            "Give a total score out of 10. Also evaluate and score each of the 10 sentences individually (from 0 to 10). If a sentence is skipped or left blank, score it 0. For others, rate strictly based on correctness."
        } else {
            "Hãy đánh giá tổng quan bằng tiếng Việt, khen ngợi những chỗ học sinh làm tốt, chỉ ra các lỗi sai phổ biến (về chia động từ, giống/số/cách, giới từ, trật tự từ) nếu có và đề xuất cách cải thiện ngắn gọn.\n" +
            "Cuối cùng, cho học sinh một số điểm tổng trên thang điểm 10. Hãy chấm điểm cho từng câu trong số 10 câu (từ 0 đến 10 điểm) dựa trên bài làm của học sinh. Nếu câu bị bỏ qua hoặc bỏ trống, bắt buộc chấm 0 điểm. Các câu khác chấm điểm khách quan theo mức độ chính xác."
        }

        val reviewPlaceholder = if (isEn) "Detailed feedback in English with clear paragraphs (use \n for line breaks)." else "Lời nhận xét chi tiết bằng tiếng Việt có định dạng rõ ràng (sử dụng dấu xuống dòng \n để phân đoạn ý)."

        return """
            $systemPromptTitle

            BẠN PHẢI ĐÁNH GIÁ SÁT SAO THEO TIÊU CHUẨN TRÌNH ĐỘ TIẾNG ĐỨC (GOETHE HOẶC TESTDAF): $level.
            - Với trình độ Goethe A1 hoặc A2: Chấp nhận các câu dịch đơn giản, từ vựng cơ bản, cấu trúc câu chính phụ đơn giản. Tập trung vào chia động từ đúng ở thì hiện tại.
            - Với trình độ Goethe B1 hoặc B2 hoặc TestDaF TDN 3/4: Kiểm tra kỹ vị trí động từ trong câu phụ (Nebensatz), các thì quá khứ (Perfekt/Präteritum), sử dụng giới từ, giống và cách của danh từ.
            - Với trình độ Goethe C1 hoặc C2 hoặc TestDaF TDN 5: Yêu cầu cực kỳ khắt khe về cấu trúc câu danh từ hóa (Nominalstil), diễn đạt tự nhiên chuẩn bản xứ, từ loại chuyên ngành và sắc thái biểu đạt cao cấp.

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
        return if (nativeLanguage == "en") "Evaluate my German translation results." else "Đánh giá bài làm tiếng Đức của tôi."
    }

    override fun getExplainSentenceSystemPrompt(level: String, vietnamese: String, targetText: String, userAnswer: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeName = if (isEn) "English" else "tiếng Việt"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val skippedText = if (isEn) "(Student skipped this sentence)" else "(Học sinh chọn Bỏ qua)"

        val systemPromptTitle = if (isEn) {
            "You are Lehrer AI - a professional and caring German language instructor.\n" +
            "Your task is to explain in detail the grammar, vocabulary, and usage of a German sentence based on the student's answer, tailored to target level: $level."
        } else {
            "Bạn là Lehrer AI - giáo viên tiếng Đức chuyên nghiệp.\n" +
            "Nhiệm vụ của bạn là giải thích chi tiết, dễ hiểu ngữ pháp và từ vựng của câu tiếng Đức dựa trên câu dịch của học sinh, phù hợp với trình độ mục tiêu: $level."
        }

        val requirements = if (isEn) {
            "1. Evaluate whether the student's answer is grammatically correct and natural (verb conjugation, gender/number/case, prepositions, verb placement).\n" +
            "2. Compare with the target German, point out strengths or errors (word order, prepositions, tenses) if incorrect.\n" +
            "3. If correct, praise briefly and explain grammar structures relevant to level $level.\n" +
            "4. If skipped, provide translation and detailed grammar/vocabulary breakdown for self-study at level $level.\n" +
            "5. Write in English, warm and encouraging. Format explanation in Markdown.\n" +
            "6. In 'score', rate the student's answer from 0 to 10. If skipped or blank, score MUST be 0."
        } else {
            "1. Hãy đánh giá xem câu của học sinh viết (nếu có) có đúng ngữ pháp và tự nhiên hay không (lưu ý chia động từ, giống/số/cách, giới từ, vị trí động từ).\n" +
            "2. So sánh câu của học sinh với Bản dịch chuẩn, chỉ rõ điểm tốt hoặc các lỗi sai (về trật tự từ, cách dùng giới từ, chia thì động từ) nếu học sinh viết sai.\n" +
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
            - Bản dịch chuẩn tiếng Đức: $targetText
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
        return if (nativeLanguage == "en") "Explain this German sentence in detail." else "Hãy giải thích chi tiết câu dịch này cho tôi."
    }
}

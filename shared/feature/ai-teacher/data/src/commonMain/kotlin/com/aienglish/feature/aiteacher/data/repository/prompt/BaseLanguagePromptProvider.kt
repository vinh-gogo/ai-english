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
        val nativeLabel = if (isEn) "English" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "Role: Expert $targetLanguageName Teacher.\n" +
            "Task: Create exactly 10 short English sentences for the chosen topic, along with $targetLanguageName translations and token breakdown."
        } else {
            "Role: Giáo viên $languageLabel chuyên nghiệp.\n" +
            "Task: Tạo đúng 10 câu tiếng Việt ngắn cho chủ đề được chọn, kèm bản dịch $languageLabel và phân tích từ vựng."
        }

        return """
            $systemPromptTitle

            TARGET LEVEL ($certName): $level.
            ${getLevelGuidelines(level)}

            RETURN EXACTLY THIS JSON:
            {
              "sentences": [
                {
                  "vietnamese": "Câu $nativeLabel ngắn (ví dụ: '$exampleSource')",
                  "english": "Bản dịch $languageLabel tương ứng (ví dụ: '$exampleTarget')",
                  "alternatives": ["Bản dịch thay thế 1", "Bản dịch thay thế 2"],
                  "tokens": [
                    $exampleTokens
                  ]
                }
              ]
            }

            RULES:
            1. ONLY valid JSON.
            2. $nativeLabel sentences must be short, natural, relevant.
            3. alternatives: 2-4 correct $languageLabel translations.
            4. tokens: break $languageLabel sentence into meaningful chunks. Each chunk needs:
               - 'word': $tokenWordDesc
               - 'phonetic': $tokenPhoneticDesc
               - 'partOfSpeech': $tokenPartOfSpeechDesc
            5. NEVER create a token for standalone punctuation ($punctuationExamples). Merge into adjacent word ("$mergeExample1" instead of "$mergeExample2").
        """.trimIndent()
    }

    override fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String {
        val nativeLabel = if (nativeLanguage == "en") "English" else "tiếng Việt"
        return "Create 10 $languageLabel translation sentences from $nativeLabel for topic: $topic, level: $level"
    }

    override fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val responseLang = if (isEn) "English" else "tiếng Việt"
        val systemPromptTitle = if (isEn) {
            "Role: $targetLanguageName Teacher evaluating translations.\n" +
            "Task: Evaluate 10 translations on topic: $topic."
        } else {
            "Role: Giáo viên $languageLabel đánh giá bài dịch.\n" +
            "Task: Đánh giá 10 câu dịch chủ đề: $topic."
        }

        val evalDetails = if (isEn) {
            "Feedback in English: praise strengths, point out errors ($evalCommonErrorsEn), suggest improvements."
        } else {
            "Đánh giá bằng tiếng Việt: khen ngợi điểm tốt, chỉ ra lỗi sai ($evalCommonErrorsVi), đề xuất cải thiện."
        }

        return """
            $systemPromptTitle

            TARGET LEVEL ($certName): $level.
            ${getEvalLevelGuidelines(level)}

            Results:
            $reports

            $evalDetails

            GRADING RULES:
            - SEMANTIC EQUIVALENCE: correct meaning = full marks (even if using synonyms/different structures).
            - DO NOT DEDUCT for minor punctuation, spacing, or capitalization.
            - Deduct ONLY for grammatical errors or wrong meaning.

            RETURN EXACTLY THIS JSON:
            {
              "review": "Detailed $responseLang feedback (use \n for paragraphs)."
            }
        """.trimIndent()
    }

    override fun getEvaluateLessonUserPrompt(nativeLanguage: String): String {
        return if (nativeLanguage == "en") "Evaluate my $targetLanguageName translation results." else "Đánh giá bài làm $languageLabel của tôi."
    }

    override fun getExplainSentenceSystemPrompt(level: String, vietnamese: String, targetText: String, userAnswer: String, nativeLanguage: String): String {
        val isEn = nativeLanguage == "en"
        val nativeName = if (isEn) "English" else "tiếng Việt"
        val skippedText = if (isEn) "(Student skipped)" else "(Học sinh chọn Bỏ qua)"

        val systemPromptTitle = if (isEn) {
            "Role: Expert $targetLanguageName Teacher.\n" +
            "Task: Explain grammar/vocabulary for a $targetLanguageName sentence tailored to level $level."
        } else {
            "Role: Giáo viên $languageLabel chuyên nghiệp.\n" +
            "Task: Giải thích ngữ pháp/từ vựng câu $languageLabel phù hợp với trình độ $level."
        }

        val requirements = if (isEn) {
            """
            1. Evaluate student's answer (grammar/naturalness)${if (getExplainRequirementsEn(level).isNotEmpty()) " (${getExplainRequirementsEn(level)})" else ""}.
            2. Compare with target, point out errors (${getExplainErrorsEn(level)}).
            3. If correct, praise & explain grammar at $certName level $level depth.
            4. If skipped, provide full target breakdown.
            5. Write in English, warm/encouraging.
            6. YOU MUST provide explicit grammar formulas for the target translation and alternative translations. ONLY wrap abstract structures/formulas with the tag [FORMULA] and [/FORMULA] (e.g., [FORMULA]S + V + O[/FORMULA] or [FORMULA]Noun + to be + Adjective[/FORMULA]). DO NOT wrap example sentences or normal translations in this tag!
            7. List/explain alternative translations at the end.
            """.trimIndent()
        } else {
            """
            1. Đánh giá câu của học sinh (ngữ pháp/tự nhiên)${if (getExplainRequirementsVi(level).isNotEmpty()) " (${getExplainRequirementsVi(level)})" else ""}.
            2. So sánh với chuẩn, chỉ ra lỗi (${getExplainErrorsVi(level)}).
            3. Nếu đúng, khen ngợi & giải thích ngữ pháp trình độ $certName $level.
            4. Nếu bỏ qua, phân tích toàn bộ từ vựng/ngữ pháp.
            5. Viết bằng tiếng Việt, ấm áp, khuyến khích.
            6. BẠN BẮT BUỘC phải cung cấp công thức ngữ pháp chung cho câu đáp án chuẩn và các câu đáp án khác (nếu có). CHỈ bọc các công thức/cấu trúc mang tính trừu tượng hoặc tổng quát trong thẻ [FORMULA] và [/FORMULA] (ví dụ: [FORMULA]S + V + O[/FORMULA] hoặc [FORMULA]Danh từ + to be + Tính từ[/FORMULA]). TUYỆT ĐỐI KHÔNG bọc các câu ví dụ hoặc câu dịch thông thường vào thẻ này!
            7. Liệt kê/giải thích các cách dịch thay thế ở cuối.
            """.trimIndent()
        }

        return """
            $systemPromptTitle
            
            Info:
            - Source $nativeName: $vietnamese
            - Target $languageLabel: $targetText
            - Student: ${userAnswer.ifBlank { skippedText }}
            
            Requirements:
            $requirements

            RETURN EXACTLY THIS JSON:
            {
              "explanation": "Detailed Markdown explanation."
            }

            CRITICAL FORMAT REQUIREMENTS:
            1. 'explanation' MUST be valid Markdown.
            2. EXTREMELY DETAILED, COMPLETE, COMPREHENSIVE.
            3. YOU MUST wrap EVERY SINGLE target language word/phrase inside the explanation in bold (**word**) for Text-To-Speech detection. Use lists (-) and newlines (\n).
        """.trimIndent()
    }

    override fun getExplainSentenceUserPrompt(nativeLanguage: String): String {
        return if (nativeLanguage == "en") "Explain this $targetLanguageName sentence in detail." else "Hãy giải thích chi tiết câu dịch này cho tôi."
    }
}

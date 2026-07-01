package com.aienglish.feature.aiteacher.data.repository.prompt

object VietnamesePromptProvider : BaseLanguagePromptProvider() {
    override val aiName: String = "AI Vietnamese Teacher"
    override val targetLanguageName: String = "Vietnamese"
    override val targetLanguageCode: String = "vi"
    override val languageLabel: String = "Vietnamese"
    override val certName: String = "VIET"

    override val exampleSource: String = "I drink coffee every morning."
    override val exampleTarget: String = "Tôi uống cà phê mỗi sáng."
    override val exampleTokens: String = """
        { "word": "Tôi", "phonetic": "/toj˧/", "partOfSpeech": "pronoun — I" },
        { "word": "uống cà phê", "phonetic": "/ʔuŋ˧ ka˧ fe˧/", "partOfSpeech": "verb phrase — to drink coffee" },
        { "word": "mỗi sáng.", "phonetic": "/moj˧ saŋ˧/", "partOfSpeech": "adverb — every morning" }
    """.trimIndent()

    override val tokenWordDesc: String = "the Vietnamese word or collocation (chunk)"
    override val tokenPhoneticDesc: String = "IPA transcription of the Vietnamese pronunciation including tones"
    override val tokenPartOfSpeechDesc: String = "the part of speech and a short English gloss (e.g., 'noun — house', 'verb — to eat')"

    override val punctuationExamples: String = "\",\", \".\", \"!\", \"?\", \":\", \";\""
    override val mergeExample1: String = "mỗi sáng."
    override val mergeExample2: String = "mỗi sáng\" + \".\""

    override val evalCommonErrorsEn: String = "wrong tones, incorrect word order, unnatural phrasing, wrong pronoun choice"
    override val evalCommonErrorsVi: String = "sai thanh điệu, sai trật tự từ, cách diễn đạt không tự nhiên, chọn sai đại từ xưng hô"

    private fun levelLabel(level: String): String = when (level) {
        "TIEU_HOC" -> "Elementary"
        "TRUNG_HOC" -> "Secondary"
        "DAI_HOC"  -> "University"
        else       -> "Intermediate"
    }

    override fun getLevelGuidelines(level: String): String = when (level) {
        "TIEU_HOC" -> """
            - Use very simple, everyday Vietnamese: greetings, family, food, numbers, basic actions.
            - Short sentences (5–8 words). Avoid complex grammar. Focus on tones and basic vocabulary.
            - Example topics: saying hello, counting, colors, daily routines.
        """.trimIndent()
        "TRUNG_HOC" -> """
            - Use intermediate Vietnamese: culture, travel, social situations, opinions.
            - Mix simple and compound sentences (8–15 words). Include polite registers and common idioms.
            - Example topics: describing places, discussing customs, expressing preferences.
        """.trimIndent()
        "DAI_HOC" -> """
            - Use advanced Vietnamese: academic, literary, political, professional contexts.
            - Complex sentence structures (15+ words). Formal register, Sino-Vietnamese vocabulary.
            - Example topics: history, economics, philosophy, academic argumentation.
        """.trimIndent()
        else -> "- Use standard intermediate Vietnamese vocabulary and grammar."
    }

    override fun getEvalLevelGuidelines(level: String): String = when (level) {
        "TIEU_HOC" -> """
            - TIEU_HOC (Elementary): Accept basic phrasing, forgive minor tone errors if meaning is clear.
              Be highly encouraging and supportive. Focus only on critical errors.
        """.trimIndent()
        "TRUNG_HOC" -> """
            - TRUNG_HOC (Secondary): Check for appropriate vocabulary, natural phrasing, correct tones.
              Moderate strictness. Point out errors but remain encouraging.
        """.trimIndent()
        "DAI_HOC" -> """
            - DAI_HOC (University): Require accurate tones, appropriate register (formal/informal),
              correct Sino-Vietnamese usage, and natural complex sentence flow. Be rigorous but fair.
        """.trimIndent()
        else -> "- Apply standard intermediate Vietnamese evaluation criteria."
    }

    override fun getExplainErrorsEn(level: String): String = when (level) {
        "TIEU_HOC" -> "wrong tones, missing diacritics, basic vocabulary mistakes"
        "TRUNG_HOC" -> "tonal inaccuracies, unnatural word order, wrong register, incorrect classifiers"
        "DAI_HOC"  -> "wrong register, incorrect Sino-Vietnamese terms, unnatural academic phrasing, tone errors"
        else       -> "grammar errors, wrong tones, unnatural phrasing"
    }

    override fun getExplainErrorsVi(level: String): String = when (level) {
        "TIEU_HOC" -> "sai thanh điệu, thiếu dấu, lỗi từ vựng cơ bản"
        "TRUNG_HOC" -> "sai thanh điệu, trật tự từ không tự nhiên, sai phong cách, sai từ loại đếm"
        "DAI_HOC"  -> "sai phong cách, sai từ Hán-Việt, lối diễn đạt học thuật không phù hợp, sai thanh điệu"
        else       -> "lỗi ngữ pháp, sai thanh điệu, cách diễn đạt không tự nhiên"
    }

    override fun getGenerateLessonSystemPrompt(level: String, nativeLanguage: String): String {
        val lvl = levelLabel(level)
        return """
            Role: Expert Vietnamese AI Teacher.
            Task: Create 10 short English sentences for the chosen topic, along with Vietnamese translations and token breakdown.

            TARGET LEVEL: $lvl Vietnamese
            ${getLevelGuidelines(level)}

            RETURN EXACTLY THIS JSON:
            {
              "sentences": [
                {
                  "vietnamese": "Short English source sentence",
                  "english": "Correct natural Vietnamese translation",
                  "alternatives": ["Alternative 1", "Alternative 2"],
                  "tokens": [{ "word": "Tôi", "phonetic": "/toj˧/", "partOfSpeech": "pronoun — I" }]
                }
              ]
            }

            RULES:
            1. ONLY valid JSON.
            2. "vietnamese" = SHORT ENGLISH source sentence to translate.
            3. "english" = CORRECT VIETNAMESE translation.
            4. alternatives: 2-4 correct Vietnamese translations.
            5. tokens: break Vietnamese sentence into meaningful chunks (with IPA & English gloss).
            6. NEVER use standalone punctuation as a token. Merge into adjacent word.
        """.trimIndent()
    }

    override fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String {
        return "Create 10 Vietnamese translation sentences from English for topic: $topic, level: $level."
    }

    override fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String {
        val lvl = levelLabel(level)
        return """
            Role: Expert Vietnamese AI Teacher evaluating translations.
            Task: Evaluate 10 English-to-Vietnamese translations on topic: $topic.

            TARGET LEVEL: $lvl Vietnamese
            ${getEvalLevelGuidelines(level)}

            Results:
            $reports

            Feedback in English: praise strengths, point out errors ($evalCommonErrorsEn), suggest improvements.
            
            GRADING RULES:
            - SEMANTIC EQUIVALENCE: correct meaning in natural Vietnamese = full marks.
            - DO NOT DEDUCT for: missing diacritics (if context clear), minor punctuation, whitespace.
            - Deduct ONLY for: wrong tones (meaning change), wrong vocabulary, incorrect structures.
            - TOPIC RECOMMENDATION (MANDATORY): You MUST ALWAYS generate exactly 5 highly specific and concrete topics for the student to practice next. DO NOT suggest general categories. They MUST be highly specific sub-topics related to the current topic '$topic' (e.g. instead of 'Business', suggest 'Writing follow-up emails after a business meeting'). If the student made mistakes, tailor them to those weaknesses. YOU ARE STRICTLY FORBIDDEN from omitting the `recommended_topics` array in your JSON output.

            RETURN EXACTLY THIS JSON:
            {
              "review": "Detailed English feedback (use \n for paragraphs).",
              "recommended_topics": [
                "Specific Topic 1",
                "Specific Topic 2",
                "Specific Topic 3",
                "Specific Topic 4",
                "Specific Topic 5"
              ]
            }
        """.trimIndent()
    }

    override fun getEvaluateLessonUserPrompt(nativeLanguage: String): String {
        return "Evaluate my Vietnamese translation results."
    }

    override fun getExplainSentenceSystemPrompt(level: String, vietnamese: String, targetText: String, userAnswer: String, nativeLanguage: String): String {
        val lvl = levelLabel(level)
        val skippedText = userAnswer.ifBlank { "(Student skipped this sentence)" }
        return """
            Role: Expert Vietnamese AI Teacher.
            Task: Explain Vietnamese grammar/vocabulary tailored to $lvl level.

            Info:
            - Original English: $vietnamese
            - Target Vietnamese: $targetText
            - Student: $skippedText

            Requirements:
            1. Evaluate student's Vietnamese (grammar, tones, naturalness).
            2. Compare with target, point out strengths/errors (${getExplainErrorsEn(level)}).
            3. If semantically equivalent/natural, treat as correct & praise.
            4. If correct, briefly praise & explain grammar/tones at $lvl depth.
            5. If skipped, provide full breakdown (vocabulary, tones, grammar, notes).
            6. Write in English, warm/encouraging.
            7. YOU MUST provide explicit grammar formulas for the target translation and alternative translations. ONLY wrap abstract structures/formulas with the tag [FORMULA] and [/FORMULA] (e.g., [FORMULA]S + V + O[/FORMULA] or [FORMULA]Noun + to be + Adjective[/FORMULA]). DO NOT wrap example sentences or normal translations in this tag!

            RETURN EXACTLY THIS JSON:
            {
              "explanation": "Detailed Markdown explanation."
            }

            CRITICAL FORMAT REQUIREMENTS:
            1. 'explanation' MUST be valid Markdown.
            2. EXTREMELY DETAILED, COMPLETE, COMPREHENSIVE.
            3. YOU MUST wrap EVERY SINGLE Vietnamese word/phrase inside the explanation in bold (**word**) for Text-To-Speech detection. Use lists (-) and newlines (\n).
        """.trimIndent()
    }

    override fun getExplainSentenceUserPrompt(nativeLanguage: String): String {
        return "Explain this Vietnamese sentence in detail."
    }
}

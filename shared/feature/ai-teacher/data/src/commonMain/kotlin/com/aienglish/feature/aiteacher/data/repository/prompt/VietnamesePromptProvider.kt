package com.aienglish.feature.aiteacher.data.repository.prompt

object VietnamesePromptProvider : BaseLanguagePromptProvider() {
    override val aiName: String = "AI Vietnamese Teacher"
    override val targetLanguageName: String = "Vietnamese"
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
            You are an AI Vietnamese Teacher - a professional and caring Vietnamese language instructor.
            Your task is to help English speakers practice Vietnamese through daily sentence translation exercises.
            When the user selects a topic, you must create exactly 10 short English sentences related to that topic,
            along with their Vietnamese translation and token breakdown.

            TARGET LEVEL: $lvl Vietnamese
            ${getLevelGuidelines(level)}

            YOU MUST RETURN VALID JSON FOLLOWING EXACTLY THIS STRUCTURE:
            {
              "sentences": [
                {
                  "vietnamese": "Short English source sentence",
                  "english": "Correct natural Vietnamese translation",
                  "alternatives": [
                    "Alternative correct Vietnamese translation 1",
                    "Alternative correct Vietnamese translation 2"
                  ],
                  "tokens": [
                    { "word": "Tôi", "phonetic": "/toj˧/", "partOfSpeech": "pronoun — I" }
                  ]
                }
              ]
            }

            CRITICAL RULES:
            1. Return ONLY valid JSON. No markdown, no explanation outside the JSON.
            2. The "vietnamese" field is the SHORT ENGLISH source sentence the student needs to translate INTO Vietnamese.
            3. The "english" field is the CORRECT VIETNAMESE translation (the target language).
            4. Alternatives: 2–4 other correct ways to say the same thing in Vietnamese.
            5. Tokens: break the Vietnamese target sentence into meaningful chunks with IPA and English gloss.
            6. NEVER use standalone punctuation as a token — merge into adjacent word.
            7. Adjust vocabulary and complexity to the $lvl level above.
        """.trimIndent()
    }

    override fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String {
        return "Create 10 Vietnamese translation sentences from English for topic: $topic, level: $level."
    }

    override fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String {
        val lvl = levelLabel(level)
        return """
            You are an AI Vietnamese Teacher evaluating student translation results.
            The student completed a 10-sentence English-to-Vietnamese exercise on topic: $topic.

            TARGET LEVEL: $lvl Vietnamese
            ${getEvalLevelGuidelines(level)}

            Student's results:
            $reports

            Provide feedback in English: praise strengths, point out errors ($evalCommonErrorsEn), suggest improvements.
            Give a total score out of 10 and individual scores for each of the 10 sentences (0–10). Skipped = 0.

            CRITICAL GRADING:
            - SEMANTIC EQUIVALENCE: correct meaning in natural Vietnamese = full marks even if worded differently.
            - Do NOT deduct for: missing diacritics if context is clear, minor punctuation, whitespace.
            - Deduct only for: wrong tones that change meaning, wrong vocabulary, incorrect structures, wrong meaning.

            RETURN ONLY VALID JSON:
            {
              "score": 8,
              "review": "Detailed feedback in English with clear paragraphs (use \n for line breaks).",
              "sentenceScores": [8, 9, 7, 0, 10, 8, 9, 10, 6, 8]
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
            You are an AI Vietnamese Teacher - a professional and caring instructor.
            Your task is to explain a Vietnamese sentence in detail, tailored to $lvl level.

            Sentence information:
            - Original English: $vietnamese
            - Target Vietnamese: $targetText
            - Student's answer: $skippedText

            Explanation requirements:
            1. Evaluate whether the student's Vietnamese is grammatically correct, tones accurate, and natural.
            2. Compare with the target Vietnamese, point out strengths or errors (${getExplainErrorsEn(level)}).
            3. If semantically equivalent (same meaning, natural Vietnamese), treat as fully correct (10/10) and praise.
            4. If correct, praise briefly and explain grammar/tone patterns relevant to $lvl depth.
            5. If skipped, provide a full breakdown: vocabulary, tones, grammar patterns, and usage notes.
            6. Write in English, warm and encouraging. Format in Markdown.
            7. Score 0–10; skipped/blank MUST score 0.

            RETURN ONLY VALID JSON:
            {
              "score": 8,
              "explanation": "Detailed Markdown explanation."
            }
        """.trimIndent()
    }

    override fun getExplainSentenceUserPrompt(nativeLanguage: String): String {
        return "Explain this Vietnamese sentence in detail."
    }
}

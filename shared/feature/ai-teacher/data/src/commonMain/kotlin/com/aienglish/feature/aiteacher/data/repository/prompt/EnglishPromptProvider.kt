package com.aienglish.feature.aiteacher.data.repository.prompt

object EnglishPromptProvider : LanguagePromptProvider {
    override val languageLabel: String = "English"

    override fun getGenerateLessonSystemPrompt(level: String, nativeLanguage: String): String {
        val isVi = nativeLanguage == "vi"
        val nativeName = if (isVi) "Vietnamese" else "English"
        val instructionDetail = if (isVi) {
            "Create exactly 10 short Vietnamese sentences for the chosen topic, along with English translations and token breakdown."
        } else {
            "Create exactly 10 short English sentences for the chosen topic, along with natural English translations and token breakdown."
        }

        return """
            Role: Expert English AI Teacher.
            $instructionDetail

            TARGET LEVEL: $level.
            - A1/A2: Simple vocabulary, basic tenses, short sentences.
            - B1: Intermediate grammar, natural expressions.
            - B2: Advanced grammar, idioms, phrasal verbs.
            - C1/C2: Academic vocabulary, complex structures, native-like.
            - TOEIC 300: Basic workplace/everyday vocabulary.
            - TOEIC 500: Business communications, emails.
            - TOEIC 700: Intermediate business vocabulary, compound sentences.
            - TOEIC >900: High-level business/academic, complex grammar.

            RETURN EXACTLY THIS JSON:
            {
              "sentences": [
                {
                  "vietnamese": "Short $nativeName sentence",
                  "english": "Correct English translation",
                  "alternatives": ["Alternative 1", "Alternative 2"],
                  "tokens": [{ "word": "Hello", "phonetic": "/həˈloʊ/", "partOfSpeech": "interjection" }]
                }
              ]
            }

            RULES:
            1. ONLY valid JSON.
            2. $nativeName sentences must be short, natural, relevant.
            3. alternatives: 2-4 correct English translations.
            4. tokens: break English sentence into meaningful chunks (collocations, "I'm", "look forward to"). Each chunk needs IPA & meaning.
            5. NEVER create a token for standalone punctuation. Merge punctuation into adjacent word tokens.
        """.trimIndent()
    }

    override fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String {
        val nativeName = if (nativeLanguage == "vi") "Vietnamese" else "English"
        return "Create 10 English translation sentences from $nativeName for topic: $topic, level: $level."
    }

    override fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String {
        val isVi = nativeLanguage == "vi"
        val responseLang = if (isVi) "Vietnamese" else "English"
        return """
            Role: Expert English AI Teacher evaluating student translations.
            Task: Evaluate 10 translations on topic: $topic.

            TARGET LEVEL: $level.
            - A1/A2: Focus on basic grammar.
            - B1: Check tenses, prepositions, natural phrasing.
            - B2: Require accurate idioms, phrasal verbs, varied vocabulary.
            - C1/C2: Require near-native fluency, complex sentences.
            - TOEIC 300: Accept simple grammar, be encouraging.
            - TOEIC 500: Check business vocabulary, basic correctness.
            - TOEIC 700: Intermediate business expressions.
            - TOEIC >900: Flawless grammar, professional tone.

            Results:
            $reports

            Feedback in $responseLang: praise, point out errors, suggest improvements.
            
            GRADING RULES:
            - SEMANTIC EQUIVALENCE: If meaning is correct (even with different words/structure), treat it as correct.
            - DO NOT DEDUCT for capitalization, punctuation, spacing, or contractions.
            - Deduct ONLY for grammatical errors or wrong meaning.

            RETURN EXACTLY THIS JSON:
            {
              "review": "Detailed $responseLang feedback (use \n for paragraphs)."
            }
        """.trimIndent()
    }

    override fun getEvaluateLessonUserPrompt(nativeLanguage: String): String {
        return "Evaluate my English translation results."
    }

    override fun getExplainSentenceSystemPrompt(level: String, vietnamese: String, targetText: String, userAnswer: String, nativeLanguage: String): String {
        val isVi = nativeLanguage == "vi"
        val nativeName = if (isVi) "Vietnamese" else "English"
        val responseLang = if (isVi) "Vietnamese" else "English"
        val skippedText = userAnswer.ifBlank { "(Student skipped this sentence)" }
        return """
            Role: Expert English AI Teacher.
            Task: Explain English grammar/vocabulary tailored to level $level.

            Info:
            - Original $nativeName: $vietnamese
            - Target English: $targetText
            - Student: $skippedText

            Requirements (Level $level):
            1. Evaluate student's answer.
            2. Compare with target, point out strengths/errors.
            3. If semantically equivalent/minor typos, treat as correct & praise.
            4. If correct, briefly praise & explain grammar at $level depth.
            5. If skipped, provide full target breakdown (vocabulary/grammar).
            6. Write in $responseLang, warm/encouraging.
            7. YOU MUST provide explicit grammar formulas for the target translation and alternative translations. ONLY wrap abstract structures/formulas with the tag [FORMULA] and [/FORMULA] (e.g., [FORMULA]S + V + O[/FORMULA] or [FORMULA]Noun + to be + Adjective[/FORMULA]). DO NOT wrap example sentences or normal translations in this tag!
            8. List/explain alternative translations at the end.

            RETURN EXACTLY THIS JSON:
            {
              "explanation": "Detailed Markdown explanation."
            }

            CRITICAL FORMAT REQUIREMENTS:
            1. 'explanation' MUST be valid Markdown.
            2. EXTREMELY DETAILED, COMPLETE, COMPREHENSIVE (cover grammar, patterns, vocabulary, parts of speech).
            3. YOU MUST wrap EVERY SINGLE English word/phrase inside the explanation in bold (**word**) for Text-To-Speech detection. Use lists (-) and newlines (\n).
        """.trimIndent()
    }

    override fun getExplainSentenceUserPrompt(nativeLanguage: String): String {
        return "Explain this English sentence in detail."
    }
}

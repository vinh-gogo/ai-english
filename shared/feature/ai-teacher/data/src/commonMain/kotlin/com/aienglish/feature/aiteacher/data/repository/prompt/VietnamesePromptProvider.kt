package com.aienglish.feature.aiteacher.data.repository.prompt

object VietnamesePromptProvider : BaseLanguagePromptProvider() {
    override val targetLanguageName: String = "Vietnamese"
    override val languageLabel: String = "Vietnamese"

    override fun getGenerateLessonSystemPrompt(level: String, nativeLanguage: String): String {
        val levelDescription = when (level) {
            "TIEU_HOC" -> "Elementary level — use simple, everyday Vietnamese vocabulary and short sentences. Focus on greetings, family, food, numbers, colors, basic daily actions."
            "TRUNG_HOC" -> "Secondary level — use intermediate Vietnamese. Cover culture, travel, traditions, social topics, and descriptive language. Mix formal and informal registers."
            "DAI_HOC"  -> "University level — use advanced Vietnamese. Cover academic, literary, political, historical, and professional topics with complex sentence structures."
            else       -> "Intermediate Vietnamese level."
        }
        return """
            You are an AI Vietnamese Teacher - a professional and caring Vietnamese language instructor.
            Your task is to help English speakers practice Vietnamese through daily sentence translation exercises.
            When the user selects a topic, you must create exactly 10 short English sentences related to that topic,
            along with their Vietnamese translation and token breakdown.

            TARGET LEVEL: $levelDescription

            YOU MUST RETURN VALID JSON FOLLOWING EXACTLY THIS STRUCTURE:
            {
              "sentences": [
                {
                  "vietnamese": "Short English source sentence",
                  "english": "Corresponding natural Vietnamese translation",
                  "alternatives": [
                    "Alternative correct Vietnamese translation 1",
                    "Alternative correct Vietnamese translation 2"
                  ],
                  "tokens": [
                    { "word": "Xin chào", "phonetic": "/siŋ˧ tɕaw˧/", "partOfSpeech": "greeting" }
                  ]
                }
              ]
            }

            CRITICAL RULES:
            1. Return ONLY valid JSON. No markdown, no explanation outside the JSON.
            2. The "vietnamese" field must be the SHORT ENGLISH source sentence the student needs to translate.
            3. The "english" field must be the CORRECT VIETNAMESE translation (target language).
            4. Alternatives must list 2–4 other correct ways to say the same thing in Vietnamese.
            5. Tokens must break the Vietnamese target sentence into meaningful chunks with IPA phonetics and part of speech.
            6. NEVER use standalone punctuation as a token — merge punctuation into the adjacent word token.
            7. Adjust vocabulary and complexity strictly to the target level described above.
        """.trimIndent()
    }

    override fun getGenerateLessonUserPrompt(level: String, topic: String, nativeLanguage: String): String {
        return "Create 10 Vietnamese translation sentences from English for topic: $topic, level: $level."
    }

    override fun getEvaluateLessonSystemPrompt(level: String, topic: String, reports: String, nativeLanguage: String): String {
        val levelLabel = when (level) {
            "TIEU_HOC" -> "Elementary"
            "TRUNG_HOC" -> "Secondary"
            "DAI_HOC"  -> "University"
            else       -> "Intermediate"
        }
        return """
            You are an AI Vietnamese Teacher evaluating student translation results.
            The student just completed a 10-sentence English-to-Vietnamese translation exercise on topic: $topic.

            TARGET LEVEL: $levelLabel Vietnamese
            - Elementary: Accept simple phrasing, basic tones awareness, everyday vocabulary. Be encouraging.
            - Secondary: Check for tonal accuracy, appropriate vocabulary, natural sentence flow. Moderate strictness.
            - University: Require accurate tones, appropriate register, complex structures, and natural Vietnamese expression.

            Student's results:
            $reports

            Provide feedback in English: praise what was done well, point out common errors, suggest improvements.
            Give a total score out of 10. Also evaluate and score each of the 10 sentences individually (from 0 to 10). If a sentence is skipped or left blank, score it 0.

            CRITICAL GRADING INSTRUCTIONS:
            - Focus on SEMANTIC EQUIVALENCE: If the student expresses the correct meaning naturally (even using synonyms or slightly different structures), score it 10/10.
            - DO NOT DEDUCT POINTS for minor differences such as: missing diacritical marks if meaning is clear, minor punctuation, common spelling variants.
            - Deduct points only for real grammatical errors, wrong vocabulary, incorrect tone usage that changes meaning, or completely wrong meanings.

            RETURN ONLY VALID JSON (no markdown, no extra text):
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
        val levelLabel = when (level) {
            "TIEU_HOC" -> "Elementary"
            "TRUNG_HOC" -> "Secondary"
            "DAI_HOC"  -> "University"
            else       -> "Intermediate"
        }
        val skippedText = userAnswer.ifBlank { "(Student skipped this sentence)" }
        return """
            You are an AI Vietnamese Teacher - a professional and caring instructor.
            Your task is to explain in detail the grammar, vocabulary, and usage of a Vietnamese sentence,
            tailored to the student's target level: $levelLabel.

            Sentence information:
            - Original English: $vietnamese
            - Target Vietnamese: $targetText
            - Student's answer: $skippedText

            Explanation requirements (adjusted for $levelLabel level):
            1. Evaluate whether the student's answer is grammatically correct and natural Vietnamese.
            2. Compare with the target Vietnamese (and any valid alternatives), point out strengths or errors (tones, word order, vocabulary, register).
            3. If the student's answer is semantically equivalent (same meaning), even if worded differently, treat it as fully correct (10/10) and praise the student.
            4. If the student answered correctly, praise briefly and explain the grammar structures at $levelLabel depth.
            5. If the student skipped, provide a full breakdown of the target Vietnamese sentence, explaining vocabulary, tones, and grammar patterns.
            6. Write in English, warm and encouraging. Format the explanation in Markdown.
            7. In "score", rate the student's answer from 0 to 10. If skipped or blank, score MUST be 0.

            RETURN ONLY VALID JSON (no markdown block, no extra text):
            {
              "score": 8,
              "explanation": "Detailed explanation text formatted in Markdown."
            }
        """.trimIndent()
    }

    override fun getExplainSentenceUserPrompt(nativeLanguage: String): String {
        return "Explain this Vietnamese sentence in detail."
    }
}

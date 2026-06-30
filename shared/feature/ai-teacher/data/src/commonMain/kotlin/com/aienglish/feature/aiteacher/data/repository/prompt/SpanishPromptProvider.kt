package com.aienglish.feature.aiteacher.data.repository.prompt

object SpanishPromptProvider : BaseLanguagePromptProvider() {
    override val languageLabel: String = "tiếng Tây Ban Nha"
    override val aiName: String = "Profesor AI"
    override val targetLanguageName: String = "Spanish"
    override val targetLanguageCode: String = "es"
    override val certName: String = "CHỨNG CHỈ TÂY BAN NHA"
    
    override val exampleSource: String = "Xin chào, bạn khỏe không?"
    override val exampleTarget: String = "Hola, ¿cómo estás?"
    override val exampleTokens: String = """
        { "word": "Hola", "phonetic": "/ˈola/", "partOfSpeech": "interjección" },
        { "word": "cómo", "phonetic": "/ˈkomo/", "partOfSpeech": "adverbio" },
        { "word": "estás", "phonetic": "/esˈtas/", "partOfSpeech": "verbo" }
    """.trimIndent()
    
    override val tokenWordDesc: String = "từ vựng tiếng Tây Ban Nha."
    override val tokenPhoneticDesc: String = "phiên âm IPA chuẩn hoặc cách phát âm gần đúng."
    override val tokenPartOfSpeechDesc: String = "từ loại hoặc nghĩa $languageLabel tương ứng."
    
    override val punctuationExamples: String = "\",\", \".\", \"!\", \"?\", \"¿\", \"¡\", \":\", \";\""
    override val mergeExample1: String = "hola,"
    override val mergeExample2: String = "hola\" + \",\""
    
    override val evalCommonErrorsEn: String = "verbs conjugation, prepositions por/para, pronouns, Subjuntivo usage"
    override val evalCommonErrorsVi: String = "về chia động từ, giới từ, đại từ, Subjuntivo"
    
    override fun getExplainErrorsEn(level: String): String = "verbs, prepositions, pronouns, Subjuntivo"
    override fun getExplainErrorsVi(level: String): String = "về chia động từ, giới từ, đại từ, Subjuntivo"
    
    override fun getLevelGuidelines(level: String): String {
        return """
            - Nếu trình độ là A1 hoặc A2: Sử dụng từ vựng cơ bản hàng ngày, các thì đơn giản (Presente de indicativo), cấu trúc câu đơn giản.
            - Nếu trình độ là B1 hoặc B2: Sử dụng các thì quá khứ (Indefinido, Imperfecto), tương lai, câu giả định cơ bản (Subjuntivo), các liên từ và thành ngữ phổ biến.
            - Nếu trình độ là C1 hoặc C2: Sử dụng ngữ pháp nâng cao, các cấu trúc giả định phức tạp, từ vựng học thuật hoặc diễn đạt sắc thái tinh tế trong công việc và xã hội.
        """.trimIndent()
    }
    
    override fun getEvalLevelGuidelines(level: String): String {
        return """
            - Với trình độ A1 hoặc A2: Chấp nhận các cách dịch cơ bản, tập trung vào thì hiện tại và từ vựng thông dụng.
            - Với trình độ B1 hoặc B2: Kiểm tra kĩ việc chia động từ ở các thì quá khứ, cách sử dụng giới từ (por/para), đại từ và cấu trúc giả định cơ bản (Subjuntivo).
            - Với trình độ C1 hoặc C2: Yêu cầu cực kỳ khắt khe về cấu trúc câu tự nhiên, cách sử dụng Subjuntivo nâng cao, thành ngữ và từ vựng học thuật.
        """.trimIndent()
    }
}

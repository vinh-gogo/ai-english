package com.aienglish.feature.aiteacher.data.repository.prompt

object HindiPromptProvider : BaseLanguagePromptProvider() {
    override val languageLabel: String = "tiếng Hindi"
    override val aiName: String = "Guru AI"
    override val targetLanguageName: String = "Hindi"
    override val targetLanguageCode: String = "hi"
    override val certName: String = "HINDI"
    
    override val exampleSource: String = "Xin chào, bạn khỏe không?"
    override val exampleTarget: String = "नमस्ते, आप कैसे हैं?"
    override val exampleTokens: String = """
        { "word": "नमस्ते", "phonetic": "namaste", "partOfSpeech": "xin chào" },
        { "word": "आप", "phonetic": "aap", "partOfSpeech": "bạn (lịch sự)" },
        { "word": "कैसे", "phonetic": "kaise", "partOfSpeech": "thế nào" },
        { "word": "हैं", "phonetic": "hain", "partOfSpeech": "thì/là" }
    """.trimIndent()
    
    override val tokenWordDesc: String = "chữ Hindi (Devanagari script)."
    override val tokenPhoneticDesc: String = "phiên âm Latinh (transliteration) tương ứng."
    override val tokenPartOfSpeechDesc: String = "ý nghĩa hoặc nghĩa $languageLabel tương ứng của từ đó."
    
    override val punctuationExamples: String = "\",\", \".\", \"!\", \"?\", \"।\", \":\", \";\""
    override val mergeExample1: String = "नमस्ते,"
    override val mergeExample2: String = "नमस्ते\" + \",\""
    
    override val evalCommonErrorsEn: String = "postpositions, pronouns, gender/number agreement, verb conjugations"
    override val evalCommonErrorsVi: String = "về cấu trúc câu, hậu giới từ, chia động từ"
    
    override fun getExplainErrorsEn(level: String): String = "postpositions, gender/number, verb conjugations"
    override fun getExplainErrorsVi(level: String): String = "về trật tự từ, cách dùng hậu giới từ, chia động từ"
    
    override fun getLevelGuidelines(level: String): String {
        return """
            - Nếu trình độ là A1 hoặc A2: Sử dụng từ vựng sinh hoạt hàng ngày, thì hiện tại đơn giản, các câu giới thiệu bản thân đơn giản.
            - Nếu trình độ là B1 hoặc B2: Sử dụng cấu trúc câu phức, thì quá khứ, tương lai, cách dùng trợ động từ và liên từ phong phú hơn.
        """.trimIndent()
    }
    
    override fun getEvalLevelGuidelines(level: String): String {
        return """
            - Với trình độ A1 hoặc A2: Chấp nhận các câu dịch ngắn gọn, cấu trúc cơ bản, động từ ở thì hiện tại.
            - Với trình độ B1 hoặc B2: Kiểm tra cách dùng hậu giới từ (postpositions), đại từ, chia động từ theo giống/số và sự tự nhiên của câu.
        """.trimIndent()
    }
}

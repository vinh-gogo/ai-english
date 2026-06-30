package com.aienglish.feature.aiteacher.data.repository.prompt

object FrenchPromptProvider : BaseLanguagePromptProvider() {
    override val languageLabel: String = "tiếng Pháp"
    override val aiName: String = "Professeur AI"
    override val targetLanguageName: String = "French"
    override val targetLanguageCode: String = "fr"
    override val certName: String = "DELF/DALF"
    
    override val exampleSource: String = "Xin chào, bạn khỏe không?"
    override val exampleTarget: String = "Bonjour, comment ça va ?"
    override val exampleTokens: String = """
        { "word": "Bonjour", "phonetic": "/bɔ̃ʒuʁ/", "partOfSpeech": "interjection" },
        { "word": "comment", "phonetic": "/kɔmɑ̃/", "partOfSpeech": "adverbe" },
        { "word": "ça", "phonetic": "/sa/", "partOfSpeech": "pronom" },
        { "word": "va", "phonetic": "/va/", "partOfSpeech": "verbe" }
    """.trimIndent()
    
    override val tokenWordDesc: String = "từ vựng tiếng Pháp."
    override val tokenPhoneticDesc: String = "phiên âm IPA tiếng Pháp chuẩn."
    override val tokenPartOfSpeechDesc: String = "từ loại tiếng Pháp hoặc nghĩa $languageLabel tương ứng."
    
    override val punctuationExamples: String = "\",\", \".\", \"!\", \"?\", \":\", \";\""
    override val mergeExample1: String = "tout,"
    override val mergeExample2: String = "tout\" + \",\""
    
    override val evalCommonErrorsEn: String = "conjugations, gender/number agreement, prepositions, word order"
    override val evalCommonErrorsVi: String = "về chia động từ, giống/số, giới từ, trật tự từ"

    override fun getExplainRequirementsEn(level: String): String = "especially gender/number, verb conjugations, prepositions"
    override fun getExplainRequirementsVi(level: String): String = "đặc biệt lưu ý chia động từ, giống/số, giới từ"
    
    override fun getExplainErrorsEn(level: String): String = "word order, prepositions, tenses"
    override fun getExplainErrorsVi(level: String): String = "về trật tự từ, cách dùng giới từ, chia thì động từ"
    
    override fun getLevelGuidelines(level: String): String {
        return """
            - Nếu trình độ là A1 hoặc A2: Sử dụng từ vựng cơ bản hàng ngày, các thì hiện tại đơn giản (Présent de l'indicatif), câu ngắn, dễ hiểu.
            - Nếu trình độ là B1 hoặc B2: Sử dụng các thì quá khứ (Passé composé, Imparfait), tương lai, thể giả định cơ bản (Subjonctif), các liên từ liên kết và thành ngữ tự nhiên.
            - Nếu trình độ là C1 hoặc C2: Sử dụng ngữ pháp nâng cao phức tạp, thể giả định nâng cao, cấu trúc diễn đạt tinh tế trong văn viết trang trọng, lối nói tự nhiên như người bản xứ.
        """.trimIndent()
    }
    
    override fun getEvalLevelGuidelines(level: String): String {
        return """
            - Với trình độ A1 hoặc A2: Chấp nhận các cách dịch cơ bản, tập trung vào thì hiện tại (Présent) và các lỗi chính tả, chia động từ cơ bản.
            - Với trình độ B1 hoặc B2: Đánh giá kĩ hơn việc sử dụng các thì quá khứ (Passé composé, Imparfait), tương lai, cấu trúc giả định cơ bản (Subjonctif), đại từ liên hệ (qui, que, dont) và giới từ phù hợp.
            - Với trình độ C1 hoặc C2: Yêu cầu rất cao về sự tự nhiên của câu dịch, trật tự từ sắc sảo, cấu trúc ngữ pháp học thuật/trang trọng, sắc thái tinh tế như người bản xứ.
        """.trimIndent()
    }
}

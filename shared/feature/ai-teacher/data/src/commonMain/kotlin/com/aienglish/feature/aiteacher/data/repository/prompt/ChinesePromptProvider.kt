package com.aienglish.feature.aiteacher.data.repository.prompt

object ChinesePromptProvider : BaseLanguagePromptProvider() {
    override val languageLabel: String = "tiếng Trung"
    override val aiName: String = "Laoshi AI"
    override val targetLanguageName: String = "Chinese"
    override val targetLanguageCode: String = "zh"
    override val certName: String = "HSK"
    
    override val exampleSource: String = "Xin chào, bạn khỏe không?"
    override val exampleTarget: String = "你好，你好吗？"
    override val exampleTokens: String = """
        { "word": "你好", "phonetic": "nǐ hǎo", "partOfSpeech": "xin chào" },
        { "word": "你好吗", "phonetic": "nǐ hǎo ma", "partOfSpeech": "bạn khỏe không" }
    """.trimIndent()
    
    override val tokenWordDesc: String = "từ vựng tiếng Trung viết bằng chữ Hán Giản thể (Simplified Chinese)."
    override val tokenPhoneticDesc: String = "phiên âm Pinyin có dấu thanh tương ứng."
    override val tokenPartOfSpeechDesc: String = "ý nghĩa hoặc nghĩa $languageLabel tương ứng của từ/cụm từ đó."
    
    override val punctuationExamples: String = "\"，\", \"。\", \"！\", \"？\", \",\", \".\""
    override val mergeExample1: String = "好，"
    override val mergeExample2: String = "好\" + \"，\""
    
    override val evalCommonErrorsEn: String = "grammar, measure words, adverbs"
    override val evalCommonErrorsVi: String = "về ngữ pháp, lượng từ, cách dùng phó từ"
    
    override fun getExplainErrorsEn(level: String): String = "word order, measure words, grammar structures"
    override fun getExplainErrorsVi(level: String): String = "về trật tự từ, cách dùng lượng từ, cấu trúc HSK $level"
    
    override fun getLevelGuidelines(level: String): String {
        return """
            - Nếu trình độ là HSK 1 hoặc HSK 2: Sử dụng từ vựng sơ cấp đơn giản, các mẫu câu giao tiếp cơ bản hàng ngày, câu ngắn gọn.
            - Nếu trình độ là HSK 3 hoặc HSK 4: Sử dụng từ vựng trung cấp, các cấu trúc câu phức tạp hơn (ví dụ: câu chữ Ba 把, câu chữ Bei 被, câu so sánh 比, các liên từ liên kết).
            - Nếu trình độ là HSK 5 hoặc HSK 6: Sử dụng các từ vựng cao cấp, thành ngữ tiếng Trung (成语), văn viết trang trọng, hoặc các chủ đề phức tạp hơn về văn hóa, xã hội, kinh tế.
        """.trimIndent()
    }
    
    override fun getEvalLevelGuidelines(level: String): String {
        return """
            - Với trình độ HSK 1 hoặc HSK 2: Chấp nhận các cách viết bính âm hoặc chữ Hán cơ bản. Khuyến khích động viên học sinh nhiều hơn.
            - Với trình độ HSK 3 hoặc HSK 4: Đánh giá cách sử dụng phó từ, lượng từ, cấu trúc câu so sánh hoặc câu chữ Ba/Bei.
            - Với trình độ HSK 5 hoặc HSK 6: Yêu cầu cực kỳ khắt khe về dùng từ (sử dụng từ vựng cao cấp, thành ngữ viết chuẩn), cấu trúc câu phức tạp và tính tự nhiên của văn phong tiếng Trung.
        """.trimIndent()
    }
}

package com.aienglish.feature.aiteacher.data.repository.prompt

object KoreanPromptProvider : BaseLanguagePromptProvider() {
    override val languageLabel: String = "tiếng Hàn"
    override val aiName: String = "Seonsaengnim AI"
    override val targetLanguageName: String = "Korean"
    override val targetLanguageCode: String = "ko"
    override val certName: String = "TOPIK"
    
    override val exampleSource: String = "Xin chào, bạn khỏe không?"
    override val exampleTarget: String = "안녕하세요, 잘 지내세요?"
    override val exampleTokens: String = """
        { "word": "안녕하세요", "phonetic": "annyeonghaseyo", "partOfSpeech": "xin chào" },
        { "word": "잘", "phonetic": "jal", "partOfSpeech": "tốt/khỏe" },
        { "word": "지내세요", "phonetic": "jinaeseyo", "partOfSpeech": "sống/trải qua" }
    """.trimIndent()
    
    override val tokenWordDesc: String = "từ vựng bằng chữ Hangul."
    override val tokenPhoneticDesc: String = "phiên âm Romaji tương ứng."
    override val tokenPartOfSpeechDesc: String = "ý nghĩa hoặc nghĩa $languageLabel tương ứng của từ đó."
    
    override val punctuationExamples: String = "\",\", \".\", \"!\", \"?\", \":\", \";\""
    override val mergeExample1: String = "안녕하세요,"
    override val mergeExample2: String = "안녕하세요\" + \",\""
    
    override val evalCommonErrorsEn: String = "particles, honorifics, sentence endings, conjugations"
    override val evalCommonErrorsVi: String = "về trợ từ, kính ngữ, chia đuôi câu"
    
    override fun getExplainRequirementsEn(level: String): String = "especially particles, verb conjugations, honorifics"
    override fun getExplainRequirementsVi(level: String): String = "đặc biệt lưu ý cách dùng trợ từ, chia đuôi động từ/tính từ, kính ngữ"
    
    override fun getExplainErrorsEn(level: String): String = "word order, tenses, particles"
    override fun getExplainErrorsVi(level: String): String = "về trật tự từ, cách dùng tiểu từ, chia thì động từ"
    
    override fun getLevelGuidelines(level: String): String {
        return """
            - Nếu trình độ là TOPIK 1 hoặc TOPIK 2 (Sơ cấp): Sử dụng từ vựng sinh hoạt cơ bản, cấu trúc ngữ pháp sơ cấp (ví dụ: -ㅂ니다/습니다, -아요/어요, các đuôi câu xã giao đơn giản), câu ngắn, dễ hiểu.
            - Nếu trình độ là TOPIK 3 hoặc TOPIK 4 (Trung cấp): Sử dụng từ vựng đa dạng hơn, các ngữ pháp liên kết trung cấp (ví dụ: -면서, -거나, -기 때문에, -ㄴ/un 후에), câu ghép và lối diễn đạt tự nhiên trong cuộc sống hay công việc.
            - Nếu trình độ là TOPIK 5 hoặc TOPIK 6 (Cao cấp): Sử dụng các ngữ pháp và từ vựng cao cấp, cấu trúc gián tiếp phức tạp, thành ngữ bốn chữ (사자성어), văn phong trang trọng (độ chính xác cao như viết báo, luận văn hoặc đàm phán thương mại).
        """.trimIndent()
    }
    
    override fun getEvalLevelGuidelines(level: String): String {
        return """
            - Với trình độ TOPIK 1 hoặc TOPIK 2: Chấp nhận các cách viết sơ cấp, động từ chia đuôi câu cơ bản (-아요/어요 hoặc -ㅂ니다/습니다). Động viên học sinh nhiều hơn.
            - Với trình độ TOPIK 3 hoặc TOPIK 4: Đánh giá kĩ vị trí từ vựng, cách kết hợp trợ từ (이/加, 은/는, 을/를) và đuôi câu trung cấp tự nhiên.
            - Với trình độ TOPIK 5 hoặc TOPIK 6: Yêu cầu cực kỳ khắt khe về từ vựng cao cấp, cấu trúc ngữ pháp học thuật, kính ngữ đúng hoàn cảnh, văn phong trôi chảy tự nhiên như người bản xứ.
        """.trimIndent()
    }
}

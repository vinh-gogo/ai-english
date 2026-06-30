package com.aienglish.feature.aiteacher.data.repository.prompt

object JapanesePromptProvider : BaseLanguagePromptProvider() {
    override val languageLabel: String = "tiếng Nhật"
    override val aiName: String = "Sensei AI"
    override val targetLanguageName: String = "Japanese"
    override val targetLanguageCode: String = "ja"
    override val certName: String = "JLPT"
    
    override val exampleSource: String = "Xin chào, bạn khỏe không?"
    override val exampleTarget: String = "こんにちは、お元気ですか？"
    override val exampleTokens: String = """
        { "word": "こんにちは", "phonetic": "konnichiwa", "partOfSpeech": "konnichiwa" },
        { "word": "お元気", "phonetic": "ogenki", "partOfSpeech": "おげんき" },
        { "word": "desu", "phonetic": "desu", "partOfSpeech": "đes" },
        { "word": "ka", "phonetic": "ka", "partOfSpeech": "ka" }
    """.trimIndent()
    
    override val tokenWordDesc: String = "từ vựng tiếng Nhật viết bằng chữ Kanji/Kana."
    override val tokenPhoneticDesc: String = "phiên âm Romaji tương ứng."
    override val tokenPartOfSpeechDesc: String = "cách đọc viết bằng chữ Kana/Furigana của từ đó hoặc nghĩa $languageLabel tương ứng."
    
    override val punctuationExamples: String = "\"、\", \"。\", \"！\", \"？\", \",\", \".\""
    override val mergeExample1: String = "元気、"
    override val mergeExample2: String = "元気\" + \"、\""
    
    override val evalCommonErrorsEn: String = "particles, verb conjugation, vocabulary"
    override val evalCommonErrorsVi: String = "về trợ từ, chia động từ, từ vựng"
    
    override fun getExplainErrorsEn(level: String): String = "particles, verb conjugation, vocabulary"
    override fun getExplainErrorsVi(level: String): String = "về trợ từ, chia động từ, từ vựng"
    
    override fun getLevelGuidelines(level: String): String {
        return """
            - Nếu trình độ là N5 hoặc N4: Sử dụng từ vựng sơ cấp đơn giản, ngữ pháp cơ bản (ví dụ: ~desu, ~masu, trợ từ cơ bản), câu ngắn gọn, dễ hiểu.
            - Nếu trình độ là N3: Sử dụng ngữ pháp trung cấp (ví dụ: ~koto ga aru, ~nakereba naranai, thể bị động/sai khiến cơ bản), từ vựng giao tiếp tự nhiên và đa dạng hơn.
            - Nếu trình độ là N2 hoặc N1: Sử dụng ngữ pháp thượng cấp/nâng cao, kính ngữ (keigo), khiêm nhường ngữ, từ vựng học thuật, viết báo, hoặc các lối diễn đạt văn chương trang trọng.
        """.trimIndent()
    }
    
    override fun getEvalLevelGuidelines(level: String): String {
        return """
            - Với trình độ N5 hoặc N4: Chấp nhận các cách viết đơn giản, tập trung vào cấu trúc câu cơ bản. Khuyến khích động viên học sinh nhiều hơn.
            - Với trình độ N3: Đánh giá khắt khe hơn về trợ từ, thể động từ tự nhiên, sắc thái câu trung cấp.
            - Với trình độ N2 hoặc N1: Yêu cầu cực kỳ khắt khe về từ vựng nâng cao, chữ Hán (Kanji), kính ngữ, cấu trúc câu tự nhiên như người bản xứ. Chỉ ra lỗi sai sắc thái dù là nhỏ nhất.
        """.trimIndent()
    }
}

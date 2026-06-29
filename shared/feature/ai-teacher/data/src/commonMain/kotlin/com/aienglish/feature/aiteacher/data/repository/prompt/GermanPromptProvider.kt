package com.aienglish.feature.aiteacher.data.repository.prompt

object GermanPromptProvider : BaseLanguagePromptProvider() {
    override val languageLabel: String = "tiếng Đức"
    override val aiName: String = "Lehrer AI"
    override val targetLanguageName: String = "German"
    override val certName: String = "TIẾNG ĐỨC (GOETHE HOẶC TESTDAF)"
    
    override val exampleSource: String = "Chào mừng bạn đến với Berlin."
    override val exampleTarget: String = "Willkommen in Berlin."
    override val exampleTokens: String = """
        { "word": "Willkommen", "phonetic": "[vɪlˈkɔmən]", "partOfSpeech": "interjection" },
        { "word": "in", "phonetic": "[ɪn]", "partOfSpeech": "preposition" },
        { "word": "Berlin", "phonetic": "[bɛʁˈliːn]", "partOfSpeech": "noun" }
    """.trimIndent()
    
    override val tokenWordDesc: String = "từ vựng tiếng Đức."
    override val tokenPhoneticDesc: String = "phiên âm IPA tiếng Đức chuẩn (ví dụ nằm trong dấu ngoặc vuông [])."
    override val tokenPartOfSpeechDesc: String = "từ loại tiếng Đức hoặc nghĩa $languageLabel tương ứng."
    
    override val punctuationExamples: String = "\",\", \".\", \"!\", \"?\", \":\", \";\""
    override val mergeExample1: String = "Hallo,"
    override val mergeExample2: String = "Hallo\" + \",\""
    
    override val evalCommonErrorsEn: String = "verb conjugation, gender/number/case, prepositions, word order"
    override val evalCommonErrorsVi: String = "về chia động từ, giống/số/cách, giới từ, trật tự từ"

    override fun getExplainRequirementsEn(level: String): String = "verb conjugation, gender/number/case, prepositions, verb placement"
    override fun getExplainRequirementsVi(level: String): String = "lưu ý chia động từ, giống/số/cách, giới từ, vị trí động từ"
    
    override fun getExplainErrorsEn(level: String): String = "word order, prepositions, tenses"
    override fun getExplainErrorsVi(level: String): String = "về trật tự từ, cách dùng giới từ, chia thì động từ"
    
    override fun getLevelGuidelines(level: String): String {
        return """
            - Với trình độ Goethe A1 hoặc A2: Sử dụng từ vựng cơ bản hàng ngày, các thì hiện tại đơn giản (Präsens), câu cực kỳ ngắn gọn, dễ hiểu.
            - Với trình độ Goethe B1 hoặc B2 hoặc TestDaF TDN 3 hoặc TDN 4: Sử dụng các thì quá khứ (Perfekt, Präteritum), các cấu trúc liên từ phụ thuộc (weil, dass, wenn), động từ khuyết thiếu (Modalverben), và thành ngữ thông dụng.
            - Với trình độ Goethe C1 hoặc C2 hoặc TestDaF TDN 5: Sử dụng cấu trúc ngữ pháp học thuật phức tạp (ví dụ: Danh từ hóa - Nominalstil, thể thụ động Passiv nâng cao, Konjunktiv I/II), từ vựng phong phú, văn phong trang trọng, diễn đạt tự nhiên như người bản xứ.
        """.trimIndent()
    }
    
    override fun getEvalLevelGuidelines(level: String): String {
        return """
            - Với trình độ Goethe A1 hoặc A2: Chấp nhận các câu dịch đơn giản, từ vựng cơ bản, cấu trúc câu chính phụ đơn giản. Tập trung vào chia động từ đúng ở thì hiện tại.
            - Với trình độ Goethe B1 hoặc B2 hoặc TestDaF TDN 3/4: Kiểm tra kỹ vị trí động từ trong câu phụ (Nebensatz), các thì quá khứ (Perfekt/Präteritum), sử dụng giới từ, giống và cách của danh từ.
            - Với trình độ Goethe C1 hoặc C2 hoặc TestDaF TDN 5: Yêu cầu cực kỳ khắt khe về cấu trúc câu danh từ hóa (Nominalstil), diễn đạt tự nhiên chuẩn bản xứ, từ loại chuyên ngành và sắc thái biểu đạt cao cấp.
        """.trimIndent()
    }
}

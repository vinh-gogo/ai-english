package com.aienglish.feature.vocabulary.ui.handwriting

/**
 * Types of Vietnamese Tone Marks (Thanh điệu).
 */
enum class VietnameseTone {
    NONE,
    ACUTE,   // Sắc (/)
    GRAVE,   // Huyền (\)
    HOOK,    // Hỏi (?)
    TILDE,   // Ngã (~)
    DOT      // Nặng (.)
}

/**
 * Types of Vietnamese Vowel & Consonant Modifiers (Dấu mũ, dấu móc, dấu trăng, gạch đ).
 */
enum class VietnameseModifier {
    NONE,
    CIRCUMFLEX, // Mũ (^) -> a->â, e->ê, o->ô
    BREVE,      // Trăng (˘) -> a->ă
    HORN,       // Móc (’) -> o->ơ, u->ư
    STROKE      // Gạch ngang (-) -> d->đ, D->Đ
}

/**
 * Utility for combining base Latin letters with Vietnamese tone marks and vowel modifiers
 * following standard Vietnamese orthography rules.
 *
 * Supports bidirectional composition:
 * - Base + Modifier (e.g. a + ^ -> â)
 * - Base + Tone (e.g. a + / -> á)
 * - Modified + Tone (e.g. â + / -> ấ)
 * - Toned + Modifier (e.g. á + ^ -> ấ)
 * - Tone replacement (e.g. á + \ -> à)
 *
 * 100% Offline, pure algorithmic, 0 external dependencies.
 */
object VietnameseDiacritics {

    // Mapping: Base letter -> Tone -> Resulting accented char
    private val TONE_MAP: Map<Char, Map<VietnameseTone, Char>> = mapOf(
        'a' to mapOf(VietnameseTone.ACUTE to 'á', VietnameseTone.GRAVE to 'à', VietnameseTone.HOOK to 'ả', VietnameseTone.TILDE to 'ã', VietnameseTone.DOT to 'ạ'),
        'A' to mapOf(VietnameseTone.ACUTE to 'Á', VietnameseTone.GRAVE to 'À', VietnameseTone.HOOK to 'Ả', VietnameseTone.TILDE to 'Ã', VietnameseTone.DOT to 'Ạ'),
        'ă' to mapOf(VietnameseTone.ACUTE to 'ắ', VietnameseTone.GRAVE to 'ằ', VietnameseTone.HOOK to 'ẳ', VietnameseTone.TILDE to 'ẵ', VietnameseTone.DOT to 'ặ'),
        'Ă' to mapOf(VietnameseTone.ACUTE to 'Ắ', VietnameseTone.GRAVE to 'Ằ', VietnameseTone.HOOK to 'Ẳ', VietnameseTone.TILDE to 'Ẵ', VietnameseTone.DOT to 'Ặ'),
        'â' to mapOf(VietnameseTone.ACUTE to 'ấ', VietnameseTone.GRAVE to 'ầ', VietnameseTone.HOOK to 'ẩ', VietnameseTone.TILDE to 'ẫ', VietnameseTone.DOT to 'ậ'),
        'Â' to mapOf(VietnameseTone.ACUTE to 'Ấ', VietnameseTone.GRAVE to 'Ầ', VietnameseTone.HOOK to 'Ẩ', VietnameseTone.TILDE to 'Ẫ', VietnameseTone.DOT to 'Ậ'),

        'e' to mapOf(VietnameseTone.ACUTE to 'é', VietnameseTone.GRAVE to 'è', VietnameseTone.HOOK to 'ẻ', VietnameseTone.TILDE to 'ẽ', VietnameseTone.DOT to 'ẹ'),
        'E' to mapOf(VietnameseTone.ACUTE to 'É', VietnameseTone.GRAVE to 'È', VietnameseTone.HOOK to 'Ẻ', VietnameseTone.TILDE to 'Ẽ', VietnameseTone.DOT to 'Ẹ'),
        'ê' to mapOf(VietnameseTone.ACUTE to 'ế', VietnameseTone.GRAVE to 'ề', VietnameseTone.HOOK to 'ể', VietnameseTone.TILDE to 'ễ', VietnameseTone.DOT to 'ệ'),
        'Ê' to mapOf(VietnameseTone.ACUTE to 'Ế', VietnameseTone.GRAVE to 'Ề', VietnameseTone.HOOK to 'Ể', VietnameseTone.TILDE to 'Ễ', VietnameseTone.DOT to 'Ệ'),

        'i' to mapOf(VietnameseTone.ACUTE to 'í', VietnameseTone.GRAVE to 'ì', VietnameseTone.HOOK to 'ỉ', VietnameseTone.TILDE to 'ĩ', VietnameseTone.DOT to 'ị'),
        'I' to mapOf(VietnameseTone.ACUTE to 'Í', VietnameseTone.GRAVE to 'Ì', VietnameseTone.HOOK to 'Ỉ', VietnameseTone.TILDE to 'Ĩ', VietnameseTone.DOT to 'Ị'),

        'o' to mapOf(VietnameseTone.ACUTE to 'ó', VietnameseTone.GRAVE to 'ò', VietnameseTone.HOOK to 'ỏ', VietnameseTone.TILDE to 'õ', VietnameseTone.DOT to 'ọ'),
        'O' to mapOf(VietnameseTone.ACUTE to 'Ó', VietnameseTone.GRAVE to 'Ò', VietnameseTone.HOOK to 'Ỏ', VietnameseTone.TILDE to 'Õ', VietnameseTone.DOT to 'Ọ'),
        'ô' to mapOf(VietnameseTone.ACUTE to 'ố', VietnameseTone.GRAVE to 'ồ', VietnameseTone.HOOK to 'ổ', VietnameseTone.TILDE to 'ỗ', VietnameseTone.DOT to 'ộ'),
        'Ô' to mapOf(VietnameseTone.ACUTE to 'Ố', VietnameseTone.GRAVE to 'Ồ', VietnameseTone.HOOK to 'Ổ', VietnameseTone.TILDE to 'Ỗ', VietnameseTone.DOT to 'Ộ'),
        'ơ' to mapOf(VietnameseTone.ACUTE to 'ớ', VietnameseTone.GRAVE to 'ờ', VietnameseTone.HOOK to 'ở', VietnameseTone.TILDE to 'ỡ', VietnameseTone.DOT to 'ợ'),
        'Ơ' to mapOf(VietnameseTone.ACUTE to 'Ớ', VietnameseTone.GRAVE to 'Ờ', VietnameseTone.HOOK to 'Ở', VietnameseTone.TILDE to 'Ỡ', VietnameseTone.DOT to 'Ợ'),

        'u' to mapOf(VietnameseTone.ACUTE to 'ú', VietnameseTone.GRAVE to 'ù', VietnameseTone.HOOK to 'ủ', VietnameseTone.TILDE to 'ũ', VietnameseTone.DOT to 'ụ'),
        'U' to mapOf(VietnameseTone.ACUTE to 'Ú', VietnameseTone.GRAVE to 'Ù', VietnameseTone.HOOK to 'Ủ', VietnameseTone.TILDE to 'Ũ', VietnameseTone.DOT to 'Ụ'),
        'ư' to mapOf(VietnameseTone.ACUTE to 'ứ', VietnameseTone.GRAVE to 'ừ', VietnameseTone.HOOK to 'ử', VietnameseTone.TILDE to 'ữ', VietnameseTone.DOT to 'ự'),
        'Ư' to mapOf(VietnameseTone.ACUTE to 'Ứ', VietnameseTone.GRAVE to 'Ừ', VietnameseTone.HOOK to 'Ử', VietnameseTone.TILDE to 'Ữ', VietnameseTone.DOT to 'Ự'),

        'y' to mapOf(VietnameseTone.ACUTE to 'ý', VietnameseTone.GRAVE to 'ỳ', VietnameseTone.HOOK to 'ỷ', VietnameseTone.TILDE to 'ỹ', VietnameseTone.DOT to 'ỵ'),
        'Y' to mapOf(VietnameseTone.ACUTE to 'Ý', VietnameseTone.GRAVE to 'Ỳ', VietnameseTone.HOOK to 'Ỷ', VietnameseTone.TILDE to 'Ỹ', VietnameseTone.DOT to 'Ỵ')
    )

    // Mapping: Base letter -> Modifier -> Resulting modified letter
    private val MODIFIER_MAP: Map<Char, Map<VietnameseModifier, Char>> = mapOf(
        'a' to mapOf(VietnameseModifier.CIRCUMFLEX to 'â', VietnameseModifier.BREVE to 'ă'),
        'A' to mapOf(VietnameseModifier.CIRCUMFLEX to 'Â', VietnameseModifier.BREVE to 'Ă'),
        'e' to mapOf(VietnameseModifier.CIRCUMFLEX to 'ê'),
        'E' to mapOf(VietnameseModifier.CIRCUMFLEX to 'Ê'),
        'o' to mapOf(VietnameseModifier.CIRCUMFLEX to 'ô', VietnameseModifier.HORN to 'ơ'),
        'O' to mapOf(VietnameseModifier.CIRCUMFLEX to 'Ô', VietnameseModifier.HORN to 'Ơ'),
        'u' to mapOf(VietnameseModifier.HORN to 'ư'),
        'U' to mapOf(VietnameseModifier.HORN to 'Ư'),
        'd' to mapOf(VietnameseModifier.STROKE to 'đ'),
        'D' to mapOf(VietnameseModifier.STROKE to 'Đ')
    )

    // Reverse Mapping: Accented letter -> Pair(BaseWithModifier, Tone)
    private val REVERSE_TONE_MAP: Map<Char, Pair<Char, VietnameseTone>> by lazy {
        val map = mutableMapOf<Char, Pair<Char, VietnameseTone>>()
        for ((base, toneMap) in TONE_MAP) {
            for ((tone, accented) in toneMap) {
                map[accented] = Pair(base, tone)
            }
        }
        map
    }

    /**
     * Decomposes any Vietnamese accented letter into its base (with modifier) and existing tone.
     * E.g. 'ấ' -> ('â', ACUTE), 'á' -> ('a', ACUTE), 'â' -> ('â', NONE)
     */
    fun decomposeChar(char: Char): Pair<Char, VietnameseTone> {
        return REVERSE_TONE_MAP[char] ?: Pair(char, VietnameseTone.NONE)
    }

    /**
     * Checks if a character is a Vietnamese vowel or consonant capable of receiving diacritics.
     */
    fun isDiacriticTarget(char: Char): Boolean {
        return TONE_MAP.containsKey(char) ||
                MODIFIER_MAP.containsKey(char) ||
                REVERSE_TONE_MAP.containsKey(char) ||
                char == 'd' || char == 'D' || char == 'đ' || char == 'Đ'
    }

    /**
     * Applies a tone mark to a base letter, replacing any existing tone if present.
     */
    fun applyTone(char: Char, newTone: VietnameseTone): Char {
        val (base, _) = decomposeChar(char)
        return TONE_MAP[base]?.get(newTone) ?: char
    }

    /**
     * Applies a vowel/consonant modifier (hat, horn, breve, stroke),
     * preserving any existing tone if applicable.
     */
    fun applyModifier(char: Char, modifier: VietnameseModifier): Char {
        val (base, currentTone) = decomposeChar(char)
        val rawBase = when (base) {
            'â', 'ă' -> 'a'
            'Â', 'Ă' -> 'A'
            'ê' -> 'e'
            'Ê' -> 'E'
            'ô', 'ơ' -> 'o'
            'Ô', 'Ơ' -> 'O'
            'ư' -> 'u'
            'Ư' -> 'U'
            'đ' -> 'd'
            'Đ' -> 'D'
            else -> base
        }
        val modifiedBase = MODIFIER_MAP[rawBase]?.get(modifier) ?: base
        return if (currentTone != VietnameseTone.NONE) {
            TONE_MAP[modifiedBase]?.get(currentTone) ?: modifiedBase
        } else {
            modifiedBase
        }
    }

    /**
     * Checks if a symbol represents a standalone diacritic gesture:
     * - Tone marks: "́" (/), "̀" (\), "̉" (?), "̃" (~), "̣" (.)
     * - Modifiers: "^", "˘", "’", "-"
     */
    fun isDiacritic(symbol: String): Boolean {
        return when (symbol) {
            "́", "̀", "̉", "̃", "̣", "^", "˘", "’", "-", ".",
            "dấu_sắc", "dấu_huyền", "dấu_hỏi", "dấu_ngã", "dấu_nặng",
            "dấu_mũ", "dấu_trăng", "dấu_móc", "gạch_ngang_đ",
            "dấu_sắc_xuống", "dấu_huyền_lên", "dấu_mũ_2stroke", "gạch_ngang_đ_phải_sang_trái" -> true
            else -> false
        }
    }

    /**
     * Formats candidate symbols into human-friendly Vietnamese diacritic descriptions.
     */
    fun formatDisplaySymbol(symbol: String): String {
        return when (symbol) {
            "́", "dấu_sắc", "dấu_sắc_xuống" -> "́ (sắc)"
            "̀", "dấu_huyền", "dấu_huyền_lên" -> "̀ (huyền)"
            "̉", "dấu_hỏi" -> "̉ (hỏi)"
            "̃", "dấu_ngã" -> "̃ (ngã)"
            "̣", "dấu_nặng" -> "̣ (nặng)"
            "^", "dấu_mũ", "dấu_mũ_2stroke" -> "^ (mũ)"
            "˘", "dấu_trăng" -> "˘ (trăng)"
            "’", "dấu_móc" -> "’ (móc)"
            "-", "gạch_ngang_đ", "gạch_ngang_đ_phải_sang_trái" -> "- (gạch đ)"
            "." -> ". (chấm)"
            else -> symbol
        }
    }

    /**
     * Checks if a character is a Vietnamese vowel.
     */
    fun isVowel(char: Char): Boolean {
        val lower = char.lowercaseChar()
        return lower in "aăâeêioôơuưyáàảãạắằẳẵặấầẩẫậéèẻẽẹếềểễệíìỉĩịóòỏõọốồổỗộớờởỡợúùủũụứừửữựýỳỷỹỵ"
    }

    /**
     * Given an existing text buffer and a recognized diacritic symbol,
     * combines the diacritic with the last compatible vowel/consonant.
     * Returns the updated buffer.
     */
    fun composeDiacritic(buffer: String, diacriticSymbol: String): String {
        if (buffer.isEmpty()) {
            return if (diacriticSymbol == "-" || diacriticSymbol == ".") diacriticSymbol else buffer
        }

        val isStroke = diacriticSymbol == "-" || diacriticSymbol == "gạch_ngang_đ" || diacriticSymbol == "gạch_ngang_đ_phải_sang_trái"
        val lastIdx = if (isStroke) {
            // Find the last 'd' or 'D' in the buffer
            buffer.indices.reversed().firstOrNull { idx ->
                buffer[idx] == 'd' || buffer[idx] == 'D'
            } ?: -1
        } else {
            // Find the last vowel in the buffer
            buffer.indices.reversed().firstOrNull { idx ->
                isVowel(buffer[idx])
            } ?: -1
        }

        if (lastIdx == -1) {
            return if (diacriticSymbol == "-" || diacriticSymbol == ".") buffer + diacriticSymbol else buffer
        }

        val targetChar = buffer[lastIdx]
        var transformedChar = targetChar

        when (diacriticSymbol) {
            "́", "dấu_sắc", "dấu_sắc_xuống" -> transformedChar = applyTone(targetChar, VietnameseTone.ACUTE)
            "̀", "dấu_huyền", "dấu_huyền_lên" -> transformedChar = applyTone(targetChar, VietnameseTone.GRAVE)
            "̉", "dấu_hỏi" -> transformedChar = applyTone(targetChar, VietnameseTone.HOOK)
            "̃", "dấu_ngã" -> transformedChar = applyTone(targetChar, VietnameseTone.TILDE)
            "̣", "dấu_nặng", "." -> transformedChar = applyTone(targetChar, VietnameseTone.DOT)
            "^", "dấu_mũ", "dấu_mũ_2stroke" -> transformedChar = applyModifier(targetChar, VietnameseModifier.CIRCUMFLEX)
            "˘", "dấu_trăng" -> transformedChar = applyModifier(targetChar, VietnameseModifier.BREVE)
            "’", "dấu_móc" -> transformedChar = applyModifier(targetChar, VietnameseModifier.HORN)
            "-", "gạch_ngang_đ", "gạch_ngang_đ_phải_sang_trái" -> transformedChar = applyModifier(targetChar, VietnameseModifier.STROKE)
        }

        return if (transformedChar != targetChar) {
            buffer.substring(0, lastIdx) + transformedChar + buffer.substring(lastIdx + 1)
        } else {
            if (diacriticSymbol == "-" || diacriticSymbol == ".") buffer + diacriticSymbol else buffer
        }
    }
}

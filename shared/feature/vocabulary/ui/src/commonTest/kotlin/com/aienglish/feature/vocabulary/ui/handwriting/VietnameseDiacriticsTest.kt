package com.aienglish.feature.vocabulary.ui.handwriting

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VietnameseDiacriticsTest {

    @Test
    fun applyTone_baseVowels_generatesCorrectAccentedVowels() {
        assertEquals('á', VietnameseDiacritics.applyTone('a', VietnameseTone.ACUTE))
        assertEquals('à', VietnameseDiacritics.applyTone('a', VietnameseTone.GRAVE))
        assertEquals('ả', VietnameseDiacritics.applyTone('a', VietnameseTone.HOOK))
        assertEquals('ã', VietnameseDiacritics.applyTone('a', VietnameseTone.TILDE))
        assertEquals('ạ', VietnameseDiacritics.applyTone('a', VietnameseTone.DOT))

        assertEquals('é', VietnameseDiacritics.applyTone('e', VietnameseTone.ACUTE))
        assertEquals('ó', VietnameseDiacritics.applyTone('o', VietnameseTone.ACUTE))
        assertEquals('ú', VietnameseDiacritics.applyTone('u', VietnameseTone.ACUTE))
        assertEquals('í', VietnameseDiacritics.applyTone('i', VietnameseTone.ACUTE))
        assertEquals('ý', VietnameseDiacritics.applyTone('y', VietnameseTone.ACUTE))
    }

    @Test
    fun applyModifier_convertsBaseVowelsToModifiedVowels() {
        assertEquals('â', VietnameseDiacritics.applyModifier('a', VietnameseModifier.CIRCUMFLEX))
        assertEquals('ă', VietnameseDiacritics.applyModifier('a', VietnameseModifier.BREVE))
        assertEquals('ê', VietnameseDiacritics.applyModifier('e', VietnameseModifier.CIRCUMFLEX))
        assertEquals('ô', VietnameseDiacritics.applyModifier('o', VietnameseModifier.CIRCUMFLEX))
        assertEquals('ơ', VietnameseDiacritics.applyModifier('o', VietnameseModifier.HORN))
        assertEquals('ư', VietnameseDiacritics.applyModifier('u', VietnameseModifier.HORN))
        assertEquals('đ', VietnameseDiacritics.applyModifier('d', VietnameseModifier.STROKE))
        assertEquals('Đ', VietnameseDiacritics.applyModifier('D', VietnameseModifier.STROKE))
    }

    @Test
    fun applyTone_modifiedVowels_addsToneMarks() {
        assertEquals('ấ', VietnameseDiacritics.applyTone('â', VietnameseTone.ACUTE))
        assertEquals('ầ', VietnameseDiacritics.applyTone('â', VietnameseTone.GRAVE))
        assertEquals('ẩ', VietnameseDiacritics.applyTone('â', VietnameseTone.HOOK))
        assertEquals('ẫ', VietnameseDiacritics.applyTone('â', VietnameseTone.TILDE))
        assertEquals('ậ', VietnameseDiacritics.applyTone('â', VietnameseTone.DOT))

        assertEquals('ắ', VietnameseDiacritics.applyTone('ă', VietnameseTone.ACUTE))
        assertEquals('ế', VietnameseDiacritics.applyTone('ê', VietnameseTone.ACUTE))
        assertEquals('ố', VietnameseDiacritics.applyTone('ô', VietnameseTone.ACUTE))
        assertEquals('ớ', VietnameseDiacritics.applyTone('ơ', VietnameseTone.ACUTE))
        assertEquals('ứ', VietnameseDiacritics.applyTone('ư', VietnameseTone.ACUTE))
    }

    @Test
    fun applyModifier_tonedVowels_preservesExistingTone() {
        // Drawing tone first, then drawing modifier (e.g. á + circumflex -> ấ)
        assertEquals('ấ', VietnameseDiacritics.applyModifier('á', VietnameseModifier.CIRCUMFLEX))
        assertEquals('ắ', VietnameseDiacritics.applyModifier('á', VietnameseModifier.BREVE))
        assertEquals('ế', VietnameseDiacritics.applyModifier('é', VietnameseModifier.CIRCUMFLEX))
        assertEquals('ố', VietnameseDiacritics.applyModifier('ó', VietnameseModifier.CIRCUMFLEX))
        assertEquals('ớ', VietnameseDiacritics.applyModifier('ó', VietnameseModifier.HORN))
        assertEquals('ứ', VietnameseDiacritics.applyModifier('ú', VietnameseModifier.HORN))
    }

    @Test
    fun applyTone_replacesExistingToneGracefully() {
        // e.g. user wrote á but meant à
        assertEquals('à', VietnameseDiacritics.applyTone('á', VietnameseTone.GRAVE))
        assertEquals('ạ', VietnameseDiacritics.applyTone('á', VietnameseTone.DOT))
        assertEquals('ầ', VietnameseDiacritics.applyTone('ấ', VietnameseTone.GRAVE))
        assertEquals('ậ', VietnameseDiacritics.applyTone('ấ', VietnameseTone.DOT))
    }

    @Test
    fun composeDiacritic_composesOntoLastVowelInBuffer() {
        // Acute mark
        assertEquals("má", VietnameseDiacritics.composeDiacritic("ma", "́"))
        assertEquals("má", VietnameseDiacritics.composeDiacritic("ma", "dấu_sắc"))

        // Grave mark
        assertEquals("mà", VietnameseDiacritics.composeDiacritic("ma", "̀"))

        // Underdot (nặng)
        assertEquals("mạ", VietnameseDiacritics.composeDiacritic("ma", "̣"))
        assertEquals("học", VietnameseDiacritics.composeDiacritic("hoc", "."))

        // Circumflex on 'a' -> 'â', then acute -> 'ấ'
        val step1 = VietnameseDiacritics.composeDiacritic("da", "^")
        assertEquals("dâ", step1)
        val step2 = VietnameseDiacritics.composeDiacritic(step1, "́")
        assertEquals("dấ", step2)

        // Stroke '-' transforms 'd' to 'đ'
        assertEquals("đ", VietnameseDiacritics.composeDiacritic("d", "-"))
        assertEquals("đi", VietnameseDiacritics.composeDiacritic("di", "-"))

        // Multi-vowel words: tone applies to the last active vowel
        assertEquals("hoá", VietnameseDiacritics.composeDiacritic("hoa", "́"))
        assertEquals("thuyến", VietnameseDiacritics.composeDiacritic("thuyên", "́"))
    }

    @Test
    fun isDiacritic_identifiesAllDiacriticSymbols() {
        assertTrue(VietnameseDiacritics.isDiacritic("́"))
        assertTrue(VietnameseDiacritics.isDiacritic("̀"))
        assertTrue(VietnameseDiacritics.isDiacritic("̉"))
        assertTrue(VietnameseDiacritics.isDiacritic("̃"))
        assertTrue(VietnameseDiacritics.isDiacritic("̣"))
        assertTrue(VietnameseDiacritics.isDiacritic("^"))
        assertTrue(VietnameseDiacritics.isDiacritic("˘"))
        assertTrue(VietnameseDiacritics.isDiacritic("’"))
        assertTrue(VietnameseDiacritics.isDiacritic("-"))
        assertTrue(VietnameseDiacritics.isDiacritic("dấu_sắc"))
    }
}

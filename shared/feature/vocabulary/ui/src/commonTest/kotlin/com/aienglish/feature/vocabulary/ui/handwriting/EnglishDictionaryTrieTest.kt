package com.aienglish.feature.vocabulary.ui.handwriting

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EnglishDictionaryTrieTest {

    @Test
    fun searchPrefix_returnsMatchingWords() {
        val trie = EnglishDictionaryTrie.DEFAULT
        val results = trie.searchPrefix("inte", limit = 5)

        assertTrue(results.isNotEmpty())
        assertTrue(results.any { it.contains("integrate") || it == "integration" })
    }

    @Test
    fun searchPrefix_isCaseInsensitive() {
        val trie = EnglishDictionaryTrie.DEFAULT
        val lowerResults = trie.searchPrefix("alg", limit = 3)
        val upperResults = trie.searchPrefix("ALG", limit = 3)

        assertEquals(lowerResults, upperResults)
        assertTrue(lowerResults.contains("algorithm"))
    }

    @Test
    fun insert_addsCustomWords() {
        val trie = EnglishDictionaryTrie()
        trie.insert("KotlinMultiplatform")
        trie.insert("ComposeDesktop")

        val results = trie.searchPrefix("kotlin", limit = 5)
        assertEquals(listOf("kotlinmultiplatform"), results)
    }

    @Test
    fun searchPrefix_returnsEmptyForNonExistentPrefix() {
        val trie = EnglishDictionaryTrie()
        val results = trie.searchPrefix("xyzqqw", limit = 5)
        assertTrue(results.isEmpty())
    }
}

package com.aienglish.feature.vocabulary.ui.handwriting

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VietnameseDictionaryTrieTest {

    @Test
    fun searchPrefix_returnsMatchingVietnameseWords() {
        val trie = VietnameseDictionaryTrie.DEFAULT
        val results = trie.searchPrefix("tích", limit = 5)

        assertTrue(results.isNotEmpty())
        assertTrue(results.contains("tích hợp"))
    }

    @Test
    fun searchPrefix_isCaseInsensitive() {
        val trie = VietnameseDictionaryTrie.DEFAULT
        val lowerResults = trie.searchPrefix("thuật", limit = 3)
        val upperResults = trie.searchPrefix("THUẬT", limit = 3)

        assertEquals(lowerResults, upperResults)
        assertTrue(lowerResults.contains("thuật toán"))
    }

    @Test
    fun searchPrefix_findsAccentedWords() {
        val trie = VietnameseDictionaryTrie.DEFAULT
        val results = trie.searchPrefix("nghiên", limit = 5)

        assertTrue(results.isNotEmpty())
        assertTrue(results.contains("nghiên cứu"))
    }

    @Test
    fun insert_customVietnamesePhrases() {
        val trie = VietnameseDictionaryTrie()
        trie.insert("Lập trình đa nền tảng")
        trie.insert("Giao diện người dùng")

        val results = trie.searchPrefix("lập", limit = 5)
        assertEquals(listOf("lập trình đa nền tảng"), results)
    }

    @Test
    fun searchPrefix_returnsEmptyForNonExistentPrefix() {
        val trie = VietnameseDictionaryTrie()
        val results = trie.searchPrefix("xyzqqw", limit = 5)
        assertTrue(results.isEmpty())
    }
}

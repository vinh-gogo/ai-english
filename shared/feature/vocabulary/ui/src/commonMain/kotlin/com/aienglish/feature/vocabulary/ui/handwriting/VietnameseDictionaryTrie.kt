package com.aienglish.feature.vocabulary.ui.handwriting

/**
 * Offline prefix Trie for fast Vietnamese vocabulary and definition auto-completion.
 * Zero token cost, zero network latency, instant (<0.1ms) lookups.
 */
class VietnameseDictionaryTrie(
    initialWords: Collection<String> = emptyList()
) {
    private val root = TrieNode()

    init {
        BUILT_IN_VIETNAMESE_WORDS.forEach { insert(it) }
        initialWords.forEach { insert(it) }
    }

    private class TrieNode {
        val children = mutableMapOf<Char, TrieNode>()
        var isWord = false
        var word: String? = null
    }

    fun insert(word: String) {
        val clean = word.trim().lowercase()
        if (clean.isBlank()) return

        var current = root
        for (char in clean) {
            current = current.children.getOrPut(char) { TrieNode() }
        }
        current.isWord = true
        current.word = clean
    }

    fun insertAll(words: Collection<String>) {
        words.forEach { insert(it) }
    }

    fun searchPrefix(prefix: String, limit: Int = 10): List<String> {
        val cleanPrefix = prefix.trim().lowercase()
        if (cleanPrefix.isBlank()) return emptyList()

        var current = root
        for (char in cleanPrefix) {
            val next = current.children[char] ?: return emptyList()
            current = next
        }

        val results = mutableListOf<String>()
        collectWords(current, results, limit)
        return results.sortedWith(compareBy({ it.length }, { it }))
    }

    private fun collectWords(node: TrieNode, results: MutableList<String>, limit: Int) {
        if (results.size >= limit) return
        if (node.isWord) {
            node.word?.let { results.add(it) }
        }
        for (child in node.children.values) {
            collectWords(child, results, limit)
            if (results.size >= limit) return
        }
    }

    companion object {
        val DEFAULT: VietnameseDictionaryTrie by lazy {
            VietnameseDictionaryTrie()
        }

        /**
         * Curated list of high-frequency Vietnamese vocabulary terms,
         * collocations, and common definitions for English learning.
         */
        val BUILT_IN_VIETNAMESE_WORDS: List<String> = listOf(
            // --- Tech, Engineering & IT ---
            "tích hợp", "hợp nhất", "sự tích hợp", "hệ thống", "phần mềm", "phần cứng",
            "cơ sở dữ liệu", "thuật toán", "cấu trúc dữ liệu", "kiến trúc", "thành phần",
            "giao diện", "người dùng", "trải nghiệm", "phát triển", "triển khai", "kiểm thử",
            "tự động hóa", "mạng máy tính", "bảo mật", "mã hóa", "giải mã", "an toàn thông tin",
            "đám mây", "máy chủ", "máy khách", "kết nối", "đường truyền", "băng thông",
            "hiệu năng", "tối ưu hóa", "khả năng mở rộng", "tài nguyên", "bộ nhớ", "dung lượng",
            "lưu trữ", "đồng bộ", "bất đồng bộ", "luồng", "đa luồng", "hàng đợi", "ngăn xếp",
            "tham số", "đối số", "biến số", "hàm số", "phương thức", "lớp", "đối tượng",
            "kế thừa", "đa hình", "đóng gói", "trừu tượng", "mô đun", "gói phần mềm",
            "thư viện", "khung phát triển", "nền tảng", "vòng đời", "trạng thái", "sự kiện",
            "xử lý lỗi", "ngoại lệ", "gỡ lỗi", "phiên bản", "nhánh", "hợp nhất mã",

            // --- Business, Finance & Management ---
            "thu nhận", "mua lại", "tiếp quản", "sáp nhập", "doanh thu", "lợi nhuận",
            "chi phí", "ngân sách", "đầu tư", "nhà đầu tư", "vốn", "tài sản", "tiêu sản",
            "nợ", "thanh khoản", "cổ phần", "cổ tức", "thị phần", "chiến lược", "kế hoạch",
            "mục tiêu", "quản lý", "quản trị", "lãnh đạo", "điều hành", "nhân sự", "tuyển dụng",
            "đào tạo", "khách hàng", "đối tác", "hợp đồng", "thỏa thuận", "đàm phán",
            "tiếp thị", "quảng cáo", "thương hiệu", "sản phẩm", "dịch vụ", "kinh doanh",
            "doanh nghiệp", "công ty", "tập đoàn", "chuỗi cung ứng", "vận hành", "năng suất",
            "chất lượng", "tiêu chuẩn", "đánh giá", "rủi ro", "kiểm toán", "báo cáo",

            // --- Academic, Research & Analysis ---
            "nhận thức", "thiên vị", "định kiến", "giả thuyết", "thực nghiệm", "phương pháp",
            "nghiên cứu", "phân tích", "tổng hợp", "đánh giá", "kết luận", "chứng minh",
            "dữ liệu", "thông tin", "tri thức", "suy luận", "suy diễn", "quy nạp",
            "tương quan", "nhân quả", "định lượng", "định tính", "mô hình", "tiêu chí",
            "cơ sở", "nguyên tắc", "nguyên lý", "khái niệm", "định nghĩa", "phạm trù",
            "quan điểm", "góc nhìn", "bối cảnh", "phạm vi", "hạn chế", "đóng góp",
            "ý nghĩa", "tác động", "ảnh hưởng", "xu hướng", "quy luật", "hiện tượng",

            // --- Common Actions, Emotions & Daily Life ---
            "học tập", "rèn luyện", "thực hành", "giao tiếp", "lắng nghe", "chia sẻ",
            "thấu hiểu", "đồng cảm", "sáng tạo", "đổi mới", "kiên trì", "nỗ lực",
            "thành công", "tiến bộ", "cải thiện", "nâng cao", "hoàn thiện", "vượt qua",
            "thách thức", "cơ hội", "trách nhiệm", "nhiệm vụ", "vai trò", "giá trị",
            "bản thân", "gia đình", "bạn bè", "xã hội", "cộng đồng", "môi trường",
            "tự nhiên", "cuộc sống", "sức khỏe", "tinh thần", "hạnh phúc", "bình an",
            "tự tin", "vui vẻ", "hào hứng", "đam mê", "nhiệt huyết", "tích cực",

            // --- Common Grammar & Parts of Speech Labels ---
            "danh từ", "động từ", "tính từ", "trạng từ", "giới từ", "liên từ", "đại từ",
            "cụm từ", "thành ngữ", "ngữ pháp", "ngữ nghĩa", "ngữ cảnh", "câu ví dụ",
            "từ đồng nghĩa", "từ trái nghĩa", "phát âm", "phiên âm", "trọng âm"
        )
    }
}

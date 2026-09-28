package com.aienglish.feature.vocabulary.ui.handwriting

/**
 * Offline prefix Trie for fast English vocabulary auto-completion.
 * Zero token cost, zero network latency, instant (<0.1ms) lookups.
 */
class EnglishDictionaryTrie(
    initialWords: Collection<String> = emptyList()
) {
    private val root = TrieNode()

    init {
        // Preload core common English words + custom initial words
        BUILT_IN_WORDS.forEach { insert(it) }
        initialWords.forEach { insert(it) }
    }

    private class TrieNode {
        val children = mutableMapOf<Char, TrieNode>()
        var isWord = false
        var word: String? = null
    }

    /**
     * Inserts a word into the Trie.
     */
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

    /**
     * Inserts multiple words into the Trie.
     */
    fun insertAll(words: Collection<String>) {
        words.forEach { insert(it) }
    }

    /**
     * Searches for words matching the given prefix.
     *
     * @param prefix Prefix to search for (e.g., "int").
     * @param limit Maximum number of suggestions to return.
     * @return List of matching words sorted by length then alphabetically.
     */
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
        val DEFAULT: EnglishDictionaryTrie by lazy {
            EnglishDictionaryTrie()
        }

        /**
         * Curated list of high-frequency English vocabulary covering:
         * Tech, Business, Academic, IELTS, and Common Communication.
         */
        val BUILT_IN_WORDS: List<String> = listOf(
            // --- Tech & Engineering ---
            "algorithm", "architecture", "asynchronous", "authentication", "authorization",
            "backend", "benchmark", "binary", "blockchain", "bandwidth", "cache", "capacity",
            "cluster", "codebase", "compiler", "component", "concurrency", "configuration",
            "container", "database", "debugging", "dependency", "deployment", "design",
            "distributed", "domain", "efficiency", "encryption", "endpoint", "entity",
            "exception", "framework", "frontend", "function", "gateway", "hardware",
            "identifier", "immutable", "infrastructure", "inheritance", "injection",
            "instance", "integration", "interface", "iteration", "library", "lifecycle",
            "logging", "memory", "metadata", "microservice", "migration", "module",
            "multithreading", "mutation", "namespace", "network", "object", "optimization",
            "orchestration", "overflow", "package", "parameter", "parser", "pattern",
            "payload", "performance", "pipeline", "platform", "pointer", "polymorphism",
            "protocol", "query", "queue", "reactive", "refactoring", "repository",
            "resilience", "resolution", "runtime", "scalability", "schema", "security",
            "serialization", "serverless", "session", "singleton", "socket", "software",
            "stack", "storage", "synchronous", "syntax", "system", "template",
            "terminal", "throughput", "timeout", "token", "transaction", "trigger",
            "validation", "variable", "virtual", "vulnerability", "websocket", "workflow",

            // --- Business & Management ---
            "acquisition", "agreement", "allocation", "amortization", "analysis", "appraisal",
            "arbitration", "assessment", "asset", "audit", "balance", "bankruptcy",
            "benchmark", "beneficiary", "budget", "campaign", "capital", "collateral",
            "commission", "commodity", "compliance", "conglomerate", "contract", "corporate",
            "creditor", "currency", "debt", "deficit", "deliverable", "depreciation",
            "dividend", "downsizing", "earnings", "ecommerce", "efficiency", "enterprise",
            "entrepreneur", "equity", "evaluation", "executive", "expenditure", "feasibility",
            "feedback", "financial", "fiscal", "forecast", "framework", "franchise",
            "governance", "headquarters", "incentive", "incorporation", "indemnity",
            "inflation", "infrastructure", "initiative", "innovation", "insolvency",
            "insurance", "inventory", "investment", "investor", "invoice", "joint",
            "leadership", "lease", "leverage", "liability", "liquidity", "logistics",
            "management", "margin", "marketing", "merger", "milestone", "monopoly",
            "negotiation", "objective", "offshore", "operations", "outsourcing", "overhead",
            "partnership", "patent", "performance", "portfolio", "procurement", "productivity",
            "profit", "projection", "proprietorship", "prospectus", "quarter", "quota",
            "recession", "recruitment", "redundancy", "remuneration", "restructuring",
            "retail", "revenue", "roadmap", "royalty", "shareholder", "stakeholder",
            "strategy", "subsidiary", "surplus", "synergy", "tactics", "tariff",
            "taxation", "transaction", "turnover", "valuation", "venture", "viability",

            // --- Academic, IELTS & Scientific ---
            "abstract", "accurate", "acknowledge", "acquire", "adaptation", "adequate",
            "adjacent", "advocate", "aggregate", "ambiguous", "analogy", "anticipate",
            "apparent", "appendix", "appreciate", "arbitrary", "aspect", "assemble",
            "assess", "assign", "assume", "assure", "attain", "attitude",
            "attribute", "authoritative", "automate", "available", "awareness", "beneficial",
            "bias", "capable", "category", "channel", "circumstance", "clarify",
            "coherent", "coincide", "collapse", "colleague", "commence", "commission",
            "commit", "commodity", "compatible", "compensate", "compile", "complement",
            "complex", "component", "compound", "comprehensive", "comprise", "compute",
            "conceive", "concentrate", "concept", "conclude", "concurrent", "conduct",
            "conform", "consent", "consequence", "considerable", "consist", "constant",
            "constitute", "constrain", "construct", "consult", "consume", "contact",
            "contemporary", "context", "contract", "contradict", "contrary", "contrast",
            "contribute", "controversy", "convene", "converse", "convert", "convince",
            "cooperate", "coordinate", "core", "corporate", "correspond", "criteria",
            "crucial", "currency", "cycle", "data", "debate", "decade",
            "decline", "deduce", "define", "definite", "demonstrate", "denote",
            "deny", "depress", "derive", "design", "despite", "detect",
            "deviate", "device", "devote", "differentiate", "dimension", "diminish",
            "discrete", "discriminate", "displace", "display", "dispose", "distinct",
            "distort", "distribute", "diverse", "document", "domain", "domestic",
            "dominate", "draft", "drama", "duration", "dynamic", "economy",
            "element", "eliminate", "emerge", "emphasis", "empirical", "enable",
            "encounter", "energy", "enforce", "enhance", "enormous", "ensure",
            "entity", "environment", "equate", "equip", "equivalent", "erode",
            "error", "establish", "estate", "estimate", "ethic", "ethnic",
            "evaluate", "eventual", "evident", "evolve", "exceed", "exclude",
            "exhibit", "expand", "expert", "explicit", "exploit", "export",
            "expose", "external", "extract", "facilitate", "factor", "feature",
            "federal", "fee", "file", "final", "finance", "finite",
            "flexible", "fluctuate", "focus", "format", "formula", "forthcoming",
            "foundation", "framework", "function", "fund", "fundamental", "furthermore",
            "generate", "generation", "globe", "goal", "grade", "grant",
            "guarantee", "guideline", "hence", "hierarchy", "highlight", "hypothesis",
            "identical", "identify", "ideology", "ignorant", "illustrate", "image",
            "immigrate", "impact", "implement", "implicate", "implicit", "imply",
            "impose", "incentive", "incidence", "incline", "income", "incorporate",
            "index", "indicate", "individual", "induce", "inevitable", "infer",
            "infrastructure", "inherent", "inhibit", "initial", "initiative", "injure",
            "innovate", "input", "insert", "insight", "inspect", "instance",
            "institute", "instruct", "integral", "integrate", "integrity", "intelligence",
            "intense", "interact", "intermediate", "internal", "interpret", "interval",
            "intervene", "intrinsic", "invest", "investigate", "invoke", "involve",
            "isolate", "issue", "item", "job", "journal", "justify",
            "label", "labor", "layer", "lecture", "legal", "legislate",
            "levy", "liberal", "license", "likewise", "link", "locate",
            "logic", "maintain", "major", "manipulate", "manual", "margin",
            "mature", "maximize", "mechanism", "media", "mediate", "medical",
            "medium", "mental", "method", "migrate", "military", "minimal",
            "minimize", "minimum", "ministry", "minor", "mode", "modify",
            "monitor", "motive", "mutual", "negate", "network", "neutral",
            "nevertheless", "nonetheless", "norm", "normal", "notion", "nuclear",
            "objective", "obtain", "obvious", "occupy", "occur", "odd",
            "offset", "ongoing", "option", "orient", "outcome", "output",
            "overall", "overlap", "overseas", "panel", "paradigm", "paragraph",
            "parallel", "parameter", "participate", "partner", "passive", "perceive",
            "percent", "period", "persist", "perspective", "phase", "phenomenon",
            "philosophy", "physical", "plus", "policy", "portion", "pose",
            "positive", "potential", "practitioner", "precede", "precise", "predict",
            "predominant", "preliminary", "presume", "previous", "primary", "prime",
            "principal", "principle", "prior", "priority", "proceed", "process",
            "professional", "prohibit", "project", "promote", "proportion", "prospect",
            "protocol", "psychology", "publication", "publish", "purchase", "pursue",
            "qualitative", "quote", "radical", "random", "range", "ratio",
            "rational", "react", "recover", "refine", "regime", "region",
            "register", "regulate", "reinforce", "reject", "relax", "release",
            "relevant", "reluctance", "rely", "remove", "require", "research",
            "reside", "resolve", "resource", "respond", "restore", "restrain",
            "restrict", "retain", "reveal", "revenue", "reverse", "revise",
            "revolution", "rigid", "role", "route", "scenario", "schedule",
            "scheme", "scope", "section", "sector", "secure", "seek",
            "select", "sequence", "series", "shift", "significant", "similar",
            "simulate", "site", "so-called", "sole", "somewhat", "source",
            "specific", "specify", "sphere", "stable", "statistic", "status",
            "straightforward", "strategy", "stress", "structure", "style", "submit",
            "subordinate", "subsequent", "subsidy", "substitute", "successor", "sufficient",
            "sum", "summary", "supplement", "survey", "survive", "suspend",
            "sustain", "symbol", "tape", "target", "task", "team",
            "technical", "technique", "technology", "temporary", "tense", "terminate",
            "text", "theme", "theory", "thereby", "thesis", "topic",
            "trace", "tradition", "transfer", "transform", "transit", "transmit",
            "transport", "trend", "trigger", "ultimate", "undergo", "underlie",
            "undertake", "uniform", "unify", "unique", "utilize", "valid",
            "vary", "vehicle", "version", "via", "violate", "virtual",
            "visible", "vision", "visual", "volume", "voluntary", "welfare",
            "whereas", "whereby", "widespread"
        )
    }
}

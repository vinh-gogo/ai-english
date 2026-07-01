val review = """[TOPIC_RECOMMEND] topic 1 [/TOPIC_RECOMMEND]
[TOPIC_RECOMMEND] topic 2 [/TOPIC_RECOMMEND]
[TOPIC_RECOMMEND] topic 3 [/TOPIC_RECOMMEND]"""

val topicRegex = Regex("\\[TOPIC_RECOMMEND\\](.*?)\\[/TOPIC_RECOMMEND\\]")
val recommendedTopics = topicRegex.findAll(review).map { it.groupValues[1].trim() }.toList()

println(recommendedTopics)

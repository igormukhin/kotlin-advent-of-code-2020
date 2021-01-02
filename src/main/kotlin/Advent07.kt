
fun main() {
    val lines = Utils.readInput("Advent07").lines()

    val rules = lines.map { Rule.parse(it) }

    // Part 1
    println(rules.map { it.bag }.count { canContain(it, "shiny gold", rules, setOf()) })

    // Part 2
    println(countContentsOf("shiny gold", rules))
}

private fun countContentsOf(bag: String, rules: List<Rule>): Int {
    val rule = rules.find { it.bag == bag } !!
    var n = 0
    rule.contents.forEach { (packet, cnt) ->
        n += cnt * (1 + countContentsOf(packet, rules))
    }
    return n
}

private fun canContain(bag: String, target: String, rules: List<Rule>, ignoreBags: Set<String>) : Boolean {
    val rule = rules.find { it.bag == bag } !!

    val ignoreBagsAndMe = mutableSetOf(bag)
    ignoreBagsAndMe.addAll(ignoreBags)

    rule.contents.keys.forEach { content ->
        if (content == target) {
            return true
        }
        if (!ignoreBags.contains(content)) {
            if (canContain(content, target, rules, ignoreBags)) {
                return true
            }
        }
    }

    return false
}

private data class Rule(val bag: String, val contents: Map<String, Int>) {
    companion object {
        fun parse(line: String) : Rule {
            val bag = line.substringBefore(" bags contain ")
            val contents = mutableMapOf<String, Int>()
            if (!line.endsWith(" no other bags.")) {
                line.substringAfter(" bags contain ").split(", ").forEach { term ->
                    val (w1, w2, w3, _) = term.split(" ")
                    contents["$w2 $w3"] = w1.toInt()
                }
            }
            return Rule(bag, contents)
        }
    }
}
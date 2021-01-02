
fun main() {
    val lines = Utils.readInput("Advent19").lines()

    val rules = mutableMapOf<Int, String>()
    val messages = mutableListOf<String>()
    var state = 0
    lines.forEach { line ->
        when {
            line.isEmpty() -> state = 1
            state == 0 -> rules[line.substringBefore(":").toInt()] = line.substringAfter(": ")
            state == 1 -> messages.add(line)
        }
    }

    fun matchesRule(message: String, rule: String): Boolean {
        val ends = matchesRuleEx(rules, message, rule, 0)
        return ends.contains(message.length)
    }

    // Part 1
    println(messages.count { matchesRule(it, rules[0]!!) })

    // Part 2
    fun formatRule8() : String {
        return (1..(messages.map { it.length }.maxOrNull()!!)).
            joinToString(separator = " | ") { n -> Array(n) { "42" }.joinToString(separator = " ")
        }
    }

    fun formatRule11() : String {
        return (1..(messages.map { it.length / 2 + 1 }.maxOrNull()!!))
            .joinToString(separator = " | ") { n -> Array(n) { "42" }.joinToString(separator = " ") +
                    " " +
                    Array(n) { "31" }.joinToString(separator = " ")
        }
    }

    val newRules = mutableMapOf<Int, String>()
    rules.forEach { kv ->
        when (kv.key) {
            8 -> newRules[8] = formatRule8() // "42 | 42 8"
            11 -> newRules[11] = formatRule11() // "42 31 | 42 11 31"
            else -> newRules[kv.key] = kv.value
        }
    }
    rules.clear()
    rules.putAll(newRules)

    println(messages.count { matchesRule(it, rules[0]!!) })
}

fun matchesSeqRule(rulesRegistry: Map<Int, String>, message: String, startAt: Int,
                   parts: List<String>, partIndex: Int): List<Int> {
    val ends = matchesRuleEx(rulesRegistry, message, rulesRegistry[parts[partIndex].toInt()]!!, startAt)
    if (partIndex == parts.size - 1) {
        return ends
    }

    return ends.map { end -> matchesSeqRule(rulesRegistry, message, end, parts, partIndex + 1) }
        .flatten().distinct()
}

fun matchesRuleEx(rulesRegistry: Map<Int, String>, message: String, rule: String, startAt: Int): List<Int> {
    return if (startAt >= message.length) {
        emptyList()
    } else if (rule.contains("\"")) {
        if (message[startAt] == rule[1]) {
            listOf(startAt + 1)
        } else {
            emptyList()
        }
    } else if (rule.contains("|")) {
        val ors = rule.split(" | ")
        ors.map { matchesRuleEx(rulesRegistry, message, it, startAt) }.flatten().distinct()
    } else {
        val parts = rule.split(" ")
        matchesSeqRule(rulesRegistry, message, startAt, parts, 0)
    }
}

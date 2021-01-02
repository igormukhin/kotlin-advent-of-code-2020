
fun main() {
    val lines = Utils.readInput("Advent18").lines()

    println(lines.map { evaluate1(tokenize(it)) }.sum())

    println(lines.map { evaluate2(tokenize(it)) }.sum())
}

private fun tokenize(expr: String): List<Char> {
    val tokens = mutableListOf<Char>()
    expr.forEach { ch ->
        if (ch != ' ') {
            tokens.add(ch)
        }
    }
    return tokens
}

private fun evaluate1(expr: List<Char>): Long {
    return evaluate1(expr, 0).first
}

private fun evaluate1(expr: List<Char>, start: Int): Pair<Long, Int> {
    var lastOper = 'X'
    var result = 0L
    var i = start
    while (i < expr.size) {
        val token = expr[i]
        if (token == ')') {
            return Pair(result, i)
        }

        if (token == '+' || token == '*') {
            lastOper = token
        } else {
            var n: Long
            if (token == '(') {
                val r = evaluate1(expr, i + 1)
                n = r.first
                i = r.second
            } else {
                n = token.toString().toLong()
            }

            when (lastOper) {
                '+' -> result += n
                '*' -> result *= n
                else -> result = n
            }
        }

        i++
    }
    return Pair(result, -1)
}

private fun evaluate2(expr: List<Char>): Long {
    val tokens : MutableList<Any> = expr.map { if (it in '0'..'9') it.toString().toLong() else it }.toMutableList()
    while ('(' in tokens) {
        val parenthesis = findInnermostParenthesis(tokens)
        val n = calculate2(tokens.subList(parenthesis.first + 1, parenthesis.last))
        tokens[parenthesis.first] = n
        for (i in parenthesis.first until parenthesis.last) {
            tokens.removeAt(parenthesis.first + 1)
        }
    }

    return calculate2(tokens)
}

fun calculate2(tokensParam: List<Any>): Long {
    val tokens = ArrayList(tokensParam)
    while ('+' in tokens) {
        val plusPos = tokens.indexOf('+')
        val result = (tokens[plusPos - 1] as Long) + (tokens[plusPos + 1] as Long)
        tokens[plusPos - 1] = result
        tokens.removeAt(plusPos + 1)
        tokens.removeAt(plusPos)
    }

    return tokens.filterIsInstance<Long>().reduce { acc, l -> acc * l }
}

private fun findInnermostParenthesis(tokens: List<Any>): IntRange {
    var state = 0
    var start = -1
    for (i in tokens.indices) {
        val token = tokens[i]
        if (token == '(') {
            state = 1
            start = i
        } else if (token == ')') {
            if (state == 1) {
                return IntRange(start, i)
            }
        }
    }
    throw IllegalArgumentException()
}

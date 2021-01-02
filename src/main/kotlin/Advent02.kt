
fun main() {
    val passwords = Utils.readInput("Advent02").lines().map { Password.parse(it) }

    solveA(passwords)
    solveB(passwords)
}

private fun solveA(passwords: List<Password>) {
    println(passwords.filter { it.compliesA() }.count())
}

private fun solveB(passwords: List<Password>) {
    println(passwords.filter { it.compliesB() }.count())
}

private data class Password(val password: String, val letter: String, val minOccurrences: Int,
                            val maxOccurrences: Int) {
    fun compliesA() : Boolean {
        val cnt = password.count { it.toString() == letter }
        return cnt in minOccurrences..maxOccurrences
    }

    fun compliesB() : Boolean {
        var cnt = 0
        if (password[minOccurrences - 1].toString() == letter) cnt++
        if (password[maxOccurrences - 1].toString() == letter) cnt++
        return cnt == 1
    }

    companion object {
        val PATTERN = Regex("([0-9]+)-([0-9]+) ([a-z]): ([a-z]+)").toPattern()

        fun parse(line: String) : Password {
            val matcher = PATTERN.matcher(line)
            if (!matcher.matches()) throw Exception("Bad line: $line")
            return Password(matcher.group(4), matcher.group(3), matcher.group(1).toInt(), matcher.group(2).toInt())
        }
    }
}
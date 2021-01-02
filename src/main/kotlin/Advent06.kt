
fun main() {
    val lines = Utils.readInput("Advent06").lines()

    // Part 1
    val groupSizes = mutableListOf(0)
    val uniqueAnswers = mutableListOf(mutableSetOf<Char>())
    val answerCounts = mutableListOf(mutableMapOf<Char, Int>())
    lines.forEach { line ->
        if (line.isEmpty()) {
            groupSizes.add(0)
            uniqueAnswers.add(mutableSetOf())
            answerCounts.add(mutableMapOf())
        } else {
            groupSizes[groupSizes.size -1] = groupSizes.last() + 1

            val groupA = uniqueAnswers.last()
            line.forEach { groupA.add(it) }

            val groupB = answerCounts.last()
            line.forEach { groupB.compute(it) { _, sum -> 1 + (sum ?: 0)} }
        }
    }

    // Part 1
    println(uniqueAnswers.map { it.size }.sum())

    // Part 2
    var part2 = 0
    answerCounts.forEachIndexed { groupIdx, counts ->
        part2 += counts.values.count { cnt -> cnt == groupSizes[groupIdx] }
    }
    println(part2)

}

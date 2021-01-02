import kotlin.math.abs

fun main() {
    val lines = Utils.readInput("Advent10").lines()

    val adapters = mutableListOf<Int>()
    adapters.add(0)
    adapters.addAll(lines.map { it.toInt() }.sorted())
    adapters.add((adapters.maxOrNull() ?: 0) + 3)

    // Part 1
    val diffs = mutableMapOf<Int, Int>()
    for (i in 1 until adapters.size) {
        diffs.compute(adapters[i] - adapters[i - 1]) { _, v -> (v ?: 0) + 1 }
    }
    println((diffs[1] ?: 0) * (diffs[3] ?: 0))

    // Part 2
    val ways = MutableList(adapters.size) { i -> if (i == 0) 1 else 0L }
    for (i in 1 until ways.size) {
        if (canGo(adapters, i, -1)) ways[i] += ways[i - 1]
        if (canGo(adapters, i, -2)) ways[i] += ways[i - 2]
        if (canGo(adapters, i, -3)) ways[i] += ways[i - 3]
    }
    println(ways.last())

}

fun canGo(adapters: List<Int>, i: Int, step: Int): Boolean {
    val j = i + step
    if (j < 0 || j >= adapters.size) return false
    if (abs(adapters[j] - adapters[i]) > 3) return false
    return true
}
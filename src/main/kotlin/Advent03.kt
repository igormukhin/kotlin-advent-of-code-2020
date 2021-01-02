
fun main() {
    val lines = Utils.readInput("Advent03").lines()

    solveA(lines)
    solveB(lines)
}

fun solveA(lines: List<String>) {
    val n = countTrees(lines, 3, 1)
    println(n)
}

fun solveB(lines: List<String>) {
    val counts = arrayListOf(
        countTrees(lines, 1, 1),
        countTrees(lines, 3, 1),
        countTrees(lines, 5, 1),
        countTrees(lines, 7, 1),
        countTrees(lines, 1, 2),
    )
    println(counts)
    println(counts.reduce { acc, i -> acc * i })
}

private fun countTrees(lines: List<String>, right: Int, down: Int): Long {
    var r = 0
    var c = 0
    var n = 0
    while (r < lines.size) {
        if (lines[r][c % lines[0].length] == '#') n++
        r += down
        c += right
    }
    return n.toLong()
}

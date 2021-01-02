import kotlin.math.absoluteValue

fun main() {
    val lines = Utils.readInput("Advent24").lines()

    var blacks = mutableSetOf<Pair<Int, Int>>()

    fun turn(point: Pair<Int, Int>) {
        if (blacks.contains(point)) {
            blacks.remove(point)
        } else {
            blacks.add(point)
        }
    }

    lines.asSequence().map { follow(Pair(0, 0), it) }.forEach { turn(it) }

    // Part 1
    println(blacks.size)

    // Part 2
    repeat(100) { blacks = animate(blacks) }
    println(blacks.size)
}

fun animate(blacks: MutableSet<Pair<Int, Int>>): MutableSet<Pair<Int, Int>> {
    val result = mutableSetOf<Pair<Int, Int>>()
    fun countBlacks(point: Pair<Int, Int>): Int = around(point) { p -> p in blacks }.count { it }

    blacks.forEach { point ->
        if (countBlacks(point) in 1..2) result.add(point)

        around(point) { p ->
            if (p !in blacks) {
                if (countBlacks(p) == 2) result.add(p)
            }
        }
    }

    return result
}

fun <X> around(point: Pair<Int, Int>, mapper: (Pair<Int, Int>) -> X): List<X> {
    return arrayOf("nw", "ne", "sw", "se", "e", "w").map { follow(point, it) }.map { mapper(it) }
}

fun follow(point: Pair<Int, Int>, commands: String): Pair<Int, Int> {
    var x = point.first
    var y = point.second
    var i = 0

    fun yOdd() = (y % 2).absoluteValue

    while (i < commands.length) {
        when {
            commands.containsAt(i, "nw") -> { y++; x -= yOdd(); i += 2 }
            commands.containsAt(i, "ne") -> { y++; x += 1 - yOdd(); i += 2 }
            commands.containsAt(i, "sw") -> { y--; x -= yOdd(); i += 2 }
            commands.containsAt(i, "se") -> { y--; x += 1 - yOdd(); i += 2 }
            commands.containsAt(i, "e") -> { x++; i++ }
            commands.containsAt(i, "w") -> { x--; i++ }
            else -> throw IllegalArgumentException(commands.substring(i))
        }
    }

    return Pair(x, y)
}

fun String.containsAt(at: Int, str: String): Boolean {
    return this.indexOf(str, at) == at
}


fun main() {
    val lines = Utils.readInput("Advent15").lines()
    val numbers = lines.first().split(",").map { it.toInt() }

    solvePart1(numbers)
    solvePart2(numbers)
}

private fun solvePart2(numbers: List<Int>) {
    val memory = mutableMapOf<Int, ShortMemory>()
    numbers.forEachIndexed { i, n -> memory[n] = ShortMemory(i) }

    var last = numbers.last()
    for (i in numbers.size until 30000000) {
        val shortMemory = memory[last]!!
        if (shortMemory.prev == -1) {
            last = 0
        } else {
            last = shortMemory.last - shortMemory.prev
        }
        memory.compute(last) { _, sm ->
            if (sm == null) {
                ShortMemory(i)
            } else {
                sm.remember(i)
                sm
            }
        }
    }

    println(last)
}

private class ShortMemory(var last: Int) {
    var prev = -1

    fun remember(newNumber: Int) {
        prev = last
        last = newNumber
    }

    override fun toString(): String {
        return "(last=$last, prev=$prev)"
    }

}

private fun solvePart1(numbers: List<Int>) {
    val moves = numbers.toMutableList()
    for (i in moves.size until 2020) {
        val last = moves.last()
        val prevOccur = indexOfBackwards(moves, last, i - 2)
        if (prevOccur == -1) {
            moves.add(0)
        } else {
            moves.add(i - 1 - prevOccur)
        }
    }

    println(moves.last())
}

fun indexOfBackwards(numbers: MutableList<Int>, numToFind: Int, startFrom: Int): Int {
    for (i in startFrom downTo 0) {
        if (numbers[i] == numToFind) {
            return i
        }
    }
    return -1
}

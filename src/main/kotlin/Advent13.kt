fun main() {
    val lines = Utils.readInput("Advent13").lines()

    solvePart1(lines)
    solvePart2(lines)
}

private fun solvePart1(lines: List<String>) {
    val now = lines.first().toInt()
    val buses = lines.last().split(",").filter { it != "x" }.map { it.toInt() }

    val waits = buses.map { bus ->
        if (now % bus == 0) {
            0
        } else {
            bus - (now % bus)
        }
    }

    val bestBusIndex = waits.withIndex().minByOrNull { (_, wait) -> wait }!!.index
    println(buses[bestBusIndex] * waits[bestBusIndex])
}

private fun solvePart2(lines: List<String>) {
    val buses = lines.last().split(",").map { if (it == "x") 0 else it.toLong() }

    val numbers = buses.filter { it != 0L }.sortedDescending()
    val positions = numbers.map { buses.indexOf(it).toLong() }

    fun isCommon(upToIndex: Int, multiplier: Long) : Boolean {
        val base = numbers[0] * multiplier - positions[0]
        return (1..upToIndex).all { i -> (base + positions[i]) % numbers[i] == 0L }
    }

    fun isFullCommon(multiplier: Long) : Boolean {
        return isCommon(numbers.size - 1, multiplier)
    }

    fun findCommon(upToIndex: Int, initial: Long, step: Long): Long {
        var m = initial
        while (true) {
            if (isCommon(upToIndex, m)) {
                return m
            }
            m += step
        }
    }

    var m = 0L
    var step = 1L
    for (i in 1 until numbers.size) {
        val firstHit = findCommon(i, m + step, step)
        if (isFullCommon(firstHit)) {
            m = firstHit
            break
        }

        val secondHit = findCommon(i, firstHit + step, step)
        if (isFullCommon(firstHit)) {
            m = secondHit
            break
        }

        m = secondHit
        step = secondHit - firstHit
    }

    println(numbers[0] * m - positions[0])
}

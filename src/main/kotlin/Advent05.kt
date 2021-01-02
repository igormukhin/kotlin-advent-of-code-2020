
fun main() {
    val lines = Utils.readInput("Advent05").lines()

    // Part 1
    val seatIds = lines.map { toSeatId(it) }
    val maxSeatId = seatIds.maxOrNull()!!
    println(maxSeatId)

    // Part 2
    for (id in 1 until maxSeatId) {
        if (seatIds.contains(id - 1) && !seatIds.contains(id) && seatIds.contains(id + 1)) {
            println("Part 2: $id")
        }
    }
}

private fun toSeatId(code : String) : Int {
    val row = binaryToInt(code.substring(0, 7), 'F', 'B')
    val col = binaryToInt(code.substring(7), 'L', 'R')

    return  row * 8 + col
}

private fun binaryToInt(str: String, zeroChar: Char, oneChar: Char): Int {
    var base = 1
    var n = 0
    str.reversed().forEach { ch ->
        if (ch == oneChar) {
            n += base
        }
        base *= 2;
    }
    return n
}


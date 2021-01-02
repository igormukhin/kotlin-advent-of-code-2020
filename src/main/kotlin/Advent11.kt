import Seating.State.*

fun main() {
    val lines = Utils.readInput("Advent11").lines()

    // Part 1
    solvePart1(lines)

    // Part 2
    solvePart2(lines)
}

private fun solvePart1(lines: List<String>) {
    var previous = Seating(lines)
    var next: Seating

    while (true) {
        next = Seating(previous.height(), previous.width())
        next.computeAll { row, col ->
            when (previous.get(row, col)) {
                EMPTY -> if (previous.countAround(row, col, OCCUPIED) == 0) OCCUPIED else EMPTY
                OCCUPIED -> if (previous.countAround(row, col, OCCUPIED) >= 4) EMPTY else OCCUPIED
                else -> FLOOR
            }
        }

        if (previous == next) {
            break
        }
        previous = next
    }

    println(previous.countAll(OCCUPIED))
}

private fun solvePart2(lines: List<String>) {
    var previous = Seating(lines)
    var next: Seating

    while (true) {
        next = Seating(previous.height(), previous.width())
        next.computeAll { row, col ->
            when (previous.get(row, col)) {
                EMPTY -> if (previous.countLookingAround(row, col, OCCUPIED) == 0) OCCUPIED else EMPTY
                OCCUPIED -> if (previous.countLookingAround(row, col, OCCUPIED) >= 5) EMPTY else OCCUPIED
                else -> FLOOR
            }
        }

        if (previous == next) {
            break
        }
        previous = next
    }

    println(previous.countAll(OCCUPIED))
}

private class Seating {
    enum class State {
        EMPTY,
        OCCUPIED,
        FLOOR
    }

    val seats: Array<Array<State>>

    constructor(height: Int, width: Int) {
        seats = Array(height) { Array(width) { EMPTY } }
    }

    constructor(lines: List<String>) {
        seats = Array(lines.size) { Array(lines.first().length) { EMPTY } }

        lines.forEachIndexed { i, line ->
            line.forEachIndexed { j, ch ->
                seats[i][j] = when (ch) {
                    'L' -> EMPTY
                    '#' -> OCCUPIED
                    else -> FLOOR
                }
            }
        }
    }

    fun height() : Int = seats.size
    fun width() : Int = seats.first().size

    fun set(row: Int, col: Int, state: State) {
        seats[row][col] = state
    }

    fun get(row: Int, col: Int) : State = seats[row][col]

    fun countAll(state: State) : Int = seats.sumBy { row -> row.sumBy { if (it == state) 1 else 0 } }

    fun countAround(row: Int, col: Int, state: State) : Int {
        var n = 0
        if (isSeatWithState(row - 1, col, state)) n++
        if (isSeatWithState(row - 1, col + 1, state)) n++
        if (isSeatWithState(row, col + 1, state)) n++
        if (isSeatWithState(row + 1, col + 1, state)) n++
        if (isSeatWithState(row + 1, col, state)) n++
        if (isSeatWithState(row + 1, col - 1, state)) n++
        if (isSeatWithState(row, col - 1, state)) n++
        if (isSeatWithState(row - 1, col - 1, state)) n++
        return n
    }

    fun countLookingAround(row: Int, col: Int, state: State) : Int {
        var n = 0
        if (lookForSeatWithState(row, -1, col, 0, state)) n++
        if (lookForSeatWithState(row, -1, col, 1, state)) n++
        if (lookForSeatWithState(row, 0, col, 1, state)) n++
        if (lookForSeatWithState(row, 1, col, 1, state)) n++
        if (lookForSeatWithState(row, 1, col, 0, state)) n++
        if (lookForSeatWithState(row, 1, col, -1, state)) n++
        if (lookForSeatWithState(row, 0, col, -1, state)) n++
        if (lookForSeatWithState(row, -1, col, -1, state)) n++
        return n
    }

    private fun lookForSeatWithState(row: Int, rowShift: Int, col: Int, colShift: Int, state: State): Boolean {
        var currRow = row
        var currCol = col

        while (true) {
            currRow += rowShift
            currCol += colShift
            if (!isSeat(currRow, currCol)) return false
            val currState = get(currRow, currCol)
            if (currState != FLOOR) {
                return currState == state
            }
        }
    }

    fun isSeat(row: Int, col: Int) : Boolean = row >= 0 && row < height() && col >= 0 && col < width()

    fun isSeatWithState(row: Int, col: Int, state: State) : Boolean = isSeat(row, col) && get(row, col) == state

    fun computeAll(block: (Int, Int) -> State) {
        seats.forEachIndexed { i, line ->
            line.forEachIndexed { j, _ ->
                set(i, j, block.invoke(i, j))
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other::class != this::class) return false
        return seats.contentDeepEquals((other as Seating).seats)
    }

    override fun hashCode(): Int {
        return seats.contentDeepHashCode()
    }
}
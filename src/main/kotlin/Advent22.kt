fun main() {
    val lines = Utils.readInput("Advent22").lines()

    val stacks = listOf(mutableListOf<Int>(), mutableListOf())
    var parserState = 0
    lines.forEach { line ->
        when {
            line.isEmpty() -> {}
            line == "Player 1:" -> parserState = 1
            line == "Player 2:" -> parserState = 2
            else -> stacks[parserState - 1].add(line.toInt())
        }
    }
    assert(stacks.size == 2)

    // Part 1
    solvePart1(stacks.map { it.toMutableList() })

    // Part 2
    solvePart2(stacks.map { it.toMutableList() })
}

private fun solvePart1(stacks: List<MutableList<Int>>) {
    while (true) {
        if (stacks.any { it.isEmpty() }) {
            break
        }

        val table = stacks.map { it.first() }
        stacks.forEach { it.removeAt(0) }
        val winner = table.indexOf(table.maxOrNull()!!)
        stacks[winner].add(table[winner])
        stacks[winner].add(table[(winner + 1) % 2])
    }

    val winner = stacks.first { it.isNotEmpty() }
    println(calcResult(winner))
}

private fun calcResult(winner: MutableList<Int>) =
    winner.asReversed().mapIndexed { i, n -> n * (i + 1) }.sum()

private fun solvePart2(stacks: List<MutableList<Int>>) {
    val game = RecursiveGame(stacks)
    val winner = game.play()
    println(calcResult(game.stacks[winner]))
}

private class RecursiveGame(val stacks: List<MutableList<Int>>) {
    val states = mutableSetOf<List<Int>>()

    fun play() : Int {
        while (true) {
            val loser = stacks.indexOfFirst { it.isEmpty() }
            if (loser != -1) {
                return (loser + 1) % 2
            }

            val state = getState()
            if (state in states) {
                // player 1 won
                return 0
            }
            states.add(state)

            val table = stacks.map { it.first() }
            stacks.forEach { it.removeAt(0) }

            val winner = if (table.mapIndexed { i, card -> stacks[i].size >= card }.all { it }) {
                RecursiveGame(table.mapIndexed { i, card -> stacks[i].subList(0, card).toMutableList() }).play()
            } else {
                table.indexOf(table.maxOrNull()!!)
            }

            stacks[winner].add(table[winner])
            stacks[winner].add(table[(winner + 1) % 2])
        }
    }

    private fun getState(): ArrayList<Int> {
        val state = ArrayList<Int>(1 + stacks.sumOf { it.size })
        state.addAll(stacks[0])
        state.add(0)
        state.addAll(stacks[1])
        return state
    }

}
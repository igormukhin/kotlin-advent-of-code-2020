
fun main() {
    val lines = Utils.readInput("Advent08").lines()
    val instructions = lines.map { Instruction.parse(it) }

    // Part 1
    val accumulator = runProgram(instructions)
    println(accumulator)

    // Part 2
    for (i in instructions.indices) {
        val (ended, result) = runProgram(instructions, i)
        if (ended) {
            println(result)
            break
        }
    }
}

private fun runProgram(instructions: List<Instruction>, fixAt: Int = -1): Pair<Boolean, Int> {
    val visited = mutableSetOf<Int>()
    var address = 0
    var accumulator = 0
    while (true) {
        if (address >= instructions.size) {
            return Pair(true, accumulator)
        }
        if (visited.contains(address)) {
            return Pair(false, accumulator)
        }
        visited.add(address)

        val instruction = instructions[address]
        var oper = instruction.operation
        if (address == fixAt) {
            oper = when (oper) {
                "nop" -> "jmp"
                "jmp" -> "nop"
                else -> oper
            }
        }
        when (oper) {
            "acc" -> accumulator += instruction.argument
            "jmp" -> address += instruction.argument - 1
        }
        address++
    }
}

private data class Instruction(val operation: String, val argument: Int) {
    companion object {
        fun parse(line: String) : Instruction {
            val (operation, tail) = line.split(" ")
            return Instruction(operation, tail.removePrefix("+").toInt())
        }
    }
}
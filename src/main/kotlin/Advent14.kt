fun main() {
    val lines = Utils.readInput("Advent14").lines()

    solvePart1(lines)
    solvePart2(lines)
}

private fun solvePart2(lines: List<String>) {
    var mask = Mask2("")
    val memory = mutableMapOf<Long, Long>()
    lines.forEach { line ->
        if (line.startsWith("mask")) {
            mask = Mask2(line.substringAfterLast(" "))
        } else {
            val address = line.substringAfter("[").substringBefore("]").toLong()
            val value = line.substringAfterLast(" ").toLong()
            mask.apply(address) { addr -> memory[addr] = value }
        }
    }

    println(memory.values.sum())
}

private fun solvePart1(lines: List<String>) {
    var mask = Mask(0L, 0L)
    val memory = mutableMapOf<Long, Long>()
    lines.forEach { line ->
        if (line.startsWith("mask")) {
            mask = parseMask(line.substringAfterLast(" "))
        } else {
            val address = line.substringAfter("[").substringBefore("]").toLong()
            val value = line.substringAfterLast(" ").toLong()
            memory[address] = mask.apply(value)
        }
    }

    println(memory.values.sum())
}

private fun parseMask(str: String) : Mask {
    var maskOnes = 0L
    var maskZeros = Long.MAX_VALUE
    var n = 1L
    str.reversed().forEach { ch ->
        when (ch) {
            '1' -> maskOnes += n
            '0' -> maskZeros -= n
        }
        n *= 2
    }
    return Mask(maskOnes, maskZeros)
}

private class Mask (val maskOnes : Long, val maskZeros : Long) {
    fun apply(value: Long) : Long = (value or maskOnes) and maskZeros
}

private class Mask2 (val mask: String) {

    private fun applyOnes(address: Long) : Long {
        var n = 1L
        var result = address
        mask.reversed().forEach { ch ->
            when (ch) {
                '1' -> result = result or n
            }
            n *= 2
        }
        return result
    }

    fun apply(address : Long, consumer : (Long) -> Unit) {
        var n = 1L
        val floaters = mutableListOf<Long>()
        mask.reversed().forEach { ch ->
            when (ch) {
                'X' -> floaters.add(n)
            }
            n *= 2
        }

        iterate(applyOnes(address), consumer, floaters, 0)
    }

    private fun iterate(base: Long, consumer: (Long) -> Unit, floaters: MutableList<Long>, i: Int) {
        if (i >= floaters.size) {
            consumer(base)
        } else {
            val zeroed = base and floaters[i].inv()
            iterate(zeroed, consumer, floaters, i + 1)
            val oned = base or floaters[i]
            iterate(oned, consumer, floaters, i + 1)
        }
    }
}

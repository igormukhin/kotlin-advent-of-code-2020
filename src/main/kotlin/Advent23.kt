fun main() {
    val lines = Utils.readInput("Advent23").lines()

    val initial = lines.first().map { it.toString().toInt() }
    solvePart1(initial)
    solvePart2(initial)
}

private fun solvePart1(initial: List<Int>) {
    val circle = Circle(initial, initial.size)

    repeat(100) {
        circle.play()
    }

    circle.select(1)
    println(circle.collect().drop(1).joinToString("") { it.toString() })
}

private fun solvePart2(initial: List<Int>) {
    val circle = Circle(initial, 1_000_000)

    repeat(10_000_000) {
        circle.play()
    }

    circle.select(1)
    circle.rotate()
    val v1 = circle.current.value
    circle.rotate()
    val v2 = circle.current.value
    println(v1.toLong() * v2)
}

private class Cup (val value: Int) {
    var prev: Cup? = null
    var next: Cup? = null

    fun contains(n: Int): Boolean {
        var current: Cup = this
        do {
            if (current.value == n) return true
            if (current.next == null) return false
            current = current.next!!
        } while (current !== this)
        return false
    }

    fun last(): Cup {
        var current = this
        while (current.next != null) {
            current = current.next!!
        }
        return current
    }

    fun connectNext(cup: Cup) {
        next = cup
        cup.prev = this
    }

    fun connectPrev(cup: Cup) {
        prev = cup
        cup.next = this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Cup
        if (value != other.value) return false
        return true
    }

    override fun hashCode(): Int {
        return value
    }

}

private class Circle(initial: List<Int>, val maxValue: Int) {
    var current: Cup = Cup(initial.first())
    val cups = mutableMapOf<Int, Cup>()

    init {
        cups[current.value] = current
        var prev = current
        initial.drop(1).forEach { n ->
            val cup = Cup(n)
            cups[cup.value] = cup
            prev.connectNext(cup)
            prev = cup
        }
        for (n in (initial.maxOrNull()!! + 1 .. maxValue)) {
            val cup = Cup(n)
            cups[cup.value] = cup
            prev.connectNext(cup)
            prev = cup
        }
        // close the circle
        prev.connectNext(current)
    }

    fun select(n: Int) {
        current = cups[n]!!
    }

    fun rotate() {
        current = current.next!!
    }

    fun play() {
        val cut = cutOutNext(3)
        var value = current.value - 1
        while (cut.contains(value)) value--
        if (value < 1) {
            value = maxValue
            while (cut.contains(value)) value--
        }

        val where = cups[value]!!
        insertAfter(cut, where)

        rotate()
    }

    private fun insertAfter(cut: Cup, where: Cup) {
        val end = where.next!!
        where.connectNext(cut)
        cut.last().connectNext(end)
    }

    @Suppress("SameParameterValue")
    private fun cutOutNext(n: Int): Cup {
        val start = current.next!!
        var end = start
        repeat(n - 1) {
            end = end.next!!
        }
        current.connectNext(end.next!!)

        start.prev = null
        end.next = null
        return start
    }

    fun collect(): List<Int> {
        var that = current
        val list = mutableListOf<Int>()
        do {
            list.add(that.value)
            if (that.next == null) break
            that = that.next!!
        } while(that !== current)
        return list
    }
}
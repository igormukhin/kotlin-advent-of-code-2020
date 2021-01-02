import java.lang.IllegalArgumentException

fun main() {
    val lines = Utils.readInput("Advent25").lines()

    val commonSecret = 7L
    val divider = 20201227L
    val cardPK = lines[0].toLong()
    val doorPK = lines[1].toLong()

    // Part 1
    fun findLoopSize(pk: Long): Int {
        var value = 1L
        var ls = 0
        while (true) {
            ls++
            value = (value * commonSecret) % divider
            if (value == pk) break
            if (ls == Int.MAX_VALUE) throw IllegalArgumentException()
        }
        return ls
    }

    fun produceEK(secret: Long, loopSize: Int): Long {
        var value = 1L
        repeat(loopSize) {
            value = (value * secret) % divider
        }
        return value
    }

    val cardLS = findLoopSize(cardPK)
    val doorLS = findLoopSize(doorPK)

    println(cardLS)
    println(doorLS)

    println(produceEK(doorPK, cardLS))
    println(produceEK(cardPK, doorLS))

    // Part 2
    // is free
}

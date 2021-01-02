
fun main() {
    val lines = Utils.readInput("Advent09").lines()

    val numbers = lines.map { it.toLong() }
    val tailSize = 25

    // Part 1
    var badNumber = 0L
    for (i in tailSize until numbers.size) {
        var found = false
loop@   for (j in i - tailSize until i - 1) {
            for (k in j + 1 until i) {
                if (numbers[i] == numbers[j] + numbers[k]) {
                    found = true
                    break@loop
                }
            }
        }

        if (!found) {
            badNumber = numbers[i]
            break
        }
    }
    println(badNumber)

    // Part 2
    var sumNumbers = listOf<Long>()
l2@ for (i in 0..numbers.size - 2) {
        var sum = 0L
        for (j in 0 until numbers.size - i) {
            sum += numbers[i + j]
            if (sum == badNumber) {
                sumNumbers = numbers.subList(i, i + j - 1)
                break@l2
            } else if (sum > badNumber) {
                break
            }
        }
    }
    println(sumNumbers.minOrNull()!! + sumNumbers.maxOrNull()!!)
}

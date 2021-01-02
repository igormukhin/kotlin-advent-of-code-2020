
fun main() {
    val numbers = Utils.readInput("Advent01").lines().map { x -> x.toInt() }

    solveA(numbers)
    solveB(numbers)
}

private fun solveA(numbers: List<Int>) {
    for (i in 0..numbers.size - 2) {
        for (j in 1 until numbers.size) {
            if (numbers[i] + numbers[j] == 2020) {
                println(numbers[i] * numbers[j])
                return
            }
        }
    }
}

private fun solveB(numbers: List<Int>) {
    for (i in 0..numbers.size - 3) {
        for (j in 0..numbers.size - 2) {
            for (k in 1 until numbers.size) {
                if (numbers[i] + numbers[j] + numbers[k] == 2020) {
                    println(numbers[i] * numbers[j] * numbers[k])
                    return
                }
            }
        }
    }
}

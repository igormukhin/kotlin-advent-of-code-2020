
fun main() {
    val lines = Utils.readInput("Advent16").lines()

    val fields = mutableMapOf<String, Pair<IntRange, IntRange>>()
    var state = 0
    val tickets = mutableListOf<List<Int>>()
    lines.forEach { line ->
        if (line.isNotEmpty()) {
            when {
                line == "your ticket:" -> {
                    state = 1
                }
                line == "nearby tickets:" -> {
                    state = 2
                }
                state == 0 -> {
                    val (a, b) = line.substringAfter(": ").split(" or ").map { rng ->
                        rng.split("-").let { IntRange(it[0].toInt(), it[1].toInt()) } }
                    fields[line.substringBefore(":")] = Pair(a, b)
                }
                else -> {
                    tickets.add(line.split(",").map { it.toInt() })
                }
            }
        }
    }

    // Part 1
    var error = 0
    val validTickets = mutableListOf<List<Int>>()
    tickets.forEach { ticket ->
        var valid = true
        ticket.forEach { number ->
            if (fields.values.none { pair -> inRange(pair, number) }) {
                error += number
                valid = false
            }
        }
        if (valid) {
            validTickets.add(ticket)
        }
    }

    println(error)

    // Part 2
    val fits = mutableMapOf<String, MutableList<Int>>()
    fields.forEach { (fld, rangePair) ->
        fits[fld] = mutableListOf()
        for (i in validTickets.first().indices) {
            if (validTickets.all { t -> inRange(rangePair, t[i]) }) {
                fits[fld]!!.add(i)
            }
        }
    }

    val fieldIndexes = mutableMapOf<String, Int>()
    while (fits.isNotEmpty()) {
        val field = fits.entries.first { it.value.size == 1 }
        val fieldIndex = field.value.first()
        fieldIndexes[field.key] = fieldIndex
        fits.remove(field.key)
        fits.values.forEach { it.remove(fieldIndex) }
    }

    var product = 1L
    fieldIndexes.filter { it.key.startsWith("departure") }
        .map { it.value }.map { tickets.first()[it] }
        .forEach { product *= it }
    println(product)
}

private fun inRange(
    pair: Pair<IntRange, IntRange>,
    number: Int
) = pair.first.contains(number) || pair.second.contains(number)

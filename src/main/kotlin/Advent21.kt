fun main() {
    val lines = Utils.readInput("Advent21").lines()

    val dishes = lines.map { line ->
        Dish(line.substringBefore(" (").split(" "),
            line.substringAfter("(contains ").substringBefore(")").split(", "))
    }
    val ingredients = dishes.map { it.ingredients }.flatten().distinct()
    val allergens = dishes.map { it.allergens }.flatten().distinct()

    val unsafeIngredients = mutableSetOf<String>()
    val current = Array(allergens.size) { "" }

    fun complies(at: Int, ingredient: String): Boolean {
        return dishes.all { dish ->
            when {
                allergens[at] in dish.allergens -> ingredient in dish.ingredients
                ingredient in dish.ingredients -> dish.ingredients.size > dish.allergens.size
                else -> true
            }
        }
    }

    fun solvePart2(current: Array<String>) {
        val sorted = current.copyOf()
        sorted.sortBy { allergens[current.indexOf(it)] }
        println("Part 2: " + sorted.joinToString(","))
    }

    fun iterate(at: Int) {
        if (at == current.size) {
            unsafeIngredients.addAll(current)
            solvePart2(current)
            return
        }

        for (i in ingredients.indices) {
            if (ingredients[i] in current) {
                continue
            }

            if (!complies(at, ingredients[i])) {
                continue
            }

            current[at] = ingredients[i]
            iterate(at + 1)
            current[at] = ""
        }
    }

    iterate(0)

    val safes = mutableSetOf<String>()
    safes.addAll(ingredients)
    safes.removeAll(unsafeIngredients)

    println("Part 1: " + dishes.sumBy { dish -> safes.count { safe -> safe in dish.ingredients } })

}

private data class Dish(val ingredients: List<String>, val allergens: List<String>)
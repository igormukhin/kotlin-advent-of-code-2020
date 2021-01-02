@file:Suppress("DuplicatedCode")

fun main() {
    val lines = Utils.readInput("Advent17").lines()

    val initial = mutableSetOf<Point>()
    lines.forEachIndexed { x, line ->
        line.forEachIndexed { y, ch ->
            if (ch == '#') {
                initial.add(Point(x, y, 0, 0))
            }
        }
    }

    solvePart1(initial)
    solvePart2(initial)
}

private data class Point(
    val x: Int,
    val y: Int,
    val z: Int,
    val w: Int
) {
    override fun toString(): String = "($x, $y, $z, $w)"
}


private fun solvePart1(initial: Set<Point>) {
    var current = initial
    for (cycle in 1..6) {
        val next = mutableSetOf<Point>()
        for (x in dimensionRange(current) { it.x }) {
            for (y in dimensionRange(current) { it.y }) {
                for (z in dimensionRange(current) { it.z }) {
                    val p = Point(x, y, z, 0)
                    val n = countAround(current, p)
                    if (current.contains(p)) {
                        if (n == 2 || n == 3) {
                            next.add(p)
                        }
                    } else {
                        if (n == 3) {
                            next.add(p)
                        }
                    }
                }
            }
        }
        current = next
    }

    println(current.size)
}

private fun countAround(points: Set<Point>, point: Point) : Int {
    var n = 0
    for (x in -1..1) {
        for (y in -1..1) {
            for (z in -1..1) {
                for (w in -1..1) {
                    val p = Point(point.x + x, point.y + y, point.z + z, point.w + w)
                    if (p != point && points.contains(p)) {
                        n++
                    }
                }
            }
        }
    }
    return n
}

private fun dimensionRange(points: Set<Point>, extractor: (Point) -> Int) : IntRange {
    val min = points.minByOrNull { extractor(it) }
    val max = points.maxByOrNull { extractor(it) }

    return IntRange(
        if (min == null) 0 else extractor(min) - 1,
        if (max == null) 0 else extractor(max) + 1)
}

private fun solvePart2(initial: Set<Point>) {
    var current = initial
    for (cycle in 1..6) {
        val next = mutableSetOf<Point>()
        for (x in dimensionRange(current) { it.x }) {
            for (y in dimensionRange(current) { it.y }) {
                for (z in dimensionRange(current) { it.z }) {
                    for (w in dimensionRange(current) { it.w }) {
                        val p = Point(x, y, z, w)
                        val n = countAround(current, p)
                        if (current.contains(p)) {
                            if (n == 2 || n == 3) {
                                next.add(p)
                            }
                        } else {
                            if (n == 3) {
                                next.add(p)
                            }
                        }
                    }
                }
            }
        }
        current = next
    }

    println(current.size)
}


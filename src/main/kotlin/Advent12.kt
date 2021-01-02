import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.math.abs

fun main() {
    val lines = Utils.readInput("Advent12").lines()

    // Part 1
    val ship = Ship()
    lines.forEach { line ->
        ship.execute(line[0], line.substring(1).toInt())
    }
    println(abs(ship.x) + abs(ship.y))

    // Part 2
    val shipWithWaypoint = ShipWithWaypoint()
    lines.forEach { line ->
        shipWithWaypoint.execute(line[0], line.substring(1).toInt())
        //println("$line: $shipWithWaypoint")
    }
    println(abs(shipWithWaypoint.x) + abs(shipWithWaypoint.y))
}

private fun degreeToCardinal(degree: Int): Char {
    return when (degree) {
        0 -> 'E'
        90 -> 'S'
        180 -> 'W'
        270 -> 'N'
        else -> throw IllegalStateException(degree.toString())
    }
}

private class Ship {
    var x = 0
    var y = 0
    var dir = 0

    fun execute(what: Char, howMuch: Int) {
        when (what) {
            'N' -> y += howMuch
            'S' -> y -= howMuch
            'E' -> x += howMuch
            'W' -> x -= howMuch
            'R' -> dir = (dir + howMuch) % 360
            'L' -> dir = (dir + 360 - howMuch) % 360
            'F' -> execute(degreeToCardinal(dir), howMuch)
        }
    }
}

private class ShipWithWaypoint {
    var x = 0
    var y = 0

    var wpX = 10
    var wpY = 1

    fun execute(what: Char, howMuch: Int) {
        when (what) {
            'N' -> wpY += howMuch
            'S' -> wpY -= howMuch
            'E' -> wpX += howMuch
            'W' -> wpX -= howMuch
            'R' -> rotateWpBy(howMuch)
            'L' -> rotateWpBy(360 - howMuch)
            'F' -> {
                x += howMuch * wpX
                y += howMuch * wpY
            }
        }
    }

    private fun rotateWpBy(howMuch: Int) {
        val newX: Int
        val newY: Int
        when (howMuch) {
            90 -> { newX = wpY; newY = -wpX }
            180 -> { newX = -wpX; newY = -wpY }
            270 -> { newX = -wpY; newY = wpX }
            else -> throw IllegalArgumentException(howMuch.toString())
        }
        wpX = newX
        wpY = newY
    }

    override fun toString(): String {
        return "x:$x\ty:$y\t\twpX:$wpX\twpY:$wpY"
    }
}
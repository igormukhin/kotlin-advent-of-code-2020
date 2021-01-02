import kotlin.math.sqrt

fun main() {
    val lines = Utils.readInput("Advent20").lines()

    var id = 0L
    var bits: MutableList<List<Boolean>> = ArrayList()
    val tiles = mutableListOf<Tile>()
    lines.forEach { line ->
        if (line.startsWith("Tile ")) {
            id = line.substringAfter("Tile ").substringBefore(":").toLong()
            bits = ArrayList()
        } else if (line.isEmpty()) {
            tiles.add(Tile(id, bits))
        } else {
            bits.add(line.map { it == '#'})
        }
    }
    tiles.add(Tile(id, bits))
    val imgSize = sqrt(tiles.size.toDouble()).toInt()

    // Part 1
    val image = Array(imgSize) {
        Array<Tile?>(imgSize) { null }
    }

    fun iterate(pos: Int): Boolean {
        if (pos == imgSize * imgSize) {
            return true
        }

        val i = pos / imgSize
        val j = pos % imgSize
        for (k in tiles.indices) {
            var tile = tiles[k]
            if (image.any { row -> row.any { cell -> cell?.id == tile.id } }) {
                continue
            }

            for (f in 0..1) {
                if (f == 1) tile = tile.flip()
                for (r in 0..3) {
                    tile = tile.rotate()
                    if (i > 0 && !image[i - 1][j]!!.adjacentBottomTo(tile)) {
                        continue
                    }
                    if (j > 0 && !image[i][j - 1]!!.adjacentRightTo(tile)) {
                        continue
                    }

                    image[i][j] = tile

                    if (iterate(pos + 1)) {
                        return true
                    }
                }
            }
            image[i][j] = null
        }

        return false
    }

    if (!iterate(0)) {
        throw IllegalArgumentException("No image found")
    }

/*
    for (i in 0 until imgSize) {
        for (j in 0 until imgSize) {
            println()
            val tile = image[i][j]!!
            tile.print()
        }
    }
*/

    println(image[0][0]!!.id * image[imgSize - 1][0]!!.id *
            image[0][imgSize - 1]!!.id * image[imgSize - 1][imgSize - 1]!!.id)

    // Part 2
    var lake = image.map { line -> line.map { it!!.removeBorder() }.reduce { acc, tile -> acc.mergeRight(tile) } }
        .reduce { acc, row -> acc.mergeDown(row) }
    //lake.print()

    val monster = Tile(0, listOf(
        "                  # ".map { it == '#' },
        "#    ##    ##    ###".map { it == '#' },
        " #  #  #  #  #  #   ".map { it == '#' }
    ))
    //monster.print()

first@
    for (f in 0..1) {
        if (f == 1) lake = lake.flip()
        for (r in 0..3) {
            lake = lake.rotate()

            var found = false
            for (i in 0 until lake.height() - monster.height()) {
                for (j in 0 until lake.width() - monster.width()) {
                    if (lake.fitsAt(i, j, monster)) {
                        found = true
                        lake = lake.eraseAt(i, j, monster)
                    }
                }
            }
            if (found) {
                break@first
            }
        }
    }
    lake.print()
    println(lake.countDots())
}

private data class Tile(val id: Long, val bits: List<List<Boolean>>) {

    fun height() : Int = bits.size
    fun width() : Int = bits.first().size

    fun flip() : Tile {
        return Tile(id, bits.map { line -> line.asReversed() })
    }

    fun rotate() : Tile {
        val m = ArrayList<MutableList<Boolean>>(width())
        bits.forEach { _ -> m.add(Array(height()) { false }.toMutableList()) }
        for (i in 0 until height()) {
            for (j in 0 until width()) {
                m[j][height() - 1 - i] = bits[i][j]
            }
        }
        return Tile(id, m)
    }

    fun adjacentRightTo(tile: Tile) : Boolean {
        assert(height() == tile.height())
        return (0 until height()).all { i ->
            bits[i][height() - 1] == tile.bits[i][0]
        }
    }

    fun adjacentBottomTo(tile: Tile) : Boolean {
        assert(width() == tile.width())
        return (0 until height()).all { j ->
            bits[height() - 1][j] == tile.bits[0][j]
        }
    }

    fun removeBorder() : Tile {
        assert(height() > 2 && width() > 2)

        val m = ArrayList<MutableList<Boolean>>(height() - 2)
        repeat(height() - 2) { m.add(Array(width() - 2) { false }.toMutableList()) }
        for (i in 1 until height() - 1) {
            for (j in 1 until width() - 1) {
                m[i - 1][j - 1] = bits[i][j]
            }
        }
        return Tile(0L, m)
    }

    fun mergeRight(tile: Tile) : Tile {
        assert(height() == tile.height())

        val m = ArrayList<MutableList<Boolean>>(height())
        repeat(height()) { m.add(Array(width() + tile.width()) { false }.toMutableList()) }
        for (i in 0 until height()) {
            for (j in 0 until width()) {
                m[i][j] = bits[i][j]
            }
        }
        for (i in 0 until tile.height()) {
            for (j in 0 until tile.width()) {
                m[i][width() + j] = tile.bits[i][j]
            }
        }

        return Tile(0L, m)
    }

    fun mergeDown(tile: Tile) : Tile {
        assert(width() == tile.width())

        val m = ArrayList<MutableList<Boolean>>(height() + tile.height())
        repeat(height() + tile.height()) { m.add(Array(width()) { false }.toMutableList()) }
        for (i in 0 until height()) {
            for (j in 0 until width()) {
                m[i][j] = bits[i][j]
            }
        }
        for (i in 0 until tile.height()) {
            for (j in 0 until tile.width()) {
                m[height() + i][j] = tile.bits[i][j]
            }
        }

        return Tile(0L, m)
    }

    fun print() {
        println(id)
        for (k in 0 until height()) {
            println(bits[k].map { if (it) "#" else "." }.reduce { acc, s -> acc + s })
        }
    }

    fun fitsAt(x: Int, y: Int, tile: Tile) : Boolean {
        for (i in 0 until tile.height()) {
            for (j in 0 until tile.width()) {
                if (tile.bits[i][j] && !bits[x + i][y + j]) {
                    return false
                }
            }
        }
        return true
    }

    fun eraseAt(x: Int, y: Int, tile: Tile) : Tile {
        val m = ArrayList<MutableList<Boolean>>(height())
        repeat(height()) { m.add(Array(width()) { false }.toMutableList()) }
        for (i in 0 until height()) {
            for (j in 0 until width()) {
                val i2 = i - x
                val j2 = j - y
                if (i2 >= 0 && i2 < tile.height() && j2 >= 0 && j2 < tile.width()) {
                    if (!tile.bits[i2][j2]) {
                        m[i][j] = bits[i][j]
                    }
                } else {
                    m[i][j] = bits[i][j]
                }
            }
        }
        return Tile(id, m)
    }

    fun countDots() : Int {
        return bits.map { row -> row.count { it } }.sum()
    }
}

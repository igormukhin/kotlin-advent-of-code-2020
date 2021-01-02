
val required = setOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")

fun main() {
    val lines = Utils.readInput("Advent04").lines()
    val passports = mutableListOf<MutableMap<String, String>>(HashMap())
    lines.forEach { line ->
        if (line.isEmpty()) {
            passports.add(HashMap())
        } else {
            line.split(' ').forEach { pair ->
                val (key, value) = pair.split(':')
                passports.last()[key] = value
            }
        }
    }

    // Part 1
    println(passports.count { isValidA(it) })

    // Part 2
    println(passports.count { isValidB(it) })
}

fun isValidA(map: Map<String, String>) : Boolean {
    required.forEach { if (!map.containsKey(it)) return false }
    return true
}

fun isValidB(map: Map<String, String>) : Boolean {
    if (!isValidA(map)) return false

    val byr = map.getOrDefault("byr", "")
    if (!isValidNumber(byr, 4, 1920, 2002)) return false

    val iyr = map.getOrDefault("iyr", "")
    if (!isValidNumber(iyr, 4, 2010, 2020)) return false

    val eyr = map.getOrDefault("eyr", "")
    if (!isValidNumber(eyr, 4, 2020, 2030)) return false

    val hgt = map.getOrDefault("hgt", "")
    if (hgt.endsWith("cm")) {
        if (!isValidNumber(hgt.substringBefore("cm"), null, 150, 193)) return false
    } else if (hgt.endsWith("in")) {
        if (!isValidNumber(hgt.substringBefore("in"), null, 59, 76)) return false
    } else {
        return false
    }

    val hcl = map.getOrDefault("hcl", "")
    if (!hcl.matches(Regex("#[0-9a-f]{6}"))) return false

    val ecl = map.getOrDefault("ecl", "")
    if (ecl !in arrayOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")) return false

    val pid = map.getOrDefault("pid", "")
    if (!pid.matches(Regex("[0-9]{9}"))) return false

    return true
}

fun isValidNumber(value: String, digits: Int?, minValue: Int, maxValue: Int): Boolean {
    if (digits != null && value.length != digits) return false
    value.forEach { if (!it.isDigit()) return false }
    val num = value.toInt()
    if (num < minValue || num > maxValue) return false

    return true
}

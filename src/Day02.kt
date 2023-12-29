import kotlin.math.max

data class Game(val gameId: Int, val maxRed: Int, val maxGreen: Int, val maxBlue: Int) {
    fun sum() = maxRed * maxGreen * maxBlue

    fun playable() = maxRed <= 12 && maxGreen <= 13 && maxBlue <= 14

    companion object Parser {
        fun parse(raw: String): Game {
            val trimmed = raw.substringAfter(":").trim()
            val game = raw.substringBefore(":").substring("Game ".length).toInt()
            val map = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)
            val items = trimmed.split(';', ',')

            for (item in items) {
                val pair = item.trim().split(" ")
                val count = pair[0].toInt()
                val color = pair[1]
                map[color] = max(map.getOrDefault(color, 0), count)
            }

            return Game(game, map["red"]!!, map["green"]!!, map["blue"]!!)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.map { Game.parse(it) }.filter { it.playable() }.sumOf { it.gameId }
    }

    fun part2(input: List<String>) =
        input.map {Game.parse(it) }.sumOf { it.sum() }


    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()

    val testInput2 = readInput("Day02_test")
    check(part2(testInput2) == 2286)
    part2(input).println()
}

fun main() {

    fun strToNumber(str: String): Int {
        val first = str.find { it in '0'..'9' }
        val last = str.findLast { it in '0'..'9' }
        return "$first$last".toInt()

    }

    fun part1(input: List<String>): Int {
        return input.sumOf { strToNumber(it) }
    }

    fun strToNumber2(str: String): Int {
        val map = mapOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9
        )
        var first: Int? = 0
        for (idx in str.indices) {
            if (str[idx] in '0'..'9') {
                first = str[idx].digitToInt()
                break
            } else {
                val v = map.entries.find { str.startsWith(it.key, idx) }?.value
                if (v != null) {
                    first = v
                    break
                }
            }
        }

        var last: Int? = 0
        for (idx in str.length - 1 downTo 0) {
            if (str[idx] in '0'..'9') {
                last = str[idx].digitToInt()
                break
            } else {
                val l = map.entries.find { str.startsWith(it.key, idx) }?.value
                if (l != null) {
                    last = l
                    break
                }
            }
        }

        return "$first$last".toInt()
    }


    fun part2(input: List<String>): Int {
        return input.sumOf { strToNumber2(it) }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()

    val testInput2 = readInput("Day012_test")
    check(part2(testInput2) == 281)
    part2(input).println()
}

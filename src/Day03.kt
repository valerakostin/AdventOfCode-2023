sealed class Element(val row: Int, val range: IntRange) {
    data class Number(val number: Int, val r: Int, val range1: IntRange) : Element(r, range1)
    data class Gear(val symbol: Char, val r: Int, val range1: IntRange) : Element(r, range1)
}

data class Field(val raw: List<String>, val elements: List<List<Element>>) {
    private fun isValidSymbol(ch: Char) = ch != '.' && !ch.isDigit()

    private fun isAdjusted(loc: Element): Boolean {
        val columnRange = 0 until raw[0].length
        val rowRange = raw.indices

        // up row
        if (loc.row - 1 in rowRange) {
            val range = loc.range
            for (i in range.first - 1..range.last + 1) {
                if (i in columnRange && isValidSymbol(raw[loc.row - 1][i])) {
                    return true
                }
            }
        }
        if (loc.row + 1 in rowRange) {
            val range = loc.range
            for (i in range.first - 1..range.last + 1) {
                if (i in columnRange && isValidSymbol(raw[loc.row + 1][i])) {
                    return true
                }
            }
        }

        // middle row
        if (loc.range.first - 1 in rowRange && isValidSymbol(raw[loc.row][loc.range.first - 1])) {
            return true
        }

        if (loc.range.last + 1 in rowRange && isValidSymbol(raw[loc.row][loc.range.last + 1])) {
            return true
        }

        return false
    }

    fun adjustable() = elements.flatten().filterIsInstance<Element.Number>().filter { isAdjusted(it) }.toList()

    private fun getNumberNeighbours(el: Element): List<Int> {
        val result = mutableListOf<Int>()
        val row = el.row

        val elementRange = el.range
        val start = if (elementRange.first - 1 >= 0) elementRange.first - 1 else 0
        val end = if (elementRange.last <= raw[0].length - 1) elementRange.last + 1 else elementRange.last
        val range = start..end
        // up
        if (row - 1 >= 0) {
            val rowElements = elements[row - 1]
            val n = rowElements.filterIsInstance<Element.Number>().filter { it.range.intersect(range).isNotEmpty() }
                .map { it.number }
            result.addAll(n)
        }
        // down
        if (row + 1 < elements.size) {
            val rowElements = elements[row + 1]
            val n = rowElements.filterIsInstance<Element.Number>().filter { it.range.intersect(range).isNotEmpty() }
                .map { it.number }
            result.addAll(n)
        }
        // left
        if (elementRange.first - 1 >= 0) {
            val rowElements = elements[row]
            val leftElement = elementRange.first - 1
            val n =
                rowElements.filterIsInstance<Element.Number>().filter { it.range.last == leftElement }.map { it.number }
            result.addAll(n)

        }
        // right
        if (elementRange.last + 1 <= raw[0].length - 1) {
            val rowElements = elements[row]
            val rightElement = elementRange.last + 1
            val n = rowElements.filterIsInstance<Element.Number>().filter { it.range.first == rightElement }
                .map { it.number }
            result.addAll(n)
        }

        return result
    }

    fun gearRations(): List<Long> {
        val result = mutableListOf<Long>()
        for (row in elements) {
            for (el in row) {
                if (el is Element.Gear) {
                    val list = getNumberNeighbours(el)
                    if (list.size == 2) {
                        val product = list.reduce { acc, i -> acc * i }
                        result.add(product.toLong())
                    }
                }
            }
        }

        return result
    }


    companion object Parser {
        fun parse(lines: List<String>): Field {
            val regex = """(\d+)|([*])+""".trimMargin().toRegex()
            val elements = mutableListOf<List<Element>>()
            for (idx in lines.indices) {
                val line = lines[idx]
                val match = regex.findAll(line)
                val rowElements = mutableListOf<Element>()
                val iterator = match.iterator()
                while (iterator.hasNext()) {
                    val next = iterator.next()
                    if (next.value[0].isDigit()) {
                        val value = next.value.toInt()
                        val range = next.range
                        val el = Element.Number(value, idx, range)
                        rowElements.add(el)
                    } else {
                        val value = next.value[0]
                        val range = next.range
                        val el = Element.Gear(value, idx, range)
                        rowElements.add(el)
                    }
                }
                elements.add(rowElements)
            }
            return Field(lines, elements)
        }
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        return Field.parse(input).adjustable().sumOf { it.number }
    }

    fun part2(input: List<String>): Long {
        return Field.parse(input).gearRations().sumOf { it }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()

    val testInput2 = readInput("Day03_test")
    check(part2(testInput2) == 467835L)
    part2(input).println()
}


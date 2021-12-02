fun main() {
    // Part 1
    readDepthList("Day01_test")
        .totalDepthIncreases()
        .also { check(it == 7) }
    readDepthList("Day01")
        .totalDepthIncreases()
        .also(::println)

    // Part 2
    readDepthList("Day01_test")
        .slidingWindowTotals()
        .totalDepthIncreases()
        .also { check(it == 5) }
    readDepthList("Day01")
        .slidingWindowTotals()
        .totalDepthIncreases()
        .also(::println)
}

private fun List<Int>.slidingWindowTotals() =
    mapIndexed { index, depth ->
        if(index + 2 < size) depth + get(index+1) + get(index+2) else null
    }.filterNotNull()

private fun List<Int>.totalDepthIncreases() =
    mapIndexed { index, depth ->
        if (getPreviousDepth(index, depth) >= depth) 0 else 1
    }.sum()

private fun <T> List<T>.getPreviousDepth(index: Int, depth: T) = this.getOrElse(index - 1) { depth }

private fun readDepthList(name: String) = readInput(name).map { it.toInt() }
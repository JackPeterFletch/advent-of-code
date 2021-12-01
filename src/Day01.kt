fun main() {
    // Part 1
    readIntInput("Day01_test")
        .getTotalDepthIncreases()
        .also { check(it == 7) }
    readIntInput("Day01")
        .getTotalDepthIncreases()
        .also(::println)

    // Part 2
    readIntInput("Day01_test")
        .withSlidingWindows()
        .getTotalDepthIncreases()
        .also { check(it == 5) }
    readIntInput("Day01")
        .withSlidingWindows()
        .getTotalDepthIncreases()
        .also(::println)
}

private fun List<Int>.withSlidingWindows() =
    mapIndexed { index, depth ->
        if(index + 2 < size){ depth + get(index+1) + get(index+2) } else { null }
    }.filterNotNull()

private fun List<Int>.getTotalDepthIncreases() =
    mapIndexed { index, depth ->
        if (getPreviousDepth(index, depth) >= depth) { 0 } else { 1 }
    }.sum()

private fun <T> List<T>.getPreviousDepth(index: Int, depth: T) = this.getOrElse(index - 1) { depth }


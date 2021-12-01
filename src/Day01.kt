fun main() {
    // Part 1
    readIntInput("Day01_test") getTotalDepthIncreases { check(this == 7) }
    readIntInput("Day01") getTotalDepthIncreases ::println

    // Part 2
    readIntInput("Day01_test").withSlidingWindows() getTotalDepthIncreases { check(this == 5) }
    readIntInput("Day01").withSlidingWindows() getTotalDepthIncreases ::println
}

private fun List<Int>.withSlidingWindows() =
    mapIndexed { index, depth ->
        if(index + 2 < size){ depth + get(index+1) + get(index+2) } else { null }
    }.filterNotNull()

private infix fun List<Int>.getTotalDepthIncreases(block: Int.() -> Unit) =
    mapIndexed { index, depth ->
        if (getPreviousDepth(index, depth) >= depth) { 0 } else { 1 }
    }.sum().block()

private fun <T> List<T>.getPreviousDepth(index: Int, depth: T) = this.getOrElse(index - 1) { depth }


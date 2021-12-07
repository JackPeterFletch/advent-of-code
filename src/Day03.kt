
fun main() {
    // Part 01
    val gammaRate = readInput("Day03_test")
        .getGammaRate()
        .parseStringBinary()
        .apply(::println)

    val epsilonRate = readInput("Day03_test")
        .getGammaRate()
        .gammaRateToEpsilonRate()
        .parseStringBinary()
        .apply(::println)

    println(gammaRate * epsilonRate)

    // Part 02
    val oxygenGeneratorRating = readInput("Day03")
        .filterOnBitMask(List<String>::getGammaRate)
        .parseStringBinary()
        .apply(::println)

    val co2ScrubberRating = readInput("Day03")
        .filterOnBitMask(List<String>::getEpsilonRate)
        .parseStringBinary()
        .apply(::println)

    println(oxygenGeneratorRating * co2ScrubberRating)

}

private tailrec fun List<String>.filterOnBitMask(bitRetentionMask: List<String>.() -> String, index: Int = 0): String {
    val filtered = filter { it[index] == bitRetentionMask()[index] }
    return if (filtered.size == 1 )
        filtered[0]
    else
        filtered.filterOnBitMask(bitRetentionMask, index + 1)
}

fun List<String>.getGammaRate() =
    map { it.toCharArray().toList() }
    .toList()
    .transpose()
    .map { charArray ->
        when  {
            charArray.count { it == '1' } > (charArray.size / 2) -> 1
            charArray.count { it == '0' } > (charArray.size / 2) -> 0
            else -> 1
        }
    }
    .joinToString("")

fun List<String>.getEpsilonRate() = getGammaRate().gammaRateToEpsilonRate()

fun String.gammaRateToEpsilonRate() =
    map {
        when (it) {
            '1' -> '0'
            '0' -> '1'
            else -> throw Exception("Not Binary")
        }
    }.joinToString("")

fun String.parseStringBinary() = let { Integer.parseInt(it, 2) }

fun List<List<Char>>.transpose(transposed: MutableList<MutableList<Char>> = mutableListOf()): List<List<Char>>{
    forEachIndexed { i, row ->
        row.forEachIndexed { j, _ ->
            if (transposed.getOrNull(j) == null) { transposed.add(mutableListOf()) }
            transposed[j].add(this[i][j])
            transposed[j][i] = this[i][j]
        }
    }
    return transposed
}
fun main(){
    part01("Day05_test", 9)
    part01("Day05", 1000)
    part02("Day05_test", 9)
    part02("Day05", 1000)
}

private fun part01(input: String, gridSize: Int) {
    val ventLines = readInput(input).extractLines().filter { it.isFlat() }

    assessDangerousTiles(gridSize, ventLines) { line, coord ->
        line.crossesCoordFlat(coord)
    }.apply { println(this.size) }
}

private fun part02(input: String, gridSize: Int) {
    val ventLines = readInput(input).extractLines()

    assessDangerousTiles(gridSize, ventLines) { line, coord ->
        line.crossesCoordFlat(coord) || line.crossesCoordDiagonal(coord)
    }.apply { println(this.size) }
}


private fun assessDangerousTiles(gridMaxSize: Int, lines: List<Line>, basis: (Line, Coord) -> Boolean) =
    (0..gridMaxSize).map { x ->
        (0..gridMaxSize).map { y ->
            Coord(x, y).run {
                CoordDanger(this, lines.map { basis(it, this) }.count { it })
            }.also { print(it.danger) }
        }.filter { it.danger > 1 }.also { println() }
    }.flatten()

private fun List<String>.extractLines() =
    map { it.split(" -> ") }
        .map {
            Line(
                start = Coord(
                    x = it[0].split(",")[0].toInt(),
                    y = it[0].split(",")[1].toInt()),
                end = Coord(
                    x = it[1].split(",")[0].toInt(),
                    y =  it[1].split(",")[1].toInt()))
        }

data class CoordDanger(val coord: Coord, val danger: Int)
data class Coord(val x: Int, val y: Int)
data class Line(val start: Coord, val end: Coord) {
    fun isFlat() = (isHorizontal() || isVertical())
    fun isVertical() = start.y == end.y
    fun isHorizontal() = start.x == end.x

    fun crossesCoordFlat(coord: Coord) =
        when {
            isHorizontal() && matchesXAxis(coord) && (coord.y in start.y..end.y || coord.y in end.y..start.y) -> true
            isVertical() && matchesYAxis(coord) && (coord.x in start.x..end.x || coord.x in end.x..start.x) -> true
            else -> false
        }

    fun crossesCoordDiagonal(coord: Coord): Boolean {
        if (isFlat()) return false

        val incrementX = getIncrement(start.x, end.x)
        val incrementY = getIncrement(start.y, end.y)
        var (x, y) = start
        while (x != (end.x + incrementX) || (y != (end.y + incrementY))){
            if (coord.x == x && coord.y == y) return true
            x += incrementX
            y += incrementY
        }
        return false
    }

    private fun getIncrement(start: Int, end: Int) =
        when {
            (start < end) -> 1
            (start > end) -> -1
            else -> 0
        }

    private fun matchesYAxis(coord: Coord) = coord.y == start.y
    private fun matchesXAxis(coord: Coord) = coord.x == start.x
}
fun main() {
    part01("Day04_test")
    part01("Day04")
    part02("Day04_test")
    part02("Day04")
}

fun part01(inputFile: String){
    val input = readInput(inputFile)

    BingoGame(buildBingoBoards(input))
        .getWinnerForDraws(buildDraws(input))
        .apply(::println)
        .apply { println(board.sumUnmarked() * winningNumber) }
}

fun part02(inputFile: String){
    val input = readInput(inputFile)

    BingoGame(buildBingoBoards(input))
        .getLastWinner(buildDraws(input))
        .apply(::println)
        .apply { println(board.sumUnmarked() * winningNumber) }
}

data class WinningBoard(val winningNumber: Int, val board: BingoBoard)
data class BingoNumber(val marked: Boolean, val number: Int)

class BingoGame(private val bingoBoards: MutableList<BingoBoard>) {

    tailrec fun getWinnerForDraws(draws: Sequence<Int>) : WinningBoard {
        bingoBoards.forEach {
            it.markNumber(number = draws.first())
            if(it.checkHorizontalWin() || it.checkVerticalWin()){
                return WinningBoard(draws.first(), it)
            }
        }
        return getWinnerForDraws(draws.drop(1))
    }

    tailrec fun getLastWinner(draws: Sequence<Int>): WinningBoard =
        if(bingoBoards.size == 1){
            getWinnerForDraws(draws)
        } else {
            bingoBoards.removeIf {
                it.markNumber(number = draws.first())
                it.checkHorizontalWin() || it.checkVerticalWin()
            }
            getLastWinner(draws.drop(1))
        }

}

class BingoBoard(bingoNumbers: List<List<BingoNumber>>) {
    private val board: Array<Array<BingoNumber>> = Array(5) { Array(5) { BingoNumber(false, 0) } }

    init {
        if (bingoNumbers.size != 5) throw Exception("Bad Board Size")
        bingoNumbers.forEach { if (it.size != 5) throw Exception("Bad Board Size") }

        bingoNumbers.forEachIndexed { rowIndex, it ->
            it.forEachIndexed { columnIndex, bingoNumber ->
                board[rowIndex][columnIndex] = bingoNumber
            }
        }
    }

    fun markNumber(number: Int) {
        board.forEachIndexed { rowIndex, it ->
            it.forEachIndexed { columnIndex, bingoNumber ->
                if(bingoNumber.number == number) {
                    board[rowIndex][columnIndex] = bingoNumber.copy(marked = true)
                }
            }
        }
    }

    fun checkHorizontalWin(): Boolean {
        board.forEach {
            if(it.filter { it.marked }.size == 5) {
                return true
            }
        }
        return false
    }

    fun checkVerticalWin(): Boolean {
        (0..4).forEach { rowIndex ->
            var columnWins = true
            board.forEach {
                if(!it[rowIndex].marked) columnWins = false
            }
            if(columnWins) return true
        }
        return false
    }

    fun sumUnmarked(): Int = board.flatten().filter { !it.marked }.sumOf { it.number }
}

private fun buildDraws(input: List<String>) = input[0].split(",").map { it.toInt() }.asSequence()

private fun buildBingoBoards(input: List<String>) =
    input.asSequence()
         .drop(2)
         .filter { it != "" }                                          //Remove Blank Lines
         .map { it.filterIndexed { index, _ -> (index+1) % 3 != 0 } }  //Remove Gaps Between Numbers
         .map { it.replace(' ', '0') }                //Replace remaining gaps with 0s
         .map { it.chunked(2) }                                   //Group Numbers
         .map { it.map { BingoNumber(false, it.toInt()) } }     //To BingoNumber
         .chunked(5)                                              //Into Boards
         .map { BingoBoard(it) }                                       //Into BingoBoards
         .toMutableList()

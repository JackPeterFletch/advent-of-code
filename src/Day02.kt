import Command.*

fun main () {
    //Part 1
    readInput("Day02_test")
        .fold(SubmarineState()) { position, instruction ->
            instruction.getSubmarineVector()
                       .applyTo(position)
        }
        .apply { check(depth == 10 && horizontalPosition == 15) }

    readInput("Day02")
        .fold(SubmarineState()) { position, instruction ->
            instruction.getSubmarineVector()
                       .applyTo(position)
        }
        .apply { println(depth * horizontalPosition) }

    //Part 2
    readInput("Day02_test")
        .fold(AdvancedSubmarineState()) { position, instruction ->
            instruction.getSubmarineVector()
                       .applyTo(position)
        }
        .apply { check(depth == 60 && horizontalPosition == 15) }

    readInput("Day02")
        .fold(AdvancedSubmarineState()) { position, instruction ->
            instruction.getSubmarineVector()
                .applyTo(position)
        }
        .apply { println(depth * horizontalPosition) }
}

data class SubmarineState(val depth: Int = 0, val horizontalPosition: Int = 0)
data class AdvancedSubmarineState(val aim: Int = 0, val depth: Int = 0, val horizontalPosition: Int = 0)

enum class Command { forward, down, up }
data class SubmarineVector(val command: Command, val distance: Int) {
    fun applyTo(pos: SubmarineState) =
        when (command) {
            forward -> pos.copy(horizontalPosition = pos.horizontalPosition + distance)
            down -> pos.copy(depth = pos.depth + distance)
            up -> if (pos.depth < distance)
                      pos.copy(depth = 0)
                  else
                      pos.copy(depth = pos.depth - distance)

        }

    fun applyTo(pos: AdvancedSubmarineState) =
        when (command) {
            forward -> pos.copy(horizontalPosition = pos.horizontalPosition + distance,
                                depth = (pos.aim * distance).let { if (pos.depth + it <= 0) 0 else pos.depth + it })
            down -> pos.copy(aim = pos.aim + distance)
            up -> pos.copy(aim = pos.aim - distance)
        }

}

private fun String.getSubmarineVector() =
    split(" ")
        .let { (command, units) ->
            SubmarineVector(Command.valueOf(command), units.toInt())
        }
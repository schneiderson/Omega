package experiments

import omega.model.Grid
import kotlin.math.pow


fun main(args: Array<String>){
    var boardSize: Int = 5
    val maxBoardSize: Int = 10

    var grid = Grid(maxBoardSize - boardSize)

    var rounds = grid.cells.size / 4
    var tilesToPlay = rounds * 4

    println("Rounds: $rounds")
    println("Cells: ${grid.cells.size}")
    println("Tiles to play: $tilesToPlay")

    var theoreticNumberOfTwoGroups = (tilesToPlay / 2) / 2
    var theoreticNumberOfThreeGroups = (tilesToPlay / 2) / 3
    var remainder = (tilesToPlay / 2) % 3
    println("remainder $remainder")

    println("white tiles: ${tilesToPlay/2}")
    println("Max 2-groups: $theoreticNumberOfTwoGroups")
    println("Max 3-groups: $theoreticNumberOfThreeGroups")

    var maxPoints =  maxOf(2.0.pow(theoreticNumberOfTwoGroups), 3.0.pow(theoreticNumberOfThreeGroups) * remainder )

    println("Max points: $maxPoints")


}

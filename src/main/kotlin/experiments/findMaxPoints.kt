package experiments

import omega.model.Grid
import kotlin.math.pow


fun main(args: Array<String>){
    var boardSize = 5
    val maxBoardSize = 10

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

    println("Cluster size-2 points: " + 2.0.pow(theoreticNumberOfTwoGroups))
    println("Cluster size-3 points: " + 3.0.pow(theoreticNumberOfThreeGroups) * (remainder + 1))
    var maxPoints =  maxOf(2.0.pow(theoreticNumberOfTwoGroups), 3.0.pow(theoreticNumberOfThreeGroups) * (remainder + 1) )

    println("Max points: $maxPoints")


}

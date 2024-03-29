package omega.model

import kotlin.math.pow


class State(var grid: Grid = Grid(), var playerTurn: Int = 1, var colorToPlay: Int = 1) {

    var players: Int = 2
    var playerScores = (1..players).map { PlayerScore(it) }

    init {
        calcPlayerScores()
    }

    fun calcPlayerScores(){
        playerScores.forEach { grid.getScore(it) }
    }

    fun playMove(move: CombinedAction): State {
        var newGrid = grid.deepCopy()
        try {
            move.actions.forEach {
                var newCell = newGrid.getCellByCoordinates(it.coordinate)
                if (newCell != null) newCell.cellType = it.cellType
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return State(newGrid, nextPlayersTurn(), nextColorToPlay())
    }

    fun simulateMove(move: CombinedAction){
        try {
            move.actions.forEach {
                var cell = grid.getCellByCoordinates(it.coordinate)
                if (cell != null) {
                    cell.cellType = it.cellType
                    playerTurn = nextPlayersTurn()
                    colorToPlay = nextColorToPlay()
                }else {
                    println("THIS SHOULD NEVER HAPPEN")
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun undoSimulatedMove(move: CombinedAction){
        try {
            move.actions.forEach {
                var cell = grid.getCellByCoordinates(it.coordinate)
                if (cell != null && cell.cellType == it.cellType) {
                    cell.cellType = 0
                    playerTurn = previousPlayersTurn()
                    colorToPlay = previousColorToPlay()
                } else {
                    println("THIS SHOULD NEVER HAPPEN")
                }
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun clone(): State {
        var newGrid = grid.deepCopy()
        return State(newGrid, playerTurn, colorToPlay)
    }

    fun nextPlayersTurn(): Int =
        if (colorToPlay < players)
            playerTurn
        else {
            if (playerTurn < players)
                playerTurn + 1
            else
                1
        }

    fun nextColorToPlay(): Int =
        if (colorToPlay < players)
            colorToPlay + 1
        else
            1

    fun previousPlayersTurn(): Int =
        if(colorToPlay == 1){
            if(playerTurn == 1)
                players
            else
                playerTurn -1
        } else
            playerTurn

    fun previousColorToPlay(): Int =
        if(colorToPlay == 1)
            players
        else
            colorToPlay -1


    fun gameEnd(): Boolean{
        if(playerTurn == 1 && colorToPlay == 1){
            return grid.getFreeCells().size < (players.toDouble().pow(players))
        }
        return false
    }

    fun getLegalActions(): List<CombinedAction>{
        return grid.getFreeCells().map{ CombinedAction.getCombinedAction(Action(it.coordinate, colorToPlay)) }
    }

    fun getLegalCombinedActions(): List<CombinedAction>{
        var combActionList = ArrayList<CombinedAction>()
        var firstAction = grid.getFreeCells().map{ Action(it.coordinate, colorToPlay) }
        firstAction.forEach {
            simulateMove(CombinedAction.getCombinedAction(it))
            grid.getFreeCells().map { cell ->
                combActionList.add(CombinedAction(arrayListOf(it, Action(cell.coordinate, colorToPlay))))
            }
            undoSimulatedMove(CombinedAction.getCombinedAction(it))
        }
        return combActionList
    }

    fun getOpponent(player: Int): Int{
        return if(player == 1) 2 else 1
    }

    fun getRoundsPlayed(): Int{
        var freeCells = grid.getFreeCells().size
        var allCells = grid.cells.size
        var numberOfPlayedTiles = allCells - freeCells

        return numberOfPlayedTiles / 2 * players
    }

}


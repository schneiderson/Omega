package omega.model

import omega.game.Action


class State(var grid: Grid = Grid(), var playerTurn: Int = 1, var colorToPlay: Int = 1) {

    var scores: Array<Int> = arrayOf(0, 0, 0, 0)
    var players: Int = 2

    init {
        calcPlayerScores()
    }

    fun calcPlayerScores(){
        scores = grid.getAllScores()
    }

    fun applyMove(move: Action): State {
        var newGrid = grid.deepCopy()
        try {
            var newCell = newGrid.getCellByCoordinates(move.coordinate)
            if (newCell != null) {
                newCell.cellType = move.cellType
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return State(newGrid, nextPlayersTurn(), nextColorToPlay())
    }

    fun clone(): State {
        var newGrid = grid.deepCopy()
        return State(newGrid)
    }

    fun nextPlayersTurn(): Int{
        if (colorToPlay < players) {
            return playerTurn
        } else {
            if (playerTurn < players) {
                return playerTurn + 1
            } else {
                return 1
            }
        }
    }

    fun nextColorToPlay(): Int{
        if (colorToPlay < players) {
            return colorToPlay + 1
        }
        return 1
    }

    fun gameEnd(): Boolean{
        if(playerTurn == 1 && colorToPlay == 1){
            return grid.getFreeCells().size < (players * 2)
        }
        return false
    }

    fun getLegalActions(): List<Action>{
        return grid.getFreeCells().map{Action(it.coordinate, colorToPlay)}
    }

}


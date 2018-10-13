package omega.model


class State(var grid: Grid = Grid(), var playerTurn: Int = 1, var colorToPlay: Int = 1) {

    var players: Int = 2
    var playerScores = (1..players).map { PlayerScore(it) }

    init {
        calcPlayerScores()
    }

    fun calcPlayerScores(){
        playerScores.forEach { grid.getScore(it) }
    }

    fun playMove(move: Action): State {
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

    fun simulateMove(move: Action){
        try {
            var cell = grid.getCellByCoordinates(move.coordinate)
            if (cell != null) {
                cell.cellType = move.cellType
                playerTurn = nextPlayersTurn()
                colorToPlay = nextColorToPlay()
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }

    fun undoSimulatedMove(move: Action){
        try {
            var cell = grid.getCellByCoordinates(move.coordinate)
            if (cell != null && cell.cellType == move.cellType) {
                cell.cellType = 0
                playerTurn = previousPlayersTurn()
                colorToPlay = previousColorToPlay()
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
            return grid.getFreeCells().size < (players * 2)
        }
        return false
    }

    fun getLegalActions(): List<Action>{
        return grid.getFreeCells().map{ Action(it.coordinate, colorToPlay) }
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


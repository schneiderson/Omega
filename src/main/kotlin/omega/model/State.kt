package omega.model


class State(var grid: Grid = Grid(), var playerTurn: Int = 1, var colorToPlay: Int = 1) {

    var scores: Array<Int> = arrayOf(0, 0, 0, 0)

    init {
        calcPlayerScores()
    }

    fun calcPlayerScores(){
        scores = grid.getAllScores()
    }

    fun applyMove(cell: Cell, cellType: Int, nextPlayersTurn: Int, nextColorToPlay: Int): State {
        var newGrid = grid.deepCopy()
        try {
            var newCell = newGrid.getCellByIndex(cell.index)
            if (newCell != null) {
                newCell.cellType = cellType
            }
        } catch (e: Exception) {
            println(e.message)
        }
        return State(newGrid, nextPlayersTurn, nextColorToPlay)
    }

    fun clone(): State {
        var newGrid = grid.deepCopy()
        return State(newGrid)
    }

}


package omega.model


class State(var grid: Grid = Grid(), var playerTurn: Int = 0, var colorToPlay: Int = 1){

    fun applyMove(cell: Cell, cellType: Int): State{
        cell.cellType = cellType
        var newGrid = grid.deepCopy()
        try {
            var newCell = newGrid.getCellByIndex(cell.index)
            if(newCell != null){
                newCell.cellType = cellType
            }
        }catch (e: Exception){
            println(e.message)
        }
        return State(newGrid)
    }

    fun clone(): State{
        var newGrid = grid.deepCopy()
        return State(newGrid)
    }

}


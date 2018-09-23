package omega.model

import omega.util.Coordinate

class Grid(val disabledRows: Int = 4, val cells: ArrayList<Cell> = ArrayList()) {
    val gridHeight = 19
    val gridWidth = 19

    init {
        if (cells.size < 1) {
            initialEmptyGrid()
        }
    }

    private fun initialEmptyGrid() {
        var index = 0
        for (j in 0.until(gridWidth)) {
            for (i in 0.until(gridHeight)) {
                var coordinate = Coordinate(i, j)
                var cellType = getCellType(coordinate)

                var currIndex = -1
                if (cellType > -1) {
                    currIndex = index
                    index++
                }

                var cell = Cell(coordinate, cellType, currIndex)

                cells.add(cell)
            }
        }
    }

    private fun getCellType(coordinate: Coordinate): Int {
        var invisCells = 0
        if (coordinate.getY() < 9) {
            invisCells = 9 - coordinate.getY()
        } else if (coordinate.getY() > 9) {
            invisCells = coordinate.getY() - 9
        }

        var leftInvis = invisCells / 2 + invisCells % 2
        var rightInvis = invisCells / 2

        if (coordinate.getX() < leftInvis
                || coordinate.getX() > gridWidth - 1 - rightInvis) {
            // invisible
            return -2
        }

        if (coordinate.getY() < disabledRows
                || gridHeight - coordinate.getY() - 1 < disabledRows
                || coordinate.getX() < leftInvis + disabledRows
                || coordinate.getX() > gridWidth - 1 - (rightInvis + disabledRows)
        ) {
            // disabled
            return -1
        }

        return 0
    }

    fun getCellByIndex(index: Int): Cell? {
        var cellFound: Cell? = null
        for (cell in cells) {
            if (cell.index == index) {
                cellFound = cell
            }
        }

        if (cellFound == null) {
            throw Exception("Cell with index $index doesn't exist")
        }

        return cellFound
    }

    fun getCellByCoordinates(c: Coordinate): Cell? {
        var cellFound: Cell? = null
        for (cell in cells){
            if(cell.coordinate == c){
                cellFound = cell
            }
        }

        if (cellFound == null) {
            throw Exception("Cell with coordinates $c doesn't exist")
        }

        return cellFound
    }

    fun getCellByAxialCoordinate(x: Int, y: Int): Cell? {
        var cellFound: Cell? = null
        for (cell in cells){
            var axialCoordinate = cell.coordinate.getAxialCoordinates()
            if(axialCoordinate.first == x && axialCoordinate.second == y){
                cellFound = cell
            }
        }

        if (cellFound == null) {
            throw Exception("Cell with axial coordinates X: x doesn't exist")
        }

        return cellFound
    }

    fun getCellNeighbours(cell: Cell): ArrayList<Cell>{
        var axC = cell.coordinate.getAxialCoordinates()
        var neighbours: ArrayList<Cell> = ArrayList()

        for(i in 1..6){
            var axOff = getAxialOffset(i, axC.first, axC.second)
            var neighbour: Cell? = null
            try {
                neighbour = getCellByAxialCoordinate(axOff.first, axOff.second)
            } catch(e: java.lang.Exception){
                // do nothing
            }
            if(neighbour != null){
                neighbours.add(neighbour)
            }
        }
        return neighbours
    }

    fun getCellCluster(cell: Cell, ignoreCells: ArrayList<Cell> = ArrayList()): ArrayList<Cell>{
        var neighbours = getCellNeighbours(cell)
        var cluster = ArrayList<Cell>()

        if(!ignoreCells.contains(cell)){
            cluster.add(cell)
            for(n in neighbours){
                if(cell.cellType == n.cellType && !ignoreCells.contains(n)) {
                    var newIgnoreCells = ArrayList<Cell>()
                    newIgnoreCells.addAll(ignoreCells)
                    newIgnoreCells.add(cell)
                    var newCells = getCellCluster(n, newIgnoreCells)

                    cluster.addAll(newCells)
                    ignoreCells.addAll(newCells)
                    ignoreCells.add(n)
                }
            }
        }
        return cluster
    }

    fun getAllScores(): Array<Int>{
        var scores = arrayOf(0, 0, 0, 0)
        for(i in 1..4){
            scores[i-1] = getPlayerScores(i)
        }
        return scores
    }

    fun getPlayerScores(player: Int): Int{
        var clusters = ArrayList<ArrayList<Cell>>()
        var visited = ArrayList<Cell>()

        for(cell in cells){
            if(cell.cellType == player && !visited.contains(cell)){

                var cluster = getCellCluster(cell, visited)
                if(cluster.size > 0){
                    visited.addAll(cluster)
                    clusters.add(cluster)
                }
                visited.add(cell)
            }
        }

        var score = 0
        for(c in clusters){
            if(score == 0) score = c.size
            else score *= c.size
        }

        return score
    }

    fun getFreeCells(): ArrayList<Cell>{
        var freeCells = ArrayList<Cell>()
        for(cell in cells){
            if(cell.cellType == 0){
                freeCells.add(cell)
            }
        }
        return freeCells
    }

    fun getAxialOffset(direction: Int, x: Int, y: Int): Pair<Int, Int>{
        var offset = Pair(x, y)
        when(direction){
            1 -> offset = Pair(x, y-1)
            2 -> offset = Pair(x+1, y-1)
            3 -> offset = Pair(x+1, y)
            4 -> offset = Pair(x, y+1)
            5 -> offset = Pair(x-1, y)
            6 -> offset = Pair(x-1, y+1)
        }
        return offset
    }

    fun deepCopy(): Grid {
        //
        var newCells: ArrayList<Cell> = ArrayList()
        for (cell in cells) {
            newCells.add(cell.copy())
        }
        return Grid(disabledRows, newCells)
    }




}
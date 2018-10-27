package omega.model

import omega.util.Coordinate

class Grid(
        val disabledRows: Int = 4,
        val cells: ArrayList<Cell> = ArrayList(),
        val disabledCells: ArrayList<Cell> = ArrayList())
    {
    val gridHeight = 19
    val gridWidth = 19

    init {
        if (cells.size < 1) {
            initialEmptyGrid()
        }
    }

    private fun initialEmptyGrid() {
        var index = 0
        0.until(gridWidth).forEach { j ->
            0.until(gridHeight).forEach { i ->
                var coordinate = Coordinate(i, j)
                var cellType = getCellTypeByCoordinate(coordinate)

                var currIndex = -1
                if (cellType > -1) {
                    currIndex = index
                    index++
                }

                var cell = Cell(coordinate, cellType, currIndex)

                if(cellType < 0) disabledCells.add(cell) else cells.add(cell)
            }
        }
    }

    private fun getCellTypeByCoordinate(coordinate: Coordinate): Int {
        var invisCells = 0// disabled
        // invisible
        when {
            coordinate.getY() < 9 -> invisCells = 9 - coordinate.getY()
            coordinate.getY() > 9 -> invisCells = coordinate.getY() - 9
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
        cells.forEach { cell ->
            if (cell.index == index) cellFound = cell
        }

        if (cellFound == null) throw Exception("Cell with index $index doesn't exist")

        return cellFound
    }

    fun getCellByCoordinates(c: Coordinate): Cell? {
        var cellFound: Cell? = null
        cells.forEach { cell ->
            if(cell.coordinate.equals(c)) cellFound = cell
        }

        if (cellFound == null) throw Exception("Cell with coordinates $c doesn't exist")

        return cellFound
    }

    fun getCellByAxialCoordinate(x: Int, y: Int): Cell? {
        var cellFound: Cell? = null
        cells.forEach { cell ->
            var axialCoordinate = cell.coordinate.getAxialCoordinates()
            if(axialCoordinate.first == x && axialCoordinate.second == y) cellFound = cell
        }

        if (cellFound == null) throw Exception("Cell with axial coordinates X: x doesn't exist")

        return cellFound
    }

    fun getCellNeighbours(cell: Cell): ArrayList<Cell>{
        var axC = cell.coordinate.getAxialCoordinates()
        var neighbours: ArrayList<Cell> = ArrayList()

        (1..6).forEach { i ->
            var axOff = getAxialOffset(i, axC.first, axC.second)
            var neighbour: Cell? = null
            try {
                neighbour = getCellByAxialCoordinate(axOff.first, axOff.second)
            } catch(e: java.lang.Exception){
                // do nothing
            }
            if(neighbour != null) neighbours.add(neighbour)
        }
        return neighbours
    }

    fun getCellCluster(cell: Cell, ignoreCells: ArrayList<Cell> = ArrayList()): ArrayList<Cell>{
        var neighbours = getCellNeighbours(cell)
        var cluster = ArrayList<Cell>()

        if(!ignoreCells.contains(cell)){
            cluster.add(cell)
            neighbours.forEach { n ->
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

    fun getScore(playerScore: PlayerScore){
        getPlayerScores(playerScore)
    }

    fun getPlayerScores(playerScore: PlayerScore){
        var clusters = ArrayList<ArrayList<Cell>>()
        var visited = ArrayList<Cell>()

        cells.forEach { cell ->
            if(cell.cellType == playerScore.player && !visited.contains(cell)){

                var cluster = getCellCluster(cell, visited)
                if(cluster.size > 0){
                    visited.addAll(cluster)
                    clusters.add(cluster)
                }
                visited.add(cell)
            }
        }

        var score = 0
        clusters.forEach { c ->
            if(score == 0) score = c.size
            else score *= c.size
        }

        playerScore.clusters = clusters
        playerScore.score = score
    }

    fun getFreeCells(): ArrayList<Cell>{
        var freeCells = ArrayList<Cell>()
        cells.forEach { cell ->
            if(cell.cellType == 0) freeCells.add(cell)
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
        var newCells: ArrayList<Cell> = ArrayList()
        cells.forEach { cell ->
            newCells.add(cell.copy())
        }
        return Grid(disabledRows, newCells, disabledCells)
    }

    fun getCellsForUI(): ArrayList<Cell>{
        var allCells = ArrayList<Cell>()
        allCells.addAll(cells)
        allCells.addAll(disabledCells)
        return allCells
    }


}
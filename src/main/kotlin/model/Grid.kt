package omega.model

import omega.util.Coordinate

class Grid (val cells: ArrayList<Cell> = ArrayList()){
    val gridHeight = 19
    val gridWidth = 19
    var disabledRows = 4

    init {
        if(cells.size < 1){
            initialEmptyGrid()
        }
    }

    fun initialEmptyGrid(){
        var index = 0
        for (j in 0.until(gridWidth)) {
            for (i in 0.until(gridHeight)) {
                var coordinate = Coordinate(i, j)
                var cellType = getCellType(coordinate)

                var currIndex = -1
                if(cellType > -1){
                    currIndex = index
                    index++
                }

                var cell = Cell(coordinate, cellType, currIndex)

                cells.add(cell)
            }
        }
    }

    fun getCellType(coordinate: Coordinate): Int {
        var invisCells = 0
        if (coordinate.getY() < 9){
            invisCells = 9 - coordinate.getY()
        } else if(coordinate.getY() > 9){
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
        for(cell in cells){
            if(cell.index == index){
                cellFound = cell
            }
        }

        if(cellFound == null){
            throw Exception("Cell with index $index doesn't exist")
        }

        return cellFound
    }

    fun deepCopy(): Grid{
        //
        var newCells: ArrayList<Cell> = ArrayList()
        for(cell in cells){
            newCells.add(cell.copy())
        }
        return Grid(newCells)
    }


}
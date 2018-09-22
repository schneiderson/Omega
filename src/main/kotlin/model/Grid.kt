package omega.model

import omega.model.Cell
import omega.util.Coordinate
import java.lang.Math.abs

class Grid {
    val gridHeight = 19
    val gridWidth = 19
    var disabledRows = 4

    val cells: ArrayList<Cell> = ArrayList<Cell>()

    init{
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


}
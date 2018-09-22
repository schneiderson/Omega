package omega.model

import omega.util.Coordinate

data class Cell(var coordinate: Coordinate, var cellType: Int, var index: Int){
    var visibility: Boolean = cellType > -2
}
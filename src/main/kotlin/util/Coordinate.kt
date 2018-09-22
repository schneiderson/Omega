package omega.util

data class Coordinate(private var x: Int, private var y: Int){

    fun getX(): Int {
        return x
    }
    fun getY(): Int {
        return y
    }

    fun getCubeCoordinates(): Cube{
        var c_x = (x - (y - (y % 2)) / 2) - 5
        var c_z = y - 9
        var c_y = -c_x - c_z

        return Cube(c_x, c_y, c_z)
    }

    fun getAxialCoordinates(): Pair<Int, Int>{
        var cube = getCubeCoordinates()
        return Pair(cube.x, cube.z)
    }
}
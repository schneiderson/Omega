package omega.util

import kotlin.math.abs

/**
 * Coordinate helper class
 *
 * has an x and y coordinate.
 * Also enables the conversion in cubic and axial coordinates
 */
data class Coordinate(private var x: Int, private var y: Int) {

    fun getX(): Int {
        return x
    }

    fun getY(): Int {
        return y
    }

    fun getCubeCoordinates(): Cube {
        var cX = (x - (y - (y % 2)) / 2) - 5
        var cZ = y - 9
        var cY = -cX - cZ

        return Cube(cX, cY, cZ)
    }

    fun getAxialCoordinates(): Pair<Int, Int> {
        var cube = getCubeCoordinates()
        return Pair(cube.x, cube.z)
    }

    fun equals(c: Coordinate): Boolean {
        if(x == c.x && y == c.y){
            return true
        }
        return false
    }

    fun getDistanceFromCoordinate(coordinate: Coordinate): Int {
        var cube1 = getCubeCoordinates()
        var cube2 = coordinate.getCubeCoordinates()
        return maxOf(abs(cube1.x - cube2.x), abs(cube1.y - cube2.x), abs(cube1.z - cube2.z))
    }

    override fun toString(): String {
        return "X: $x, Y: $y"
    }
}
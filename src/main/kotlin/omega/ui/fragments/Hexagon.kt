package omega.ui.fragments

import omega.game.Styles
import omega.controller.ViewController
import omega.model.Cell
import javafx.geometry.Point2D
import javafx.scene.shape.Polygon
import javafx.scene.input.MouseEvent
import tornadofx.*

/**
 * Hexagon tile class
 *
 * (UI fragment)
 */
class Hexagon(var cell: Cell, var size: Double = 20.0) : Polygon() {
    private var strokeSize: Double = 1.0

    private var controller: ViewController = ViewController()

    companion object {
        const val INDENTATION = 50
    }

    init {
        addEventFilter(MouseEvent.MOUSE_CLICKED) { e ->
            controller.cellClick(e, cell)
        }
        addEventFilter(MouseEvent.MOUSE_ENTERED) { e ->
            if (cell.cellType == 0) {
                strokeWidth = this.strokeSize + 4
            }
            controller.cellHover(e, cell)
        }
        addEventFilter(MouseEvent.MOUSE_EXITED) { e ->
            if (cell.cellType > -1) {
                strokeWidth = this.strokeSize
            }
        }
        addClass(Styles.hexagon)
        repaint(size)
    }

    // rendering function for the Hexagon
    private fun repaint(size: Double) {
        var corners: ArrayList<Double> = getCorners(size)
        points.setAll(corners)
        if (!cell.visibility) {
            isVisible = false
        } else {
            stroke = c("BLACK")
            when {
                // disabled
                cell.cellType == -1 -> addClass(Styles.hexDisabled)
                // free cell
                cell.cellType == 0 -> addClass(Styles.hexFree)
                // player 1
                cell.cellType == 1 -> fill = c("WHITE")
                // player 2
                cell.cellType == 2 -> {
                    fill = c("BLACK")
                    stroke = c("WHITE")
                }
                // player 3
                cell.cellType == 3 ->
                    fill = c("#A30049")
                // player 4
                cell.cellType == 4 -> fill = c("#33B800")
            }
        }
        strokeWidth = this.strokeSize
    }

    fun refresh() {
        repaint(size)
    }

    fun changeSize(size: Double) {
        this.size = size
    }

    fun getCorners(size: Double): ArrayList<Double> {
        var corners: ArrayList<Double> = ArrayList()
        var center: Point2D = calcCenter()
        for (i in 0..5) {
            var degree: Int = 60 * i - 30
            var rad = Math.PI / 180 * degree
            corners.add(center.x + size * Math.cos(rad))
            corners.add(center.y + size * Math.sin(rad))
        }
        return corners
    }

    fun calcCenter(): Point2D {
        var width = Math.sqrt(3.0) * size
        var x = INDENTATION + if (cell.coordinate.getY() % 2 == 1) cell.coordinate.getX() * width + width / 2 else cell.coordinate.getX() * width
        var y = INDENTATION + cell.coordinate.getY() * size * 3 / 2
        return Point2D(x, y)
    }
}

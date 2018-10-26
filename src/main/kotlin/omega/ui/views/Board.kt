package omega.ui.views

import javafx.scene.control.Label
import omega.game.Styles
import omega.controller.ViewController
import omega.ui.fragments.Hexagon
import javafx.scene.input.MouseEvent
import tornadofx.*
import javafx.scene.layout.Pane

/**
 * Board view containing the hex-grid
 */
class BoardView : View("Board") {
    override val root = Pane()
    private val controller: ViewController by inject()
    private lateinit var hexes: List<Hexagon>

    init {
        with(root) {
            minWidth = 760.0
            minHeight = 640.0
            addClass(Styles.board)

            root.addEventFilter(MouseEvent.MOUSE_MOVED) { e ->
                controller.displayPosition(e)
            }

            hexes = controller.gameManager.getGrid().getCellsForUI().map { Hexagon(it) }

            root.children.addAll(hexes)

        }
    }

    fun refreshBoard() {
        with(root){
            root.children.removeAll(hexes)
            hexes = controller.gameManager.getGrid().getCellsForUI().map { Hexagon(it) }
            root.children.addAll(hexes)
        }
    }

    fun gameOver() {
        with(root){
            root.children.add(Label("Game Over"))
        }
    }

}
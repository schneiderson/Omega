package omega.view

import omega.app.Styles
import omega.controller.ViewController
import omega.ui.Hexagon
import javafx.scene.input.MouseEvent
import tornadofx.*
import javafx.scene.layout.Pane

class BoardView: View("Board"){
    override val root = Pane()
    private val controller: ViewController by inject()
    private lateinit var hexes: List<Hexagon>

    init {
        with(root){
            minWidth = 760.0
            minHeight = 640.0
            addClass(Styles.board)

            root.addEventFilter(MouseEvent.MOUSE_MOVED) { e ->
                controller.displayPosition(e)
            }
            
            hexes = controller.gameManager.getGrid().cells.map { Hexagon(it) }

            root.children.addAll(hexes)

        }
    }

    fun refreshBoard(){
       hexes.forEach {
           it.refresh()
       }
    }

}
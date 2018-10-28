package omega.ui.views

import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import omega.controller.ViewController
import omega.game.Styles
import tornadofx.*

/**
 * Main omega view
 */
class OptionsView : View("Omega") {
    private val controller: ViewController by inject()

    lateinit var cb1: ComboBox<String>
    lateinit var tf: TextField
    lateinit var cb2: ComboBox<Int>

    override val root = form {
        fieldset(title) {
            field("Opponent") {
                cb1 = combobox(null, controller.getOpponentList())
                cb1.value = controller.getOpponent()
            }
            field("Search Depth") {
                tf = textfield(controller.getSearchDepth().toString())
            }
            field("Game board size") {
                cb2 = combobox(null, controller.getBoardSizes())
                cb2.value = controller.getBoardSize()
            }
            button("Save") {
                action {
                    controller.setBoardSize(cb2.value)
                    controller.setSearchDepth(tf.text.toInt())
                    controller.setOpponent(cb1.value)
                    replaceWith(MainView::class, null)
                }
            }
        }
    }
}
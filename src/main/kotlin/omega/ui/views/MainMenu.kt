package omega.ui.views

import javafx.scene.layout.VBox

import omega.game.Styles
import omega.controller.ViewController
import tornadofx.*

/**
 * Main menu on entry screen
 */
class MainMenuView : View("Omega") {
    private val controller: ViewController by inject()
    override val root = VBox()

    init {
        with(root) {
            style = ("-fx-background-color: #ffffff")
            button("play") {
                action { controller.showScreen("game") }
                addClass(Styles.menuButton)
            }
            button("options") {
                action { controller.showScreen("options") }
                addClass(Styles.menuButton)
            }
            button("exit") {
                action { System.exit(0) }
                addClass(Styles.menuButton)
            }
        }
    }
}
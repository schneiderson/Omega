package omega.view

import javafx.scene.layout.VBox

import omega.app.Styles
import omega.controller.ViewController
import tornadofx.*

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
            button("how-to") {
                action { controller.showScreen("how-to") }
                addClass(Styles.menuButton)
            }
            button("exit") {
                action { System.exit(0) }
                addClass(Styles.menuButton)
            }
        }
    }
}
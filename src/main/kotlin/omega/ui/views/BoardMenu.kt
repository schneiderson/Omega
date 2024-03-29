package omega.ui.views

import omega.game.Styles
import javafx.scene.control.Label
import omega.controller.ViewController
import tornadofx.*

/**
 * Board menu
 */
class BoardMenuView : View("Board menu") {
    private val controller: ViewController by inject()

    var status: Label = Label("status")
    var offset: Label = Label("offset")
    var axial: Label = Label("axial")
    var cube: Label = Label("cube")
    var label: Label = Label(title)
    var undoButton = button("Undo Action") {
        action { controller.undoMove() }
        addClass(Styles.boardMenuButton)
        isDisable = true
    }

    var exitButton = button("Exit") {
        action {
            controller.gameManager.resetGame()
            find(GameView::class).replaceWith(MainView::class)
        }
        addClass(Styles.boardMenuButton)
    }

    override val root = hbox {

        label.addClass("heading")
        status.addClass(Styles.statsText)
        offset.addClass(Styles.statsText)
        axial.addClass(Styles.statsText)
        cube.addClass(Styles.statsText)


        children.addAll(label, undoButton, exitButton, status, offset, axial, cube)
    }
}
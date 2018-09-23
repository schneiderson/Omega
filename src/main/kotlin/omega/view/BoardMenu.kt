package omega.view

import omega.app.Styles
import javafx.scene.control.Label
import omega.controller.ViewController
import tornadofx.*

class BoardMenuView : View("Board menu") {
    private val controller: ViewController by inject()

    var status: Label = Label("status")
    var offset: Label = Label("offset")
    var axial: Label = Label("axial")
    var cube: Label = Label("cube")
    var label: Label = Label(title)
    var undoButton = button("Undo Move") {
        action { controller.undoMove() }
        addClass(Styles.menuButton)
        isDisable = true
    }

    override val root = hbox {

        label.addClass("heading")
        status.addClass(Styles.statsText)
        offset.addClass(Styles.statsText)
        axial.addClass(Styles.statsText)
        cube.addClass(Styles.statsText)


        children.addAll(label, undoButton, status, offset, axial, cube)
    }
}
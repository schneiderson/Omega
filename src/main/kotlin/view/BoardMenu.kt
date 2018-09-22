package omega.view

import omega.app.Styles
import javafx.scene.control.Label
import javafx.scene.text.Text
import tornadofx.*

class BoardMenuView : View("Board menu") {

    var status: Label = Label("status")
    var offset: Label = Label("offset")
    var axial: Label = Label("axial")
    var cube: Label = Label("cube")
    var label: Label = Label(title)

    override val root = hbox {
        label.addClass("heading")
        status.addClass(Styles.statsText)
        offset.addClass(Styles.statsText)
        axial.addClass(Styles.statsText)
        cube.addClass(Styles.statsText)


        children.addAll(label, status, offset, axial, cube)
    }
}
package omega.view

import omega.app.Styles
import tornadofx.*

class SecondView : View("Hello SecondView") {

    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }
    }
}
package omega.view

import omega.app.Styles
import tornadofx.*

class FirstView : View("Hello FirstView") {

    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }

    }
}
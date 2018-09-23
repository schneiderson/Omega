package omega.view

import omega.app.Styles
import tornadofx.*

class FirstView : View("Omega board game") {

    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }

    }
}
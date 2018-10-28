package omega.ui.views

import omega.game.Styles
import tornadofx.*

class TopView : View("Omega board game") {

    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }

    }
}
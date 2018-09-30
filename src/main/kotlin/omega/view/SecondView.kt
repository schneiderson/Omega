package omega.view

import omega.game.Styles
import tornadofx.*

class SecondView : View("Hello SecondView") {

    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }
    }
}
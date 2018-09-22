package omega.view

import tornadofx.*

class MainView : View("Hello TornadoFX") {
    override val root = borderpane {
        prefHeight = 700.0
        prefWidth = 800.0
        top(FirstView::class)
        center(MenuView::class)
    }
}
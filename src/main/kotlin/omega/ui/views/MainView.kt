package omega.ui.views

import tornadofx.*

class MainView : View("Omega") {
    override val root = borderpane {
        prefHeight = 700.0
        prefWidth = 800.0
        top(FirstView::class)
        center(MainMenuView::class)
    }
}
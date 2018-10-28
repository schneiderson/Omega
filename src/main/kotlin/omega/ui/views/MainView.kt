package omega.ui.views

import tornadofx.*

/**
 * Main omega view
 */
class MainView : View("Omega") {
    override val root = borderpane {
        prefHeight = 700.0
        prefWidth = 800.0
        top(TopView::class)
        center(MainMenuView::class)
    }
}
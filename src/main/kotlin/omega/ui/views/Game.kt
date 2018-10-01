package omega.ui.views

import tornadofx.*

class GameView : View("GameView") {
    override val root = borderpane {
        prefHeight = 700.0
        prefWidth = 800.0
        center(BoardView::class)
        bottom(BoardMenuView::class)
        right(GameStatsView::class)
    }


}
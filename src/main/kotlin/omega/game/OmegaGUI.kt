package omega.game

import omega.ui.views.MainView
import javafx.application.Application
import javafx.stage.Stage
import tornadofx.*

class OmegaGUI : App(MainView::class, Styles::class) {
    var gameManager: GameManager = GameManager

    override fun start(stage: Stage) {
        stage.minHeight = 700.0
        stage.minWidth = 900.0
        super.start(stage)
    }

}

fun main(args: Array<String>) {
    Application.launch(OmegaGUI::class.java, *args)
}
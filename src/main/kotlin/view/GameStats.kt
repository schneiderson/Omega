package omega.view

import javafx.scene.control.Label
import javafx.scene.text.Text
import tornadofx.*

class GameStatsView: View("Game Stats") {
    var turn: Label = Label("Turn")
    var player1Score: Text = Text("0")
    var player2Score: Text = Text("0")
    var player3Score: Text = Text("0")
    var player4Score: Text = Text("0")

    override val root = vbox {
        minWidth = 200.0
        children.addAll(turn, player1Score, player2Score)
    }

}
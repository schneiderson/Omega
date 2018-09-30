package omega.view

import javafx.scene.control.Label
import javafx.scene.text.Text
import omega.game.Styles
import omega.controller.ViewController
import omega.model.Cell
import omega.ui.Hexagon
import omega.util.Coordinate
import tornadofx.*

class GameStatsView : View("Game Stats") {
    private val controller: ViewController by inject()

    var currentPlayer: Label = Label("Current Player: ")
    var player: Text = Text("1 (White)")
    var currTile: Label = Label("Next Tile: ")
    var hexagon = Hexagon(Cell(Coordinate(1,1), 1, 1))
    var scores: Label = Label("Current Score: ")
    var player1Score: Text = Text("Player 1 (White): 0")
    var player2Score: Text = Text("Player 2 (Black): 0")
    var player3Score: Text = Text("Player 3 (Red): 0")
    var player4Score: Text = Text("Player 4 (Green): 0")

    override val root = vbox {
        minWidth = 200.0
        addClass(Styles.board)

        children.addAll(currentPlayer, player, currTile, hexagon, scores, player1Score, player2Score)

        if(controller.gameManager.getNumberOfPlayers() > 2){
            children.add(player3Score)
        }

        if(controller.gameManager.getNumberOfPlayers() > 3){
            children.add(player4Score)
        }
    }

}
package omega.controller

import omega.app.GameManager
import omega.model.Cell
import omega.model.Grid
import omega.view.*
import tornadofx.*
import javafx.scene.input.MouseEvent


class ViewController: Controller() {
    private val menu: MenuView by inject()
    private val game: GameView by inject()
    private val boardMenu: BoardMenuView by inject()
    private val board: BoardView by inject()
    private val gameManager: GameManager = GameManager
    lateinit var grid: Grid
//    private val howTo: HowToView by inject()
//    private val gameOver: GameOverView by inject()


    fun showScreen(screen: String) = when(screen){
        "menu" -> FX.primaryStage.scene.root = menu.root
        "game" -> {
            grid = gameManager.getGrid()
            FX.primaryStage.scene.root = game.root
        }
//        "how-to" -> FX.primaryStage.scene.root = howTo.root
//        "game-over" -> FX.primaryStage.scene.root = gameOver.root
        else -> { System.exit(1); throw NotImplementedError("Requested screen doesn't exist.")}
    }

    fun newGame(){
        println("new game")

    }

    fun displayPosition(event: MouseEvent){
        boardMenu.status.text = "X = ${event.x}, Y = ${event.y}"
    }

    fun cellClick(event: MouseEvent, cell: Cell){
        var curCellType = cell.cellType
        if (curCellType > -1){
            if(curCellType == 4){
                cell.cellType = 0
            } else{
                cell.cellType = curCellType + 1
            }
        }
        board.refreshBoard()
    }

    fun cellHover(event: MouseEvent, cell: Cell){
        boardMenu.offset.text = "[Offset: x=${cell.coordinate.getX()}, y=${cell.coordinate.getY()}] [Index: ${cell.index}]"
        boardMenu.axial.text = "[Axial: x=${cell.coordinate.getAxialCoordinates().first}, y=${cell.coordinate.getAxialCoordinates().second}]"
        boardMenu.cube.text = "[Cube: x=${cell.coordinate.getCubeCoordinates().x}, y=${cell.coordinate.getCubeCoordinates().y}, z=${cell.coordinate.getCubeCoordinates().z}]"
    }
}
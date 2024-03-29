package omega.controller


import javafx.application.Platform
import omega.game.GameManager
import omega.model.Cell
import tornadofx.*
import javafx.scene.input.MouseEvent
import omega.ai.Agent
import omega.model.State
import omega.ui.views.*
import omega.util.StateChangeListener


class ViewController : Controller(), StateChangeListener {

    private val menu: MainMenuView by inject()
    private val game: GameView by inject()
    private val boardMenu: BoardMenuView by inject()
    private val gameStats: GameStatsView by inject()
    private val board: BoardView by inject()
    private val options: OptionsView by inject()
    val gameManager: GameManager = GameManager

    init {
        gameManager.addStateChangeListener(this)
    }

    fun showScreen(screen: String) {
        when (screen) {
            "menu" -> FX.primaryStage.scene.root = menu.root
            "game" -> {
                FX.primaryStage.scene.root = game.root
            }
            "options" -> FX.primaryStage.scene.root = options.root
            else -> {
                System.exit(1); throw NotImplementedError("Requested screen doesn't exist.")
            }
        }
    }

    override fun onValueChanged(state: State) {
        // Platform.runLater is required to avoid thread interferences
        Platform.runLater {
            refreshUi()
        }
    }

    fun setOpponent(agent: String){
        gameManager.setOpponent(agent)
    }

    fun getBoardSizes(): List<Int>{
        return listOf(3,4,5,6,7,8,10)
    }

    fun getOpponent(): String{
        return gameManager.agents[1].agentName.split(" ")[0]
    }

    fun getOpponentList(): List<String>{
        return listOf("MiniMaxAgent", "MiniMaxABAgent", "MiniMaxTTAgent", "MiniMaxIDAgent", "MiniMaxTTMOAgent", "MiniMaxTTNMAgent")
    }

    fun getSearchDepth(): Int{
        return gameManager.maxDepth
    }

    fun setSearchDepth(depth : Int) {
        gameManager.setSearchDepth(depth)
    }

    fun setBoardSize(size: Int){
        gameManager.changeBoardSize(size)
    }

    fun getBoardSize(): Int{
        return gameManager.boardSize
    }

    fun displayPosition(event: MouseEvent) {
        boardMenu.status.text = "X = ${event.x}, Y = ${event.y}"
    }

    fun cellClick(event: MouseEvent, cell: Cell) {
        if(cell.cellType == 0 && gameManager.currentPlayerIsHuman() && !gameManager.gameEnd()){
            gameManager.performMove(cell)
        }
    }

    fun undoMove(){
        gameManager.undoMove()
    }


    fun cellHover(event: MouseEvent, cell: Cell) {
        boardMenu.offset.text = "[Offset: x=${cell.coordinate.getX()}, y=${cell.coordinate.getY()}] [Index: ${cell.index}]"
        boardMenu.axial.text = "[Axial: x=${cell.coordinate.getAxialCoordinates().first}, y=${cell.coordinate.getAxialCoordinates().second}]"
        boardMenu.cube.text = "[Cube: x=${cell.coordinate.getCubeCoordinates().x}, y=${cell.coordinate.getCubeCoordinates().y}, z=${cell.coordinate.getCubeCoordinates().z}]"
    }


    fun refreshUi(){

        if(gameManager.gameEnd()){
            board.gameOver()
        }

        board.refreshBoard()
        // set undo button
        boardMenu.undoButton.isDisable = !gameManager.undoable()
        // set current game stats
        gameStats.hexagon.cell.cellType = gameManager.colorToPlay()
        gameStats.hexagon.refresh()
        var player = gameManager.getCurrentPlayer()
        gameStats.player.text = player.toString() + " (" + gameManager.playerColor[player] + ")"

        gameStats.player1Score.text = "Player 1 " + " (" + gameManager.playerColor[1] + "): " + gameManager.getPlayerScore(1)
        gameStats.player2Score.text = "Player 2 " + " (" + gameManager.playerColor[2] + "): " + gameManager.getPlayerScore(2)
        gameStats.player3Score.text = "Player 3 " + " (" + gameManager.playerColor[3] + "): " + gameManager.getPlayerScore(3)
        gameStats.player4Score.text = "Player 4 " + " (" + gameManager.playerColor[4] + "): " + gameManager.getPlayerScore(4)



    }
}
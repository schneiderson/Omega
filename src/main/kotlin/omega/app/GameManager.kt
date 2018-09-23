package omega.app

import omega.ai.Agent
import omega.ai.HumanAgent
import omega.ai.RandomAgent
import omega.model.Cell
import omega.model.State
import omega.model.Grid
import omega.util.StateChangeListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

object GameManager {

    val playerColor = arrayOf("", "White", "Black", "Red", "Green")
    val agents: ArrayList<Agent> = arrayListOf(HumanAgent(), RandomAgent(), RandomAgent(), RandomAgent())

    var players: Int = 2
    var boardSize: Int = 5
    val maxBoardSize: Int = 10

    var stateChangeListener: StateChangeListener? = null
    var currentState: State by Delegates.observable(
            State(Grid(maxBoardSize - boardSize)),
            onChange = {
                prop, old, new ->
                if(stateChangeListener != null) stateChangeListener!!.onValueChanged(new)
            }
    )
    private var stateHistory: LinkedList<State> = LinkedList()

    fun changeBoardSize(size: Int) {
        boardSize = size
        // reset current state and state history
        currentState = State(Grid(maxBoardSize - boardSize))
        stateHistory = LinkedList()
    }

    fun changeNumberOfPlayers(players: Int) {
        this.players = players
    }

    fun addStateChangeListener(listener: StateChangeListener){
        stateChangeListener = listener
    }

    fun getGrid(): Grid {
        return currentState.grid
    }

    fun getCurrentPlayer(): Int {
        return currentState.playerTurn
    }

    fun colorToPlay(): Int {
        return currentState.colorToPlay
    }

    fun nextPlayersTurn(): Int {
        var currentPlayer = getCurrentPlayer()
        if (colorToPlay() < players) {
            return currentPlayer
        } else {
            if (currentPlayer < players) {
                return currentPlayer + 1
            } else {
                return 1
            }
        }
    }

    fun nextColorToPlay(): Int {
        if (colorToPlay() < players) {
            return colorToPlay() + 1
        }
        return 1

    }

    fun currentPlayerIsHuman(): Boolean{
        var player = getCurrentPlayer()
        var agent = agents.get(player - 1)
        return agent is HumanAgent
    }


    /* MOVE MANAGEMENT */
    fun performMove(cell: Cell, cellType: Int) {
        stateHistory.addLast(currentState)
        currentState = currentState.applyMove(cell, cellType, nextPlayersTurn(), nextColorToPlay())
        getNextMove()
    }

    fun getNextMove(){
        var player = getCurrentPlayer()
        var agent = agents.get(player - 1)
        if(agent is HumanAgent){
            // wait for UI input
        } else{
            performMove(agent.getAction(currentState))
        }
    }

    fun performMove(cell: Cell) {
        performMove(cell, currentState.colorToPlay)
    }

    fun undoMove() {
        var previousState = stateHistory.removeLast()

        currentState = previousState
    }

    fun undoable(): Boolean {
        return stateHistory.size > 0
    }

    fun getPlayerScore(i: Int): Int {
        return currentState.scores[i-1]
    }

    fun gameEnd(): Boolean{
        if(getCurrentPlayer() == 1 && colorToPlay() == 1){
            return currentState.grid.getFreeCells().size < (players * 2)
        }
        return false
    }

}
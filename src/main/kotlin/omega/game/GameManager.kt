package omega.game

import omega.ai.*
import omega.model.Action
import omega.model.Cell
import omega.model.State
import omega.model.Grid
import omega.util.GameSpecificKnowledge
import omega.util.StateChangeListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.properties.Delegates

object GameManager {
    var boardSize: Int = 3
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

    val playerColor = arrayOf("", "White", "Black", "Red", "Green")
    val agents: ArrayList<Agent> = arrayListOf(HumanAgent(currentState), MiniMaxABAgent(currentState), RandomAgent(currentState), RandomAgent(currentState))

    fun changeBoardSize(size: Int) {
        boardSize = size
        // reset current state and state history
        currentState = State(Grid(maxBoardSize - boardSize))
        stateHistory = LinkedList()
    }

    fun changeNumberOfPlayers(players: Int) {
        currentState.players = players
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

    fun getNumberOfPlayers(): Int{
        return currentState.players
    }

    fun colorToPlay(): Int {
        return currentState.colorToPlay
    }

    fun nextPlayersTurn(): Int {
        return currentState.nextPlayersTurn()
    }

    fun nextColorToPlay(): Int {
        return currentState.nextColorToPlay()
    }

    fun currentPlayerIsHuman(): Boolean{
        var player = getCurrentPlayer()
        var agent = agents.get(player - 1)
        return agent is HumanAgent
    }


    /* MOVE MANAGEMENT */
    fun performMove(action: Action) {
        stateHistory.addLast(currentState)
        currentState = currentState.playMove(action)
        getNextMove()
    }

    fun getNextMove(){
        var player = getCurrentPlayer()
        var agent = agents.get(player - 1)
        if(agent is HumanAgent){
            // wait for UI input
        } else{
            // get AI action in separate thread
            thread {
                performMove(agent.getAction(currentState.clone()))
            }
        }
    }

    fun performMove(cell: Cell) {
        performMove(Action(cell.coordinate, currentState.colorToPlay))
    }

    fun undoMove() {
        var previousState = stateHistory.removeLast()

        currentState = previousState
    }

    fun undoable(): Boolean {
        return stateHistory.size > 0
    }

    fun getPlayerScore(i: Int): Int {
        if (i > getNumberOfPlayers()) return 0
        return currentState.playerScores[i-1].score
    }

    fun gameEnd(): Boolean{
        return currentState.gameEnd()
    }

}
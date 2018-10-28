package omega.game

import omega.ai.*
import omega.ai.evaluation.SimpleScore
import omega.model.Action
import omega.model.Cell
import omega.model.State
import omega.model.Grid
import omega.model.CombinedAction
import omega.util.StateChangeListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.properties.Delegates

object GameManager {
    var boardSize: Int = 4
    const val maxBoardSize: Int = 10

    var stateChangeListener: StateChangeListener? = null
    var currentState: State by Delegates.observable(
            State(Grid(maxBoardSize - boardSize)),
            onChange = {
                _, _, new ->
                if(stateChangeListener != null) stateChangeListener!!.onValueChanged(new)
            }
    )
    private var stateHistory: LinkedList<State> = LinkedList()

    val playerColor = arrayOf("", "White", "Black", "Red", "Green")
    val evalFunction = SimpleScore()
    var maxDepth = 8

    // List of Agents
    val agents: ArrayList<Agent> = arrayListOf(
            HumanAgent(currentState),
            MiniMaxTTAgent(currentState, maxDepth, evalFunction))

    fun changeBoardSize(size: Int) {
        boardSize = size
        // reset current state and state history
        currentState = State(Grid(maxBoardSize - boardSize))
        stateHistory = LinkedList()
    }

    fun changeNumberOfPlayers(players: Int) {
        currentState.players = players
    }

    fun setOpponent(agent: String){
        when (agent) {
            "MiniMaxAgent" -> {
                agents[1] = MiniMaxAgent(currentState, maxDepth, evalFunction)
            }
            "MiniMaxABAgent" -> {
                agents[1] = MiniMaxABAgent(currentState, maxDepth, evalFunction)
            }
            "MiniMaxTTAgent" -> {
                agents[1] = MiniMaxTTAgent(currentState, maxDepth, evalFunction)
            }
            "MiniMaxIDAgent" -> {
                agents[1] = MiniMaxIDAgent(currentState, maxDepth, evalFunction)
            }
            "MiniMaxTTMOAgent" -> {
                agents[1] = MiniMaxTTMOAgent(currentState, maxDepth, evalFunction)
            }
            "MiniMaxTTNMAgent" -> {
                agents[1] = MiniMaxTTNMAgent(currentState, maxDepth, evalFunction)
            }
        }
    }

    fun setSearchDepth(depth: Int){
        maxDepth = depth
        agents.forEach { it.setSearchDepth(maxDepth) }
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
        var agent = agents[player - 1]
        return agent is HumanAgent
    }


    /* MOVE MANAGEMENT */
    fun performMove(action: CombinedAction, blocking: Boolean = false) {
        stateHistory.addLast(currentState)
        currentState = currentState.playMove(action)
        if(!gameEnd()) getNextMove(blocking)
    }

    fun getNextMove(blocking: Boolean = false){
        var player = getCurrentPlayer()
        var agent = agents[player - 1]
        if(agent is HumanAgent){
            // wait for UI input
        } else{
            if(!blocking){
                // get AI action in separate thread
                thread {
                    performMove(agent.getAction(currentState.clone()))
                }
            } else{
                performMove(agent.getAction(currentState.clone()), blocking)
            }
        }
    }

    fun performMove(cell: Cell) {
        performMove(CombinedAction.getCombinedAction(Action(cell.coordinate, currentState.colorToPlay)))
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

    fun runHeadless(): State{
        getNextMove(true)
        return currentState
    }

    fun resetGame() {
        currentState = State(Grid(maxBoardSize - boardSize))
    }

    fun gameEnd(): Boolean{
        return currentState.gameEnd()
    }

}
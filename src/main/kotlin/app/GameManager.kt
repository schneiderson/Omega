package omega.app

import omega.model.State
import omega.model.Grid

object GameManager{

    val currentState: State = State(Grid())
    val stateHistory: ArrayList<State> = ArrayList<State>()

    fun getGrid(): Grid{
        return currentState.grid
    }

    fun getCurrentPlayer(): Int{
        return currentState.playerTurn
    }
}
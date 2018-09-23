package omega.app

import omega.model.Cell
import omega.model.State
import omega.model.Grid

object GameManager{

    var currentState: State = State()
    val stateHistory: ArrayList<State> = ArrayList()

    fun getGrid(): Grid{
        return currentState.grid
    }

    fun getCurrentPlayer(): Int{
        return currentState.playerTurn
    }

    fun colorToPlay(): Int{
        return 1
    }

    fun performMove(cell: Cell, cellType: Int){
        stateHistory.add(currentState)
        currentState = currentState.applyMove(cell, cellType)
    }

    fun performMove(cell: Cell){
        performMove(cell, currentState.colorToPlay)
    }
}
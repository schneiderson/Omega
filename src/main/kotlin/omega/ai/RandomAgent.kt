package omega.ai

import omega.model.State
import omega.model.CombinedAction
import java.util.*

class RandomAgent(var initialState: State, var combinedActions: Boolean = false): Agent{
    private fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    override var agentName: String = "RandomAgent"

    override
    fun getAction(state: State): CombinedAction {
        if(combinedActions){
            return state.getLegalCombinedActions().getRandomElement()
        }
        return state.getLegalActions().getRandomElement()
    }
}
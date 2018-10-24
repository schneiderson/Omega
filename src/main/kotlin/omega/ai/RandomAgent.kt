package omega.ai

import omega.model.Action
import omega.model.State
import java.util.*

class RandomAgent(var initialState: State): Agent{
    fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    override var agentName: String = "RandomAgent"

    override
    fun getAction(state: State): Action {
        // Thread.sleep(5000)
        return state.getLegalActions().getRandomElement()
    }
}
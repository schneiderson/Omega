package omega.ai

import omega.game.Action
import omega.model.State
import java.util.*

class NegaMaxAgent(): Agent{
    fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    override
    fun getAction(state: State): Action {
        return state.getLegalActions().getRandomElement()
    }
}
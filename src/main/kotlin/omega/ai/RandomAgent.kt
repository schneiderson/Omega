package omega.ai

import omega.model.Cell
import omega.model.State
import java.util.*

class RandomAgent(): Agent{
    fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    override
    fun getAction(state: State): Cell {
        return state.grid.getFreeCells().getRandomElement()
    }
}
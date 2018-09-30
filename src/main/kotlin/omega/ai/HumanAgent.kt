package omega.ai

import omega.game.Action
import omega.model.Cell
import omega.model.State
import omega.util.Coordinate
import java.util.*

class HumanAgent(): Agent{

    override
    fun getAction(state: State): Action {
        // Dummy implementation
        return Action(Coordinate(0,0), -1)
    }
}
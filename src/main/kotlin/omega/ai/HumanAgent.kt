package omega.ai

import omega.model.Action
import omega.model.State
import omega.util.Coordinate

class HumanAgent(): Agent{

    override
    fun getAction(state: State): Action {
        // Dummy implementation
        return Action(Coordinate(0, 0), -1)
    }
}
package omega.ai

import omega.model.Action
import omega.model.State
import omega.util.Coordinate

class HumanAgent(var initialState: State): Agent{

    override
    fun getAction(state: State): Action {
        // Dummy implementation
        return Action(Coordinate(0, 0), -1)
    }
}
package omega.ai

import omega.model.Action
import omega.model.CombinedAction
import omega.model.State
import omega.util.Coordinate

class HumanAgent(var initialState: State): Agent{

    override var agentName: String = "HumanAgent"

    override
    fun getAction(state: State): CombinedAction {
        // Dummy implementation
        return CombinedAction.getCombinedAction(Action(Coordinate(0, 0), -1))
    }
}
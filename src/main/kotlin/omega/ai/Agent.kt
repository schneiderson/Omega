package omega.ai

import omega.model.Action
import omega.model.State
import omega.util.GameSpecificKnowledge

interface Agent{
    var agentName: String
    fun getAction(state: State): Action

}
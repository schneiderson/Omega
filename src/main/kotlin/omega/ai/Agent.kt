package omega.ai

import omega.model.State
import omega.model.CombinedAction

interface Agent{
    var agentName: String
    fun getAction(state: State): CombinedAction
    fun setSearchDepth(depth: Int)

}
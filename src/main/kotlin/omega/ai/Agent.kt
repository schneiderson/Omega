package omega.ai

import omega.model.Action
import omega.model.State

interface Agent {
    fun getAction(state: State): Action
}
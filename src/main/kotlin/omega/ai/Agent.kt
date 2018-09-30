package omega.ai

import omega.game.Action
import omega.model.State

interface Agent {
    fun getAction(state: State): Action
}
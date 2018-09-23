package omega.ai

import omega.model.Cell
import omega.model.State

interface Agent {
    fun getAction(state: State): Cell
}
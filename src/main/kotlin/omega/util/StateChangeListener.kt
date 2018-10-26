package omega.util

import omega.model.State

/**
 * State Change Listener Interface
 *
 * (required for loose coupling of UI and model)
 */
interface StateChangeListener {
    fun onValueChanged(state: State)
}
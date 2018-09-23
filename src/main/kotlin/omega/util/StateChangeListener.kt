package omega.util

import omega.model.State

interface StateChangeListener {
    fun onValueChanged(state: State)
}
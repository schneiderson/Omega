package omega.util

import omega.model.Action

/**
 * A combined action consisting of several actions performed sequentially
 */
class CombinedAction(val actions: ArrayList<Action>) {

    // comparison operator
    fun equals(combAction: CombinedAction): Boolean {
        var equal = actions.size == combAction.actions.size
        for(i in 0..actions.size) {
            equal = actions[i] == combAction.actions[i]
        }
        return equal
    }

    // toString method override
    override fun toString(): String {
        var outString = ""
        actions.forEach { it ->
            outString += "action: $it"
        }
        return outString
    }

}
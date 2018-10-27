package omega.model

/**
 * A combined action consisting of several actions performed sequentially
 */
class CombinedAction(val actions: ArrayList<Action>) {

    companion object {
        var invalidCombinedAction = getCombinedAction(Action.invalidAction)
        fun getCombinedAction(action: Action): CombinedAction {
            return CombinedAction(arrayListOf(action))
        }
    }

    // comparison operator
    fun equals(combAction: CombinedAction): Boolean {
        var equal = actions.size == combAction.actions.size
        if(equal){
            for(i in 0 until actions.size) {
                if(!actions[i].equals(combAction.actions[i])) {
                    equal = false
                    break
                }
            }
        }
        return equal
    }

    // toString method override
    override fun toString(): String {
        var outString = ""
        actions.forEach { it ->
            outString += "action: $it "
        }
        return outString
    }

}
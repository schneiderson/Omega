package omega.model

import omega.util.Coordinate

class Action(val coordinate: Coordinate, val cellType: Int) {

    fun equals(action: Action): Boolean {
        if(coordinate == action.coordinate && cellType == action.cellType){
            return true
        }
        return false
    }
}
package omega.searchtree

import omega.model.CombinedAction

class Edge (val move: CombinedAction, val fromNode: Node, val toNode: Node){

    var score = 0.0

    init {
        toNode.parentConnections.add(this)
    }
}
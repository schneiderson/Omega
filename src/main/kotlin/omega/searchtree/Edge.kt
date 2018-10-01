package omega.searchtree

import omega.model.Action

class Edge (val move: Action, val fromNode: Node, val toNode: Node){

    var score = 0.0

    init {
        toNode.parentConnections.add(this)
    }
}
package omega.searchtree

import omega.model.Action
import omega.model.State

class Node (val tree: Tree, val state: State){
    var isExpanded = false
    var visists = 0

    val childConnections = ArrayList<Edge>()
    val parentConnections = ArrayList<Edge>()

    var score: Double = 0.0


    fun expand(){
        if(!isExpanded){
            for(action in state.getLegalActions()){
                addChildConnectionFromAction(action)
            }
            isExpanded = true
        }
    }

    fun addChildConnectionFromAction(action: Action){
        var newState = state.applyMove(action)
        var newNode = Node(tree, newState)
        childConnections.add(Edge(action, this, newNode))
    }

    fun getBestAction(): Edge{
        var highScore = Double.NEGATIVE_INFINITY
        var highScorer = childConnections.get(0)

        for(edge in childConnections){
            if(edge.toNode.score > highScore){
                highScore = edge.toNode.score
                highScorer = edge
            }
        }
        return highScorer
    }

}
package omega.searchtree

import omega.model.Action
import omega.model.State
import java.lang.Exception

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

    private fun addChildConnectionFromAction(action: Action){
        var newNode = Node(tree, state)
        childConnections.add(Edge(action, this, newNode))
    }

    fun gotToChild(edge: Edge){
        if(childConnections.contains(edge)){
            state.simulateMove(edge.move)
        } else{
            throw Exception("move is an illegal option in this state")
        }
    }

    fun goToParent(){
        var parentEdge = parentConnections.get(0)
        state.undoSimulatedMove(parentEdge.move)
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
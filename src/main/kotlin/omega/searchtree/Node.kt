package omega.searchtree

import omega.model.State
import omega.model.CombinedAction
import java.lang.Exception

class Node (val tree: Tree, val state: State, val combinedActions: Boolean = false){
    var isExpanded = false
    var visists = 0

    val childConnections = ArrayList<Edge>()
    val parentConnections = ArrayList<Edge>()

    var score: Double = 0.0

    fun expand(){
        if(!isExpanded){
            var legalActions = if(combinedActions){
                state.getLegalCombinedActions()
            }else {
                state.getLegalActions()
            }

            for(action in legalActions){
                addChildConnectionFromAction(action)
            }
            isExpanded = true
        }
    }



    private fun addChildConnectionFromAction(action: CombinedAction){
        childConnections.add(getNewEdgeFromAction(action))
    }

    fun getNewEdgeFromAction(action: CombinedAction): Edge{
        var newNode = Node(tree, state, combinedActions)
        return Edge(action, this, newNode)
    }

    fun hasEdge(edge: Edge): Boolean{
        return childConnections.any { it.move.equals(edge.move) }
    }

    fun getEdgeByAction(action: CombinedAction): Edge?{
        return childConnections.firstOrNull { it.move.equals(action) }
    }

    fun gotToChild(edge: Edge){
        if(childConnections.contains(edge)){
            state.simulateMove(edge.move)
        } else{
            throw Exception("move is an illegal option in this state")
        }
    }

    fun goToParent(){
        var parentEdge = parentConnections[0]
        state.undoSimulatedMove(parentEdge.move)
    }

    fun applyNullMove(){
        state.playerTurn = state.nextPlayersTurn()
        state.colorToPlay = state.nextColorToPlay()
    }

    fun undoNullMove(){
        state.playerTurn = state.previousPlayersTurn()
        state.colorToPlay = state.previousColorToPlay()
    }

    fun getBestAction(): Edge{
        var highScore = Double.NEGATIVE_INFINITY
        var highScorer = childConnections[0]

        for(edge in childConnections){
            if(edge.toNode.score > highScore){
                highScore = edge.toNode.score
                highScorer = edge
            }
        }
        return highScorer
    }

}
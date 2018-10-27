package omega.ai

import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.model.CombinedAction
import omega.util.GameSpecificKnowledge

class MiniMaxAgent(
        var initialState: State,
        var maxDepth: Int = 8,
        var evaluator: NodeEvaluation = SimpleScore(),
        var combinedActions: Boolean = false
): Agent{
    override var agentName: String = "MiniMaxAgent - ${evaluator.evalFuncName}"
    var gsk = GameSpecificKnowledge(initialState)
    var visits = 0
    var terminalNodes = 0

    override
    fun getAction(state: State): CombinedAction {

        val tree = Tree(state, combinedActions)
        val root = tree.root

        visits = 0
        terminalNodes = 0

        evaluate(root, maxDepth, state.playerTurn)

        return root.getBestAction().move

    }

    fun evaluate(root: Node, depth: Int, maximizingPlayer: Int){
        miniMax(root, depth, maximizingPlayer)
    }

    fun miniMax(node: Node, depth: Int, maximizingPlayer: Int): Double{
        // count number of visits
        visits++

        if(depth == 0 || node.state.gameEnd()){
            terminalNodes++
            return evaluator.evaluate(node, maximizingPlayer, gsk)
        }

        node.expand()
        var maximizing = node.state.playerTurn == maximizingPlayer
        var value = if(maximizing) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY

        node.childConnections.forEach { edge ->
            edge.toNode.score = value

            node.gotToChild(edge)
            var nodeValue = miniMax(edge.toNode, depth - 1, maximizingPlayer)
            edge.toNode.goToParent()

            value = if(maximizing) maxOf(value, nodeValue) else minOf(value, nodeValue)
            edge.toNode.score = value
        }

        return value
    }
}
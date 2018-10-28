package omega.ai

import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.CombinedAction
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.util.GameSpecificKnowledge

class MiniMaxABMOAgent(
        var initialState: State,
        var maxDepth: Int = 8,
        var evaluator: NodeEvaluation = SimpleScore(),
        var combinedActions: Boolean = false
): Agent{
    override var agentName: String = "MiniMaxABMOAgent - ${evaluator.evalFuncName}"
    var gsk = GameSpecificKnowledge(initialState)

    override fun setSearchDepth(depth: Int) {
        maxDepth = depth
    }

    override
    fun getAction(state: State): CombinedAction {

        val tree = Tree(state, combinedActions)
        val root = tree.root

        evaluate(root, maxDepth, state.playerTurn)

        return root.getBestAction().move

    }

    fun evaluate(root: Node, depth: Int, maximizingPlayer: Int){
        miniMax(root, depth, maximizingPlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
    }

    fun miniMax(node: Node, depth: Int, maximizingPlayer: Int, alpha_init: Double, beta_init: Double): Double{
        if(depth == 0 || node.state.gameEnd())
            return evaluator.evaluate(node, maximizingPlayer, gsk)

        var alpha = alpha_init
        var beta = beta_init
        var maximizing = node.state.playerTurn == maximizingPlayer
        var value = if(maximizing) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY

        node.expand()
        // order moves
        node.childConnections.forEach { edge -> edge.toNode.score = evaluator.evaluate(node, maximizingPlayer, gsk)}
        node.childConnections.sortedWith(compareBy { it.toNode.score })

        for(edge in node.childConnections){
            edge.toNode.score = value

            node.gotToChild(edge)
            var nodeValue = miniMax(edge.toNode, depth - 1, maximizingPlayer, alpha, beta)
            edge.toNode.goToParent()

            if(maximizing){
                value = maxOf(value, nodeValue)
                alpha = maxOf(alpha, value)
            } else {
                value = minOf(value, nodeValue)
                beta = minOf(beta, value)
            }
            edge.toNode.score = value

            if(alpha >= beta){
                break
            }
        }

        return value
    }
}
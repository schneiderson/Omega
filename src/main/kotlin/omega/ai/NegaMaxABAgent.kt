package omega.ai

import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.model.CombinedAction
import omega.util.GameSpecificKnowledge

class NegaMaxABAgent(
        var initialState: State,
        var maxDepth: Int = 8,
        var evaluator: NodeEvaluation = SimpleScore(),
        var combinedActions: Boolean = false
): Agent{
    override var agentName: String = "NegaMaxABAgent - ${evaluator.evalFuncName}"
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
        negaMax(root, depth , maximizingPlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
    }

    fun negaMax(node: Node, depth: Int, maximizingPlayer: Int, alpha_init: Double, beta_init: Double): Double{
        if(depth == 0 || node.state.gameEnd())
            return evaluator.evaluate(node, maximizingPlayer, gsk)

        var alpha = alpha_init
        var beta = beta_init
        var value = Double.NEGATIVE_INFINITY

        node.expand()

        for(edge in node.childConnections){
            var factor = 1
            var alphaPass = alpha
            var betaPass = beta
            if(node.state.playerTurn != maximizingPlayer){
                factor = -1
                alphaPass = beta * -1
                betaPass = alpha * -1
            }

            node.gotToChild(edge)
            var nodeValue = factor * negaMax(edge.toNode, depth - 1, maximizingPlayer, alphaPass, betaPass)
            edge.toNode.goToParent()

            value = maxOf(value, nodeValue)
            alpha = maxOf(alpha, value)
            edge.toNode.score = value

            if (alpha >= beta){
                break // cut-off
            }
        }

        return value
    }
}
package omega.ai

import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.transpositionTable.TranspositionTable
import omega.model.CombinedAction
import omega.util.GameSpecificKnowledge

class MiniMaxTTNMAgent(
        var initialState: State,
        var maxDepth: Int = 8,
        var evaluator: NodeEvaluation = SimpleScore(),
        var combinedActions: Boolean = false
): Agent{
    override var agentName: String = "MiniMaxTTNMAgent - ${evaluator.evalFuncName}"
    var gsk = GameSpecificKnowledge(initialState)
    var transpositionTable = TranspositionTable(initialState)
    val invalidMove = CombinedAction.invalidCombinedAction

    var visists = 0
    var transposRetrievals = 0

    override fun setSearchDepth(depth: Int) {
        maxDepth = depth
    }

    override
    fun getAction(state: State): CombinedAction {

        val tree = Tree(state, combinedActions)
        transpositionTable = TranspositionTable(initialState)
        val root = tree.root

        visists = 0
        transposRetrievals = 0

        evaluate(root, maxDepth, state.playerTurn)

        return root.getBestAction().move

    }

    fun evaluate(root: Node, depth: Int, maximizingPlayer: Int){
        miniMax(root, depth, maximizingPlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
    }

    fun miniMax(node: Node, depth: Int, maximizingPlayer: Int, alpha_init: Double, beta_init: Double): Double{

        /* Start with transposition table lookup */
        var moveInfo = transpositionTable.probeHash(node.state.grid.cells)
        visists++
        if(moveInfo.move != invalidMove){
            transposRetrievals ++
            return moveInfo.value
        }


        if(depth == 0 || node.state.gameEnd())
            return evaluator.evaluate(node, maximizingPlayer, gsk)

        var alpha = alpha_init
        var beta = beta_init
        var maximizing = node.state.playerTurn == maximizingPlayer
        var value = if(maximizing) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY

        node.expand()


        // do null move
        node.applyNullMove()
        var nodeValue = miniMax(node, depth - 1, maximizingPlayer, alpha, beta)

        if(maximizing){
            value = maxOf(value, nodeValue)
            alpha = maxOf(alpha, value)
        } else {
            value = minOf(value, nodeValue)
            beta = minOf(beta, value)
        }
        node.undoNullMove()


        for(edge in node.childConnections){
            edge.toNode.score = value

            node.gotToChild(edge)
            var nodeValue = miniMax(edge.toNode, depth - 1, maximizingPlayer, alpha, beta)

            if(maximizing){
                value = maxOf(value, nodeValue)
                alpha = maxOf(alpha, value)
            } else {
                value = minOf(value, nodeValue)
                beta = minOf(beta, value)
            }

            edge.toNode.score = value
            /* save hash and value in transposition table */
            transpositionTable.recordHash(edge.toNode.state.grid.cells, value, 0, edge.move)
            edge.toNode.goToParent()

            if(alpha >= beta){
                break
            }
        }

        return value
    }
}
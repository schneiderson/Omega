package omega.ai

import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.transpositionTable.TranspositionTable
import omega.model.CombinedAction
import omega.util.GameSpecificKnowledge

class MiniMaxTTAgent(
        var initialState: State,
        var maxDepth: Int = 8,
        var evaluator: NodeEvaluation = SimpleScore(),
        var combinedActions: Boolean = false
): Agent{
    override var agentName: String = "MiniMaxTTAgent - ${evaluator.evalFuncName}"
    var gsk = GameSpecificKnowledge(initialState)
    var transpositionTable = TranspositionTable(initialState)
    val invalidMove = CombinedAction.invalidCombinedAction

    var visits = 0
    var terminalNodes = 0
    var transposRetrievals = 0

    override
    fun getAction(state: State): CombinedAction {

        val tree = Tree(state, combinedActions)
        transpositionTable = TranspositionTable(initialState)
        val root = tree.root

        visits = 0
        terminalNodes = 0
        transposRetrievals = 0

        evaluate(root, maxDepth, state.playerTurn)

        return root.getBestAction().move

    }

    fun evaluate(root: Node, depth: Int, maximizingPlayer: Int){
        miniMax(root, depth, maximizingPlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
    }

    fun miniMax(node: Node, depth: Int, maximizingPlayer: Int, alpha_init: Double, beta_init: Double): Double{

        // count number of nodes visited
        visits++

        /* Start with transposition table lookup */
        var moveInfo = transpositionTable.probeHash(node.state.grid.cells)
        if(!moveInfo.move.equals(invalidMove)){
            transposRetrievals ++
            return moveInfo.value
        }


        if(depth == 0 || node.state.gameEnd()){
            terminalNodes++
            return evaluator.evaluate(node, maximizingPlayer, gsk)
        }


        var alpha = alpha_init
        var beta = beta_init
        var maximizing = node.state.playerTurn == maximizingPlayer
        var value = if(maximizing) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY

        node.expand()

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
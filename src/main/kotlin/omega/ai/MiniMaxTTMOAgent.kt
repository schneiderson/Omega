package omega.ai

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.ai.evaluation.SimpleScore2
import omega.model.Action
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.transpositionTable.TranspositionTable
import omega.util.Coordinate
import omega.util.GameSpecificKnowledge
import java.util.*
import kotlin.system.measureTimeMillis

class MiniMaxTTMOAgent(var initialState: State, var maxDepth: Int = 8, var evaluator: NodeEvaluation = SimpleScore()): Agent{
    override var agentName: String = "MiniMaxTTMOAgent - ${evaluator.evalFuncName}"
    var gsk = GameSpecificKnowledge(initialState)
    var transpositionTable = TranspositionTable(initialState)
    val invalidMove = Action.invalidAction

    var visists = 0
    var transposRetrievals = 0

    override
    fun getAction(state: State): Action {

        val tree = Tree(state)
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
        if(!moveInfo.move.equals(invalidMove)){
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
        orderMoves(node, maximizingPlayer)

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

    fun orderMoves(node: Node, maximizingPlayer: Int){
        node.childConnections.forEach { edge ->
            node.gotToChild(edge)
            var moveInfo = transpositionTable.probeHash(node.state.grid.cells)
            if(!moveInfo.move.equals(invalidMove)){
                edge.toNode.score = moveInfo.value
            } else {
                edge.toNode.score = evaluator.evaluate(node, maximizingPlayer, gsk)
            }
            edge.toNode.goToParent()
        }

        node.childConnections.sortedWith(compareBy { it.toNode.score })

    }
}
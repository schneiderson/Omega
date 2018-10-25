package omega.ai

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withTimeoutOrNull
import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.Action
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.util.Coordinate
import omega.util.GameSpecificKnowledge
import java.util.*
import kotlin.system.measureTimeMillis

class MiniMaxIDAgent(
        var initialState: State,
        var maxDepth: Int = 8,
        var evaluator: NodeEvaluation = SimpleScore(),
        var maxTime: Long = 10000
): Agent{
    override var agentName: String = "MiniMaxIDAgent - ${evaluator.evalFuncName}"
    var gsk = GameSpecificKnowledge(initialState)
    var depthReached = 0

    override
    fun getAction(state: State): Action {

        val tree = Tree(state)
        val root = tree.root
        var action = Action.invalidAction

        runBlocking {
            launch {
                action = evaluate(root, state.playerTurn)
            }.join()
        }

        return action
    }

    suspend fun evaluate(root: Node, maximizingPlayer: Int): Action{
        var bestMove = Action.invalidAction

        var currentDepth = 1
        withTimeoutOrNull(maxTime) {
            while(currentDepth <= maxDepth){
                miniMax(root, currentDepth, maximizingPlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)

                bestMove = root.getBestAction().move

                depthReached = currentDepth
                currentDepth++
            }
        }

        return bestMove
    }

    suspend fun miniMax(node: Node, depth: Int, maximizingPlayer: Int, alpha_init: Double, beta_init: Double): Double{
        // call delay to make coroutine interruptalbe
        delay(1)

        if(depth == 0 || node.state.gameEnd())
            return evaluator.evaluate(node, maximizingPlayer, gsk)

        var alpha = alpha_init
        var beta = beta_init
        var maximizing = node.state.playerTurn == maximizingPlayer
        var value = if(maximizing) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY

        node.expand()

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
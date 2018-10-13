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
import omega.util.GameSpecificKnowledge
import java.util.*
import kotlin.system.measureTimeMillis

class MiniMaxIDAgent(var initialState: State): Agent{
    fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    var evaluator = SimpleScore2()
    var gsk = GameSpecificKnowledge(initialState)
    var maxDepth = 8

    override
    fun getAction(state: State): Action {

        val tree = Tree(state)
        val root = tree.root

        evaluate(root, maxDepth, state.playerTurn)

        return root.getBestAction().move

    }

    fun evaluate(root: Node, depth: Int, maximizingPlayer: Int){
        val timeElapsed = measureTimeMillis {
            miniMax(root, depth, maximizingPlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
        }
        println("$timeElapsed")

    }

    fun miniMax(node: Node, depth: Int, maximizingPlayer: Int, alpha_init: Double, beta_init: Double): Double{
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
package omega.ai

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.Action
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import java.util.*
import kotlin.system.measureTimeMillis

class NegaMaxABAgent(): Agent{
    fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    var evaluator: NodeEvaluation = SimpleScore()
    var maxDepth = 4

    override
    fun getAction(state: State): Action {

        val tree = Tree(state)
        val root = tree.root

        evaluate(root, maxDepth, state.playerTurn)

        return root.getBestAction().move

    }

    fun evaluate(root: Node, depth: Int, maximizingPlayer: Int){
        val timeElapsed = measureTimeMillis {
            negaMax(root, depth , maximizingPlayer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)
        }
        println("$timeElapsed")

    }

    fun negaMax(node: Node, depth: Int, maximizingPlayer: Int, alpha_init: Double, beta_init: Double): Double{
        if(depth == 0 || node.state.gameEnd())
            return evaluator.evaluate(node, maximizingPlayer)

        var alpha = alpha_init
        var beta = beta_init
        var value = Double.NEGATIVE_INFINITY

        node.expand()

        for(edge in node.childConnections){
            var factor = 1
            var alpha_pass = alpha
            var beta_pass = beta
            if(node.state.playerTurn !== node.state.nextPlayersTurn()){
                factor = -1
                alpha_pass = beta * -1
                beta_pass = alpha * -1
            }

            var nodeValue = negaMax(edge.toNode, depth - 1, maximizingPlayer, alpha_pass, beta_pass)
            value = maxOf(value, factor * nodeValue)
            edge.toNode.score = value
            alpha = maxOf(alpha, value)
            if (alpha >= beta){
                break // cut-off
            }
        }

        return value
    }
}
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

class NegaMaxAgent(): Agent{
    fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    var evaluator: NodeEvaluation = SimpleScore()
    var maxDepth = 5

    override
    fun getAction(state: State): Action {

        val tree = Tree(state)
        val root = tree.root

        evaluate(root, maxDepth, state.playerTurn)

        return root.getBestAction().move

    }

    fun evaluate(root: Node, depth: Int, maximizingPlayer: Int){
        root.expand()

        val timeElapsed = measureTimeMillis {
            for(edge in root.childConnections) {
                edge.toNode.score = negaMax(edge.toNode, depth - 1, maximizingPlayer)
            }
        }
        println("$timeElapsed")

    }

    fun negaMax(node: Node, depth: Int, maximizingPlayer: Int): Double{
        var value = Double.NEGATIVE_INFINITY
        var parentNode = node.parentConnections.get(0).fromNode
        if(depth == 0 || node.state.gameEnd())
            return evaluator.evaluate(node, maximizingPlayer)

        var factor = if(parentNode.state.playerTurn == node.state.playerTurn) 1 else -1

        node.expand()
        for(edge in node.childConnections){
            var nodeValue = negaMax(edge.toNode, depth - 1, maximizingPlayer)
            value = maxOf(value, factor * nodeValue)
        }

        return value
    }
}
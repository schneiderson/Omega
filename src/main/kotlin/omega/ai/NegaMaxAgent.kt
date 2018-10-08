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
            negaMax(root, depth, maximizingPlayer)
        }
        println("$timeElapsed")

    }

    fun negaMax(node: Node, depth: Int, maximizingPlayer: Int): Double{
        if(depth == 0 || node.state.gameEnd())
            return evaluator.evaluate(node, maximizingPlayer)

        var value = Double.NEGATIVE_INFINITY
        node.expand()

        var factor = 1
        if(node.state.playerTurn !== node.state.nextPlayersTurn()){
            factor = -1
        }

        for(edge in node.childConnections){
            var nodeValue = negaMax(edge.toNode, depth - 1, maximizingPlayer)
            value = maxOf(value, factor * nodeValue)
            edge.toNode.score = value
        }

        return value
    }
}
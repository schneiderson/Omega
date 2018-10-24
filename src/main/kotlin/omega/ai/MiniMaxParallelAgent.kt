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

class MiniMaxParallelAgent(): Agent{
    fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    override var agentName: String = "MiniMaxParallelAgent"
    var evaluator: NodeEvaluation
    var maxDepth = 5
    var executeAsync = true

    init {
        evaluator = SimpleScore()
    }

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
            if(executeAsync){
                val deferred = (root.childConnections).map { n ->
                    async {
                        n.toNode.score = miniMax(n.toNode, depth - 1, maximizingPlayer)
                    }
                }

                runBlocking {
                    deferred.forEach {
                        it.await()
                    }
                }
            }
            else{
                for(edge in root.childConnections) {
                    edge.toNode.score = miniMax(edge.toNode, depth - 1, maximizingPlayer)
                }
            }
        }
        println("$timeElapsed")

    }

    fun miniMax(node: Node, depth: Int, maximizingPlayer: Int): Double{
        var value = Double.NEGATIVE_INFINITY
        var parentNode = node.parentConnections.get(0).fromNode
        if(depth == 0 || node.state.gameEnd())
            return evaluator.evaluate(node, maximizingPlayer, null)
        else if(parentNode.state.playerTurn == maximizingPlayer){
            node.expand()
            for(edge in node.childConnections){
                var nodeValue = miniMax(edge.toNode, depth - 1, maximizingPlayer)
                value = maxOf(value, nodeValue)
            }
        } else {
            value = Double.POSITIVE_INFINITY
            node.expand()
            for(edge in node.childConnections){
                var nodeValue = miniMax(edge.toNode, depth - 1, maximizingPlayer)
                value = minOf(value, nodeValue)
            }
        }
        return value
    }
}
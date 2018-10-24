package omega.ai

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.Action
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.util.GameSpecificKnowledge
import org.w3c.dom.xpath.XPathEvaluator
import java.util.*
import kotlin.system.measureTimeMillis

class NegaMaxAgent(var initialState: State, var maxDepth: Int = 8, var evaluator: NodeEvaluation = SimpleScore()): Agent{
    fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    override var agentName: String = "NegaMaxAgent"
    var gsk = GameSpecificKnowledge(initialState)

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
            return evaluator.evaluate(node, maximizingPlayer, gsk)

        var value = Double.NEGATIVE_INFINITY
        node.expand()

        var factor = 1
        if(node.state.playerTurn != maximizingPlayer){
            factor = -1
        }

        for(edge in node.childConnections){
            edge.toNode.score = value

            node.gotToChild(edge)
            var nodeValue = negaMax(edge.toNode, depth - 1, maximizingPlayer)
            edge.toNode.goToParent()

            value = maxOf(value, factor * nodeValue)
            edge.toNode.score = value
        }

        return value
    }
}
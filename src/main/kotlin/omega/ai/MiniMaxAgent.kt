package omega.ai

import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.Action
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.util.GameSpecificKnowledge
import java.util.*
import kotlin.system.measureTimeMillis

class MiniMaxAgent(var initialState: State, var maxDepth: Int = 8, var evaluator: NodeEvaluation = SimpleScore()): Agent{
    fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    override var agentName: String = "MiniMaxAgent"
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
            miniMax(root, depth, maximizingPlayer)
        }
    }

    fun miniMax(node: Node, depth: Int, maximizingPlayer: Int): Double{
        if(depth == 0 || node.state.gameEnd())
            return evaluator.evaluate(node, maximizingPlayer, gsk)

        node.expand()
        var maximizing = node.state.playerTurn == maximizingPlayer
        var value = if(maximizing) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY

        for(edge in node.childConnections){
            edge.toNode.score = value

            node.gotToChild(edge)
            var nodeValue = miniMax(edge.toNode, depth - 1, maximizingPlayer)
            edge.toNode.goToParent()

            value = if(maximizing) maxOf(value, nodeValue) else minOf(value, nodeValue)
            edge.toNode.score = value
        }

        return value
    }
}
package omega.ai

import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.Action
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import java.util.*

class GreedyAgent(): Agent{
    fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]

    var evaluator: NodeEvaluation

    init {
        evaluator = SimpleScore()
    }

    override
    fun getAction(state: State): Action {

        val tree = Tree(state)
        val root = tree.root

        root.expand()
        evaluate(root)

        return root.getBestAction().move
    }

    fun evaluate(node: Node){
        var playerTurn = node.state.playerTurn

        node.score = evaluator.evaluate(node, playerTurn)

        // TODO: Consider using coroutines
        for(edge in node.childConnections){
            edge.toNode.score = evaluator.evaluate(edge.toNode, playerTurn)
        }

    }
}
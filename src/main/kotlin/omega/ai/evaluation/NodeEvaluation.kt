package omega.ai.evaluation

import omega.searchtree.Node

interface NodeEvaluation {
    fun evaluate(node: Node, playerTurn: Int): Double
}
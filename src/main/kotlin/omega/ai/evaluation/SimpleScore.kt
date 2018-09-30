package omega.ai.evaluation

import omega.searchtree.Node

class SimpleScore: NodeEvaluation {
    override fun evaluate(node: Node, playerTurn: Int): Double {
        node.state.calcPlayerScores()
        return node.state.scores[playerTurn - 1].toDouble()
    }
}
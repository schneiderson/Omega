package omega.ai.evaluation

import omega.searchtree.Node

class SimpleScore: NodeEvaluation {
    override fun evaluate(node: Node, scoresForPlayer: Int): Double {
        var state = node.state
        state.calcPlayerScores()
        var opponent = state.getOpponent(scoresForPlayer)
        return state.scores[scoresForPlayer - 1].toDouble() - state.scores[opponent - 1]
    }
}
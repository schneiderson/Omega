package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge

class DistanceScore: NodeEvaluation {
    override fun evaluate(node: Node, scoresForPlayer: Int, gsk: GameSpecificKnowledge?): Double {
        var state = node.state
        state.calcPlayerScores()
        var opponent = state.getOpponent(scoresForPlayer)
        return state.playerScores[scoresForPlayer - 1].score.toDouble() - state.playerScores[opponent - 1].score
    }
}
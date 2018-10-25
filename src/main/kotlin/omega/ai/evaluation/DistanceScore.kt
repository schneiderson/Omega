package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge
import java.lang.Exception

class DistanceScore: NodeEvaluation {
    override val evalFuncName = "DistanceScore"

    override fun evaluate(node: Node, scoresForPlayer: Int, gsk: GameSpecificKnowledge?): Double {
        var state = node.state
        state.calcPlayerScores()
        var opponent = state.getOpponent(scoresForPlayer)

        var playerScore = state.playerScores[scoresForPlayer - 1]
        var opponentScore = state.playerScores[opponent - 1]

        var playerClusters = playerScore.clusters
        var opponentClusters = opponentScore.clusters

        var score = Double.NEGATIVE_INFINITY

        if(gsk != null){
//            playerClusters.forEach {
//
//            }
//            getDistanceFromCoordinate

        } else {
            throw Exception("game specific knowledge missing in eval function!")
        }

        return score
    }
}
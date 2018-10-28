package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge
import java.lang.Exception
import java.lang.Math.abs

class ClusterPointScore(var clusterWeight: Double = 0.5): NodeEvaluation{
    override val evalFuncName = "ClusterPointScore"

    override fun evaluate(node: Node, scoresForPlayer: Int, gsk: GameSpecificKnowledge?): Double {
        var state = node.state
        state.calcPlayerScores()
        var opponent = state.getOpponent(scoresForPlayer)

        var playerScore = state.playerScores[scoresForPlayer - 1]
        var opponentScore = state.playerScores[opponent - 1]

        var playerClusters = playerScore.clusters
        var opponentClusters = opponentScore.clusters

        var score = 0.0

        if(gsk != null){

            var pointWeight = 1 - clusterWeight

            var clusterScorePlayer = 2.0 / (abs(gsk.numClustersUpperBound - playerClusters.size) + 1)
            var pointScorePlayer = 2.0 / (abs(gsk.pointsUpperBound - playerScore.score) + 1)

            var clusterScoreOpponent = 2.0 / (abs(gsk.numClustersUpperBound - opponentClusters.size) + 1)
            var pointScoreOpponent = 2.0 / (abs(gsk.pointsUpperBound - opponentScore.score) + 1)

            score = (clusterWeight * clusterScorePlayer + pointWeight * pointScorePlayer)
            - (clusterWeight * clusterScoreOpponent + pointWeight * pointScoreOpponent)

        } else {
            throw Exception("game specific knowledge missing in eval function!")
        }

        return score
    }
}
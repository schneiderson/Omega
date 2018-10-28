package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge
import java.lang.Exception
import java.lang.Math.abs

class SimpleScore2: NodeEvaluation{
    override val evalFuncName = "SimpleScore2"

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

            var clusterWeight = 0.5
            var pointWeight = 1 - clusterWeight

            var clusterScorePlayer = 2 / (abs(gsk.numClustersUpperBound - playerClusters.size) + 1)
            var pointScorePlayer = 2 / (abs(gsk.pointsUpperBound - playerScore.score) + 1)

            var clusterScoreOpponent = 2 / (abs(gsk.numClustersUpperBound - opponentClusters.size) + 1)
            var pointScoreOpponent = 2 / (abs(gsk.pointsUpperBound - opponentScore.score) + 1)

            score = (clusterWeight * clusterScorePlayer + pointWeight * pointScorePlayer) * 10
            - (clusterWeight * clusterScoreOpponent + pointWeight * pointScoreOpponent) * 10

        } else {
            throw Exception("game specific knowledge missing in eval function!")
        }

        return score
    }
}
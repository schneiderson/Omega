package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge
import java.lang.Exception
import java.lang.Math.abs

class ClusterSizePointScore: NodeEvaluation{
    override val evalFuncName = "ClusterSizePointScore"

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
            gsk.pointsUpperBound
            gsk.numClustersUpperBound

            var roundsPlayed = node.state.getRoundsPlayed()

            var clusterWeight = 1
            var pointWeight = 0

            // player
            var clusterScorePlayer = 2.0 / (abs(playerClusters.size - gsk.numClustersUpperBound) + 1)
            var avgClusterSizePlayer = 0.0
            playerClusters.forEach { avgClusterSizePlayer += it.size }
            avgClusterSizePlayer /= clusterScorePlayer
            var clusterSizeScorePlayer = 2.0 / (abs(3 - avgClusterSizePlayer) + 1)
            var pointScorePlayer = playerScore.score - gsk.pointsUpperBound


            // opponent
            var clusterScoreOpponent = 2.0 / (abs(opponentClusters.size - gsk.numClustersUpperBound) + 1)
            var pointScoreOpponent = opponentScore.score - gsk.pointsUpperBound
            var avgClusterSizeOpponent = 0.0
            opponentClusters.forEach { avgClusterSizeOpponent += it.size }
            avgClusterSizeOpponent/= clusterScoreOpponent
            var clusterSizeScoreOpponent = 2.0 / (abs(3 - avgClusterSizeOpponent) +1)


            var maximizingPlayerScore = clusterWeight * (clusterScorePlayer * clusterSizeScorePlayer) + pointScorePlayer
            var opponentScore = clusterWeight * (clusterScoreOpponent * clusterSizeScoreOpponent) + pointScoreOpponent

            score = maximizingPlayerScore - opponentScore

        } else {
            throw Exception("game specific knowledge missing in eval function!")
        }

        return score
    }
}
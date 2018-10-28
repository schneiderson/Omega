package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge
import java.lang.Exception
import java.lang.Math.abs

class SimpleScore3: NodeEvaluation{
    override val evalFuncName = "SimpleScore3"

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
            if(gsk.rounds - roundsPlayed < 3){
                var pointWeight = 1
                var clusterWeight = 0
            }

            var clusterScorePlayer = 1 / abs(playerClusters.size - gsk.numClustersUpperBound)
            var avgClusterSizePlayer = 0
            playerClusters.forEach { avgClusterSizePlayer += it.size }
            avgClusterSizePlayer /= clusterScorePlayer
            var clusterSizeScorePlayer = 1 / abs(3 - avgClusterSizePlayer)
            var pointScorePlayer = playerScore.score - gsk.pointsUpperBound



            var clusterScoreOpponent = opponentClusters.size - gsk.numClustersUpperBound
            var pointScoreOpponent = opponentScore.score - gsk.pointsUpperBound
            var avgClusterSizeOpponent = 0
            playerClusters.forEach { avgClusterSizeOpponent += it.size }
            avgClusterSizeOpponent/= clusterScoreOpponent
            var clusterSizeScoreOpponent = 1 / abs(3 - avgClusterSizeOpponent)

            var maximizingPlayerScore = clusterWeight * (clusterScorePlayer * clusterSizeScorePlayer) + pointScorePlayer
            var opponentScore = clusterWeight * (clusterScoreOpponent * clusterSizeScoreOpponent) + pointScoreOpponent

            score = clusterWeight * ( clusterScorePlayer  )  + pointWeight * pointScorePlayer


        } else {
            throw Exception("game specific knowledge missing in eval function!")
        }

        return score
    }
}
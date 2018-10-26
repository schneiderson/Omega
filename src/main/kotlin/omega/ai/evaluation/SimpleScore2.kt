package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge
import java.lang.Exception

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

            var clusterScorePlayer = playerClusters.size - gsk.numClustersUpperBound
            var avgClusterSizePlayer = 0
            playerClusters.forEach { avgClusterSizePlayer += it.size }
            avgClusterSizePlayer /= clusterScorePlayer
            var pointScorePlayer = playerScore.score - gsk.pointsUpperBound



            var clusterScoreOpponent = gsk.numClustersUpperBound - opponentClusters.size
            var pointScoreOpponent = gsk.pointsUpperBound - opponentScore.score


            score = clusterWeight * ( clusterScorePlayer  )  + pointWeight * pointScorePlayer


        } else {
            throw Exception("game specific knowledge missing in eval function!")
        }

        return score
    }
}
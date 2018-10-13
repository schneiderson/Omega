package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge
import java.lang.Exception

class SimpleScore2: NodeEvaluation{
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
            gsk.pointsUpperBound
            gsk.numClustersUpperBound

            var roundsPlayed = node.state.getRoundsPlayed()

            var clusterWeight = (gsk.rounds + 1) / (roundsPlayed + 1)
            var pointWeight = 1 - clusterWeight

            var clusterScorePlayer = gsk.numClustersUpperBound - playerClusters.size
            var pointScorePlayer = gsk.pointsUpperBound - playerScore.score

            var clusterScoreOpponent = gsk.numClustersUpperBound - opponentClusters.size
            var pointScoreOpponent = gsk.pointsUpperBound - opponentScore.score

            score = clusterWeight * clusterScorePlayer + pointWeight * pointScorePlayer -
                    (clusterWeight * clusterScoreOpponent + pointWeight * pointScoreOpponent)


        } else {
            throw Exception("game specific knowledge missing in eval function!")
        }

        return score
    }
}
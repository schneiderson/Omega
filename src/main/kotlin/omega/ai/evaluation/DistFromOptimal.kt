package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge
import java.lang.Exception

class DistFromOptimal: NodeEvaluation{
    override val evalFuncName = "DistFromOptimal"

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

            var weightShift = 1 - (gsk.rounds + 1) / (roundsPlayed + 1)
            var clusterWeight = 0.0 * weightShift
            var pointWeight = 1 - clusterWeight

            var clusterScorePlayer = playerClusters.size - gsk.numClustersUpperBound
            var pointScorePlayer = playerScore.score - gsk.pointsUpperBound

            var clusterScoreOpponent = gsk.numClustersUpperBound - opponentClusters.size
            var pointScoreOpponent = gsk.pointsUpperBound - opponentScore.score

//            score = clusterWeight * clusterScorePlayer + pointWeight * pointScorePlayer -
//                    (clusterWeight * clusterScoreOpponent + pointWeight * pointScoreOpponent)
            score = clusterWeight * clusterScorePlayer + pointWeight * pointScorePlayer


        } else {
            throw Exception("game specific knowledge missing in eval function!")
        }

        return score
    }
}
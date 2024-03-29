package experiments

import omega.ai.*
import omega.ai.evaluation.SimpleScore
import omega.ai.evaluation.ClusterPointScore
import omega.ai.evaluation.ClusterSizePointScore
import omega.ai.evaluation.WeightShiftScore
import omega.game.GameManager
import kotlin.system.measureTimeMillis

fun main(args: Array<String>){

    var evalFunc1 = WeightShiftScore()

//    var evalFunc1 = ClusterPointScore()
    var evalFunc2 = ClusterSizePointScore()

    var gm = GameManager
    var x = 1
    var maxdepth = 4
    var maxtime = 3000L
    var boardSize = 4

    gm.changeBoardSize(boardSize)


    var agent1 = MiniMaxABAgent(gm.currentState, maxdepth, evalFunc1)
    var agent2 = MiniMaxABAgent(gm.currentState, maxdepth, evalFunc2)

    println("BoardSize: $boardSize")
    println("Maxdepth: $maxdepth")
    println("")

    (1..2).forEach{ i ->
        when (i) {
            1 -> {
                gm.agents[0] = agent1
                gm.agents[1] = agent2
            }
            else -> {
                gm.agents[0] = agent2
                gm.agents[1] = agent1
            }
        }

        var results = arrayOf(0, 0, 0)
        var gameTimes = ArrayList<Long>()

        (1..x).forEach { _ ->
            var timeOfGame = measureTimeMillis {
                var finalState = gm.runHeadless()
                when {
                    finalState.playerScores[0].score == finalState.playerScores[1].score
                        // draw
                    -> results[1]++
                    finalState.playerScores[0].score > finalState.playerScores[1].score
                        // player 1 win
                    -> results[0]++
                    else
                        // player 2 win
                    -> results[2]++
                }
            }
            gameTimes.add(timeOfGame)

            gm.resetGame()
        }

        var avgTimeGame = gameTimes.sum()/gameTimes.size

        println("After $x games")
        println("Player 1: ${gm.agents[0].agentName}, Wins: ${results[0]}")
        println("Player 2: ${gm.agents[1].agentName}, Wins: ${results[2]}")
        println("Draws : ${results[1]}")
        println("Average time of game: $avgTimeGame")
        println("")
    }

}

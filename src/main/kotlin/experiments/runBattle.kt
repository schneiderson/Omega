package experiments

import omega.ai.*
import omega.ai.evaluation.SimpleScore
import omega.ai.evaluation.SimpleScore2
import omega.game.GameManager
import omega.model.Grid
import omega.model.State
import kotlin.system.measureTimeMillis

fun main(args: Array<String>){

    var currentState = State(Grid(7))
    var evalFunc = SimpleScore()

    var gm = GameManager
    var x = 100

    gm.agents[1] = MiniMaxAgent(gm.currentState, 4, evalFunc)
    gm.agents[0] = RandomAgent(gm.currentState)

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

}

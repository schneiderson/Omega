package experiments

import omega.ai.*
import omega.ai.evaluation.SimpleScore
import omega.ai.evaluation.SimpleScore2
import omega.game.GameManager
import omega.model.CombinedAction
import kotlin.system.measureTimeMillis

fun main(args: Array<String>){

    var evalFunc1 = SimpleScore()
    var evalFunc2 = SimpleScore2()

    var gm = GameManager
    var maxdepth = 2
    var boardSize = 2

    gm.changeBoardSize(boardSize)

    var agent1 = RandomAgent(gm.currentState, true)

    var agent2 = MiniMaxAgent(gm.currentState, maxdepth, evalFunc1,false)
    var agent3 = MiniMaxAgent(gm.currentState, maxdepth/2, evalFunc1,true)
    var agent4 = MiniMaxABAgent(gm.currentState, maxdepth, evalFunc1,false)
    var agent5 = MiniMaxABAgent(gm.currentState, maxdepth/2, evalFunc1,true)
    var agent6 = MiniMaxTTAgent(gm.currentState, maxdepth, evalFunc1, false)
    var agent7 = MiniMaxTTAgent(gm.currentState, maxdepth/2, evalFunc1, true)

    gm.agents[0] = agent1
    gm.agents[1] = agent2

    println("BoardSize: $boardSize")
    println("Maxdepth: $maxdepth")
    println("")

    while(!gm.gameEnd()){
        gm.currentState = gm.currentState.playMove(agent1.getAction(gm.currentState))
        if(gm.gameEnd()) break

        var action: CombinedAction = CombinedAction.invalidCombinedAction
        var timeAgent2 = measureTimeMillis {
            action = agent2.getAction(gm.currentState)
        }
        var timeAgent3 = measureTimeMillis {
            agent3.getAction(gm.currentState)
        }
        var timeAgent4 = measureTimeMillis {
            agent4.getAction(gm.currentState)
        }
        var timeAgent5 = measureTimeMillis {
            agent5.getAction(gm.currentState)
        }
        var timeAgent6 = measureTimeMillis {
            agent6.getAction(gm.currentState)
        }
        var timeAgent7 = measureTimeMillis {
            agent7.getAction(gm.currentState)
        }

        println("${agent2.agentName} \t single actions - Nodes visited: ${agent2.visits} - Terminal Nodes: ${agent2.terminalNodes} - Time: $timeAgent2")
        println("${agent3.agentName} \t combined actions - Nodes visited: ${agent3.visits} - Terminal Nodes: ${agent3.terminalNodes} - Time: $timeAgent3")
        println("${agent4.agentName} \t single actions - Nodes visited: ${agent4.visits} - Terminal Nodes: ${agent4.terminalNodes} - Time: $timeAgent4")
        println("${agent5.agentName} \t combined actions - Nodes visited: ${agent5.visits} - Terminal Nodes: ${agent5.terminalNodes} - Time: $timeAgent5")
        println("${agent6.agentName} \t single actions - Nodes visited: ${agent6.visits} - Terminal Nodes: ${agent6.terminalNodes} - Time: $timeAgent6")
        println("${agent7.agentName} \t combined actions - Nodes visited: ${agent7.visits} - Terminal Nodes: ${agent7.terminalNodes} - Time: $timeAgent7")

        gm.currentState = gm.currentState.playMove(action)
    }




}

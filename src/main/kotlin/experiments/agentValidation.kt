package experiments

import omega.ai.*
import omega.ai.evaluation.ClusterSizePointScore
import omega.model.Action
import omega.model.CombinedAction
import omega.model.Grid
import omega.model.State
import omega.util.Coordinate
import kotlin.system.measureTimeMillis


fun main(args: Array<String>){

    var currentState = State(Grid(7))

    var maxDepth = 4

    var evalFunc = ClusterSizePointScore()
    var agentList = mutableListOf<Agent>()
    agentList.add(MiniMaxAgent(currentState, maxDepth, evalFunc))
    agentList.add(MiniMaxABAgent(currentState, maxDepth, evalFunc))
    agentList.add(MiniMaxTTAgent(currentState, maxDepth, evalFunc))
    agentList.add(MiniMaxTTMOAgent(currentState, maxDepth, evalFunc))
    agentList.add(MiniMaxTTNMAgent(currentState, maxDepth, evalFunc))
    agentList.add(NegaMaxAgent(currentState, maxDepth, evalFunc))
    agentList.add(NegaMaxABAgent(currentState, maxDepth, evalFunc))

    currentState = currentState.playMove(CombinedAction.getCombinedAction(Action(Coordinate(8,7), 1)))
    currentState = currentState.playMove(CombinedAction.getCombinedAction(Action(Coordinate(10,10), 2)))

    var action = CombinedAction.invalidCombinedAction
    val invalidAction = CombinedAction.invalidCombinedAction

    agentList.forEach{
        var executionTime = measureTimeMillis {
            var currAction = it.getAction(currentState)
            if(!action.equals(invalidAction) && !action.equals(currAction)){
                println("${it.agentName} selected a different Action! $currAction, instead of $action")
            }else{
                action = currAction
            }
        }
        println("${it.agentName} required $executionTime milliseconds")
    }

    println("Done")




}

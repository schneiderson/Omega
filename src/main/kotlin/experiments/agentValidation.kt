package experiments

import omega.ai.*
import omega.ai.evaluation.SimpleScore
import omega.ai.evaluation.SimpleScore2
import omega.model.Action
import omega.model.Grid
import omega.model.State
import omega.util.Coordinate


fun main(args: Array<String>){

    var currentState = State(Grid(7))

    var maxDepth = 5

    var evalFunc = SimpleScore2()
    var agentList = mutableListOf<Agent>()
    agentList.add(MiniMaxAgent(currentState, maxDepth, evalFunc))
    agentList.add(MiniMaxABAgent(currentState, maxDepth, evalFunc))
    agentList.add(MiniMaxTTAgent(currentState, maxDepth, evalFunc))
    agentList.add(NegaMaxAgent(currentState, maxDepth, evalFunc))
    agentList.add(NegaMaxABAgent(currentState, maxDepth, evalFunc))

    currentState = currentState.playMove(Action(Coordinate(8,7), 1))
    currentState = currentState.playMove(Action(Coordinate(10,10), 2))

    var action = Action(Coordinate(-1,-1),-3)
    val invalidAction = Action(Coordinate(-1,-1),-3)
    agentList.forEach{
        var curr_action = it.getAction(currentState)
        if(!action.equals(invalidAction) && !action.equals(curr_action)){
            println("${it.agentName} selected a different Action!")
        }else{
            action = curr_action
        }
    }

    println("Done")




}

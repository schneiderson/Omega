package experiments

import omega.ai.*
import omega.ai.evaluation.SimpleScore2
import omega.model.Action
import omega.model.Grid
import omega.model.State
import omega.model.CombinedAction
import omega.util.Coordinate


fun main(args: Array<String>){

    var currentState = State(Grid(7))

    var maxDepth = 20

    var evalFunc = SimpleScore2()
    var time:Long = 1000
    var agent = MiniMaxIDAgent(currentState, maxDepth, evalFunc, time)

    currentState = currentState.playMove(CombinedAction.getCombinedAction(Action(Coordinate(8,7), 1)))
    currentState = currentState.playMove(CombinedAction.getCombinedAction(Action(Coordinate(10,10), 2)))

    var action = CombinedAction.invalidCombinedAction
    val invalidAction = CombinedAction.invalidCombinedAction

    action = agent.getAction(currentState)

    println("After $time,  depth reached: ${agent.depthReached}")

    println("Done")




}

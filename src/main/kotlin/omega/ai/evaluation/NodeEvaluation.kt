package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge

interface NodeEvaluation {
    val evalFuncName: String
    fun evaluate(node: Node, playerTurn: Int, gsk: GameSpecificKnowledge?): Double
}
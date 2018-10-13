package omega.ai.evaluation

import omega.searchtree.Node
import omega.util.GameSpecificKnowledge

interface NodeEvaluation {
    fun evaluate(node: Node, playerTurn: Int, gsk: GameSpecificKnowledge?): Double
}
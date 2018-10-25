package omega.ai

import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.Action
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.util.GameSpecificKnowledge
import java.util.*

class GreedyAgent(var initialState: State) : Agent{
    var evaluator: NodeEvaluation = SimpleScore()
    override var agentName: String = "GreedyAgent - ${evaluator.evalFuncName}"
    var gsk: GameSpecificKnowledge = GameSpecificKnowledge(initialState)

    override
    fun getAction(state: State): Action {

        val tree = Tree(state)
        val root = tree.root

        root.expand()
        evaluate(root, root.state.playerTurn)

        return root.getBestAction().move
    }

    fun evaluate(node: Node, maximizingPlayer: Int){
        for(edge in node.childConnections){
            node.gotToChild(edge)
            edge.toNode.score = evaluator.evaluate(edge.toNode, maximizingPlayer, gsk)
            edge.toNode.goToParent()
        }

    }
}
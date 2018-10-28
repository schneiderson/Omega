package omega.ai

import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.model.CombinedAction
import omega.util.GameSpecificKnowledge

class GreedyAgent(var initialState: State, var combinedActions: Boolean = false) : Agent{

    var evaluator: NodeEvaluation = SimpleScore()
    override var agentName: String = "GreedyAgent - ${evaluator.evalFuncName}"
    var gsk: GameSpecificKnowledge = GameSpecificKnowledge(initialState)

    override fun setSearchDepth(depth: Int) {
        //
    }

    override
    fun getAction(state: State): CombinedAction {

        val tree = Tree(state, combinedActions)
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
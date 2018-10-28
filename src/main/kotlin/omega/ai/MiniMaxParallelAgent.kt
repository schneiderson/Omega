package omega.ai

import omega.ai.evaluation.NodeEvaluation
import omega.ai.evaluation.SimpleScore
import omega.model.State
import omega.searchtree.Node
import omega.searchtree.Tree
import omega.model.CombinedAction
import omega.util.GameSpecificKnowledge

class MiniMaxParallelAgent(
        var initialState: State,
        var maxDepth: Int = 8,
        var evaluator: NodeEvaluation = SimpleScore(),
        var combinedActions: Boolean = false
): Agent{
    override var agentName: String = "MiniMaxParallelAgent - ${evaluator.evalFuncName}"
    var executeAsync = true
    var gsk = GameSpecificKnowledge(initialState)

    override fun setSearchDepth(depth: Int) {
        maxDepth = depth
    }

    override
    fun getAction(state: State): CombinedAction {

        val tree = Tree(state, combinedActions)
        val root = tree.root

        evaluate(root, maxDepth, state.playerTurn)

        return root.getBestAction().move

    }

    fun evaluate(root: Node, depth: Int, maximizingPlayer: Int){
        miniMax(root, depth, maximizingPlayer)

//        val timeElapsed = measureTimeMillis {
//            if(executeAsync){
//                val deferred = (root.childConnections).map { n ->
//                    async {
//                        n.toNode.score = miniMax(n.toNode, depth - 1, maximizingPlayer)
//                    }
//                }
//
//                runBlocking {
//                    deferred.forEach {
//                        it.await()
//                    }
//                }
//            }
//            else{
//                for(edge in root.childConnections) {
//                    edge.toNode.score = miniMax(edge.toNode, depth - 1, maximizingPlayer)
//                }
//            }
//        }

    }

    fun miniMax(node: Node, depth: Int, maximizingPlayer: Int): Double{
        if(depth == 0 || node.state.gameEnd())
            return evaluator.evaluate(node, maximizingPlayer, gsk)

        node.expand()
        var maximizing = node.state.playerTurn == maximizingPlayer
        var value = if(maximizing) Double.NEGATIVE_INFINITY else Double.POSITIVE_INFINITY

        for(edge in node.childConnections){
            edge.toNode.score = value

            node.gotToChild(edge)
            var nodeValue = miniMax(edge.toNode, depth - 1, maximizingPlayer)
            edge.toNode.goToParent()

            value = if(maximizing) maxOf(value, nodeValue) else minOf(value, nodeValue)
            edge.toNode.score = value
        }

        return value
    }
}
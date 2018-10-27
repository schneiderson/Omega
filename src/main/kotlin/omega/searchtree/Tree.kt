package omega.searchtree

import omega.model.State

class Tree(state: State, combinedActions: Boolean = false) {
    var root: Node = Node(this, state, combinedActions)
}
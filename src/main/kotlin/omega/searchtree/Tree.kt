package omega.searchtree

import omega.model.State

class Tree {
    var root: Node

    constructor(state: State){
        root = Node(this, state)
    }

    fun expand(){
        expandFromUntil(root, 1)
    }

    fun expandFromUntil(start: Node, depth: Int){

    }
}
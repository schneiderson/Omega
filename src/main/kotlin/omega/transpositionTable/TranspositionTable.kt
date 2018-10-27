package omega.transpositionTable

import omega.model.Cell
import omega.model.State
import omega.model.CombinedAction
import java.util.Random

class TranspositionTable (var initialState: State){
    fun array2dOfLong(sizeOuter: Int, sizeInner: Int) = Array(sizeOuter) { LongArray(sizeInner) }


    var elements: HashMap<Long, TranspositionElement> = HashMap()

    var zobristRandom = array2dOfLong(initialState.players, initialState.grid.cells.size)

    init {
        // init zobrist random values
        val r = Random()
        for (colors in 0 until initialState.players) {
            for (tile in 0 until initialState.grid.cells.size) {
                zobristRandom[colors][tile] = r.nextLong()
            }
        }
    }

    fun probeHash(cells: ArrayList<Cell>): MoveInfo{
        var tmpHash = createHashKey(cells)
        if(elements.containsKey(tmpHash)){
            val te = elements[tmpHash]
            return te!!.moveInfo
        }
        /* return invalid action */
        return MoveInfo(0.0, CombinedAction.invalidCombinedAction)
    }

    fun recordHash(cells: ArrayList<Cell>, value: Double, flags: Int, move: CombinedAction){
        var transpositionElement = TranspositionElement(createHashKey(cells), flags, MoveInfo(value, move))
        elements[transpositionElement.hashKey] = transpositionElement

    }

    fun createHashKey(cells: ArrayList<Cell>): Long{
        var key = 0L

        for(cell in cells){
            if (cell.cellType > 0) {
                key = key.xor(zobristRandom[cell.cellType -1][cell.index])
            }
        }

        return key
    }

}
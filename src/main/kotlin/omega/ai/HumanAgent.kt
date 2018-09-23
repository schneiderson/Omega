package omega.ai

import omega.model.Cell
import omega.model.State
import omega.util.Coordinate
import java.util.*

class HumanAgent(): Agent{

    override
    fun getAction(state: State): Cell {
        // Dummy implementation
        return Cell(Coordinate(0,0), -1, -1)
    }
}
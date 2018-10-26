package omega.util

import omega.model.State
import kotlin.math.pow

/**
 * Game specific knowledge helper class
 *
 * Provides some game specific information
 * This way it doesn't have to be recalculated in the evaluation function or state
 */
class GameSpecificKnowledge (state: State){

    var tilesToPlay = state.grid.cells.size
    var rounds = tilesToPlay / (2 * state.players)

    var numClustersUpperBound = (tilesToPlay / state.players) / 2

    var numberOfThreeCluster = (tilesToPlay / state.players) / 3
    var remainder = (tilesToPlay / 2) % 3
    var pointsUpperBound =  maxOf(2.0.pow(numClustersUpperBound), 3.0.pow(numberOfThreeCluster) * remainder )

}
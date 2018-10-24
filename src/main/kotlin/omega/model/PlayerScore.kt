package omega.model

data class PlayerScore(val player: Int) {
    var clusters = ArrayList<ArrayList<Cell>>()
    var score = 0

    override fun toString(): String {
        return "Player: $player, score: $score"
    }
}
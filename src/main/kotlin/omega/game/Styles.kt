package omega.game

import javafx.scene.text.FontWeight
import javafx.scene.Cursor
import javafx.scene.paint.CycleMethod
import javafx.scene.paint.LinearGradient
import javafx.scene.paint.Stop
import tornadofx.*

class Styles : Stylesheet() {
    companion object {
        val menuButton by cssclass()
        val heading by cssclass()
        val hexagon by cssclass()
        val board by cssclass()
        val gameStats by cssclass()
        val hexDisabled by cssclass()
        val hexFree by cssclass()
        val statsText by cssclass()
    }

    init {
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }

        statsText {
            padding = box(5.px)
            fontSize = 15.px
        }

        menuButton {
            fontWeight = FontWeight.SEMI_BOLD
            textFill = c(255, 100, 50)
            borderColor += box(c(255, 255, 255), c(255, 255, 255), c(255, 255, 255), c(255, 255, 255))
            backgroundColor += c(255, 255, 255)
            fontSize = 30.px
            cursor = Cursor.HAND
        }

//        hexagon and hover {
//            fill = c("MAGENTA")
//        }

        hexDisabled {
            fill = c("#608786", 0.2)
            stroke = c("#003838", 0.2)
            strokeWidth = 2.0.px
        }

        hexFree {
            fill = c("#9BDEDD", 0.5)
            stroke = c("#003838", 0.5)
        }

        board {
            backgroundColor += LinearGradient(0.0, 0.0, 0.0, 1.0, true,
                    CycleMethod.NO_CYCLE, Stop(0.0, c("#006e6e")),
                    Stop(1.0, c("#003838")))
        }

        gameStats {
            backgroundColor += LinearGradient(0.0, 0.0, 0.0, 1.0, true,
                    CycleMethod.NO_CYCLE, Stop(0.0, c("#003838")),
                    Stop(1.0, c("#006e6e")))
        }


    }


}
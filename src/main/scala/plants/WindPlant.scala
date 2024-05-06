package plants

class WindPlant extends Plant {
    override val name = "Wind turbine"
    var workPercentage = 0.1
    override val filename = "wind.txt"

    def directionChange(): Unit = {
        println("What direction do you want wind turbine to face?")
        println("Choose between 0 and 360 degrees:")
        val angle = scala.io.StdIn.readInt()

        if (angle < 0 || angle > 360) {
            println("wrong input")
        }
        else if (angle <= 90) {
            workPercentage = 0.055
        }
        else if (angle < 180) {
            workPercentage = 0.1
        }
        else if (angle <= 270) {
            workPercentage = 0.075
        }
        else {
            workPercentage = 0.055
        }

    }
}

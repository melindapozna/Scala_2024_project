package plants

class WindPlant extends Plant {
    override val name = "Wind turbine"
    var workPercentage = 0.1
    override val filename = "wind.txt"

    def Direction_change(): Unit = {
        println("what direction do you want wind tubrine to face?")
        println("Choose between 0 and 360 degerees:")
        val angle = scala.io.StdIn.readInt()
        if (angle < 0 || angle > 360) {
            println("wrong input")
        }
        else if (angle >= 0 && angle <= 90) {
            workPercentage = 0.055
        }
        else if (angle > 90 && angle < 180) {
            workPercentage = 0.1
        }
        else if (angle >= 180 && angle <= 270) {
            workPercentage = 0.075
        }
        else if (angle > 270 && angle <= 360) {
            workPercentage = 0.055
        }

    }
}

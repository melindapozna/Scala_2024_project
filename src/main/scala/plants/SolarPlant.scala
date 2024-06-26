package plants
class SolarPlant extends Plant {
    override val name = "Solar panel"
    var workPercentage = 0.1
    override val filename = "solar.txt"

    def angleChange(): Unit = {
        println("What angle do you want to set?")
        println("Choose between 0 and 90 degrees:")
        val angle = scala.io.StdIn.readInt()
        
        if (angle < 0 || angle > 90) {
            println("wrong input")
        }
        else if (angle <= 45) {
            workPercentage = 0.05
        }
        else if (angle < 60) {
            workPercentage = 0.1
        }
        else {
            workPercentage = 0.075
        }
    }

}

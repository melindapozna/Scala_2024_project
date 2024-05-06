package plants
import scala.util.Random
import java.io.File

trait Plant {
    val name: String
    var storage: Double

    //sets the multiplier when calculating the produced energy
    var workPercentage: Double
    def setWorkPercentage(x: Double): Unit = {
        this.workPercentage = x
    }
    //TODO implement
    //def getData(): List[Double]
    def produceEnergy(data: List[Double], x: Int): List[Double] = {
            data.map(_ * workPercentage * x)
    }

    def writeProducedEnergyToFile(data: List[Double]): Unit
    def isMalfunctioning: Boolean = {
        // to simulate malfunctioning, it generates a random number and checks if it's equal to 0
        val rand: Random = new Random()
        if (rand.nextInt(99) == 0) {
            return true
        }
        false
    }
}

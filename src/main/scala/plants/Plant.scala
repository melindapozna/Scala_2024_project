package plants
import scala.util.Random
import java.io.File

trait Plant {
    val name: String

    //def getData(): List[String]
    //def produceEnergy(): List[String]
    //def writeProducedEnergyToFile(file: File): Unit
    def isMalfunctioning(): Boolean = {
        // to simulate malfunctioning, it generates a random number and checks if it's equal to 0
        val rand: Random = new Random()
        if (rand.nextInt(99) == 0) {
            return true
        }
        false
    }



}

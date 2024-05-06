package plants
import scala.util.Random
import java.io.{DataOutputStream, File, FileOutputStream}



trait Plant {
    val name: String
    var storage: Double

    //sets the multiplier when calculating the produced energy
    var workPercentage: Double
    val filename: String


    def getStorage(): Double = Plant.storage
    def incrementTakenStorage(x: Double): Unit = {
        Plant.storage += x
    }

    def decrementTakenStorage(x: Double): Unit = {
        Plant.storage -= x
    }

    def setWorkPercentage(x: Double): Unit = {
        this.workPercentage = x
    }

    def writeProducedEnergyToFile(data: List[Double]): Unit = {
        val dataOutputStream = new DataOutputStream(new FileOutputStream(new File(filename)))
        data.foreach(dataOutputStream.writeDouble)
        dataOutputStream.close()
    }

    def produceEnergy(data: List[Double], numberOfPlants: Int): List[Double] = {
        val energy = data.map(_ * workPercentage * numberOfPlants)
        writeProducedEnergyToFile(energy)
        energy

    }
    def isMalfunctioning: Boolean = {
        // to simulate malfunctioning, it generates a random number and checks if it's equal to 0
        val rand: Random = new Random()
        if (rand.nextInt(99) == 0) {
            return true
        }
        false
    }
}

object Plant {
    //taken storage space
    var storage = 0.0
    val MAX = 1000000.0
}
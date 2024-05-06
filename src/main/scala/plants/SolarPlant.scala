package plants
import java.io._

class SolarPlant extends Plant {
    override val name = "Solar panel"
    override var workPercentage = 0.1

    override def writeProducedEnergyToFile(data: List[Double]): Unit = {
        val dataOutputStream = new DataOutputStream(new FileOutputStream(new File("solar.txt")))
        data.foreach(dataOutputStream.writeDouble)
        dataOutputStream.close()
    }


}

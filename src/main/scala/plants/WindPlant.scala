package plants
import java.io._

class WindPlant extends Plant {
    override val name: String = "Wind turbine"
    override var workPercentage = 0.1

    override def writeProducedEnergyToFile(data: List[Double]): Unit = {
        val dataOutputStream = new DataOutputStream(new FileOutputStream(new File("wind.txt")))
        data.foreach(dataOutputStream.writeDouble)
        dataOutputStream.close()
    }


}

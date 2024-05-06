package plants
import java.io._

class HydroPlant extends Plant {
    override val name = "Hydro plant"
    override var workPercentage = 0.1


    override def writeProducedEnergyToFile(data: List[Double]): Unit = {
        val dataOutputStream = new DataOutputStream(new FileOutputStream(new File("hydro.txt")))
        data.foreach(dataOutputStream.writeDouble)
        dataOutputStream.close()
    }

}

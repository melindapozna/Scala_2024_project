package plants
import java.io._
import scala.util.Try

class HydroPlant extends Plant {
    override val name = "Hydro plant"
    override var workPercentage = 0.1


    override def writeProducedEnergyToFile(data: List[Double]): Unit = {
        Try {
            val dataOutputStream = new DataOutputStream(new FileOutputStream(new File("hydro.txt")))
            data.foreach(dataOutputStream.writeDouble)
            dataOutputStream.close()
        }.toEither match {
        case Left(ex) =>
            println("Couldn't write data to file.")
        case Right(_) =>
            println("Data successfully saved to hydro.txt")
    }
    }

}

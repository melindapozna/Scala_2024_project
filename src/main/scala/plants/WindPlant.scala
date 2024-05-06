package plants
import java.io._
import scala.util.Try

class WindPlant extends Plant {
    override val name: String = "Wind turbine"
    override var workPercentage = 0.1

    override def writeProducedEnergyToFile(data: List[Double]): Unit = {
        Try {
        val dataOutputStream = new DataOutputStream(new FileOutputStream(new File("wind.txt")))
        data.foreach(dataOutputStream.writeDouble)
        dataOutputStream.close()
        }.toEither match {
            case Left(ex) =>
                println("Couldn't write data to file.")
            case Right(_) =>
                println("Data successfully saved to wind.txt")
        }
    }


}

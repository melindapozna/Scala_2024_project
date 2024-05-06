package plants
import java.io._
import scala.util.Try

class SolarPlant extends Plant {
    override val name = "Solar panel"
    override var workPercentage = 0.1

    override def writeProducedEnergyToFile(data: List[Double]): Unit = {
        Try{
            val dataOutputStream = new DataOutputStream(new FileOutputStream(new File("solar.txt")))
            data.foreach(dataOutputStream.writeDouble)
            dataOutputStream.close()
        }.toEither match {
            case Left(ex) =>
                println("Couldn't write data to file.")
            case Right(_) =>
                println("Data successfully saved to solar.txt")
        }
    }


}

package sttp.client4.examples

import plants._
import DataAnalysis._

import io.circe.generic.auto._
import sttp.client4._
import sttp.client4.circe._
import scala.util.{Try, Success, Failure}
import cats.syntax.set
import scala.io.StdIn
import io.circe.Decoder.state
import java.util.Date


object Main {

  def main(args: Array[String]): Unit = {

    // Connection check

    val request = basicRequest
            .get(uri"https://data.fingrid.fi/api/notifications/active")
            .header("x-api-key", "73fc1886cab94b028a409172089a5871")

    val response = request.send(DefaultSyncBackend())

    response.body match {
      case Left(error) =>
        println(s"Error: ${error}")
      case Right(body) =>
        if (response.isSuccess) {
          // Main program here
          while (true){
            println("Choose an option")
            println("1)Generate power")
            println("2)Check storage")
            println("3)Data analysis")
            println("4)Transfer energy")
            println("0)Exit")
            val user_choice = scala.io.StdIn.readInt()
            user_choice match {
              case 1 =>
                val prop = Type_Time_Choice()
                val power_list = APIRequest(prop(0), prop(1), prop(2))
                println("How many plants you want to use?")
                val amount = scala.io.StdIn.readInt()
                if (prop.head == "245") {
                  val solar_plant = new SolarPlant()
                  solar_plant.angleChange()
                  val result = solar_plant.produceEnergy(power_list, amount)
                  println(f"${solar_plant.name} generated power.")
                  Plant.storage += result.foldLeft(0.0)(_ + _)
                  solar_plant.workPercentage = 0.1
                  print("Solar panels were reset to their original position.")
                }
                else if (prop.head == "247") {
                  val wind_plant = new WindPlant()
                  wind_plant.directionChange()
                  val result = wind_plant.produceEnergy(power_list, amount)
                  println(f"${wind_plant.name} generated power.")
                  Plant.storage += result.foldLeft(0.0)(_ + _)
                  wind_plant.workPercentage = 0.1
                  print("Wind turbines were reset to their original position.")
                }
              case 2 =>
                val storagePercentage = Plant.storage / Plant.MAX * 100
                println(f"Storage is ${storagePercentage} %% full.")
              case 3 =>
                val prop = Type_Time_Choice()
                val api_data_list = APIRequest(prop(0), prop(1), prop(2))
                api_data_list.head match {
                  case "245" => val power_list = SolarPlant().produceEnergy(api_data_list)
                  case "247" => val power_list = WindPlant().produceEnergy(api_data_list)
                  case _ => println("Wrong input")
                }
                val mean = DataAnalysis.mean(power_list)
                val median = DataAnalysis.median(power_list)
                val mode = DataAnalysis.mode(power_list)
                val midrange = DataAnalysis.midrange(power_list)
                val range = DataAnalysis.range(power_list)
                println(f"Mean: ${mean}")
                println(f"Median: ${median}")
                println(f"Mode: ${mode}")
                println(f"Range: ${range}")
                println(f"Midrange: ${midrange}")

              case 4 =>
                println(
                  """Choose your option:
                1. Transfer energy to a nearby village (10%)
                2. Transfer energy to a industrial facility (25%)
                3. Transfer energy to municipal power grid (50%)""")
                val user_choice = scala.io.StdIn.readInt()
                user_choice match {
                  case x if (1 <= x && x <= 3) => {
                    val energy_values = List(0.1, 0.25, 0.5)
                    val percentage = energy_values(x - 1)
                    val result = SolarPlant().useEnergy(percentage)
                    if (result)
                      println("Energy transfer successful.")
                    else
                      println("Not enough energy to transfer")
                  }
                  case _ => println("Wrong user input")
                }
              case 0 => return false
              case _ => println("Wrong input")
            }
          }
        } else if (response.isServerError) {
          println(s"Server Error, Got response code: ${response.code}")
        } else {
          println(s"Unexpected response code: ${response.code}")
        }
    }


  }

}



def Type_Time_Choice(): List[String] = {
  println("Choose type:")
  println("1)Solar")
  println("2)Wind")
  var data_id = "0"
  while (data_id == "0") {
    val type_choice = scala.io.StdIn.readInt()
    type_choice match {
      case 1 => data_id = "245"
      case 2 => data_id = "247"
      case _ => println("Wrong input")
    }

  }


  println("Choose start date (YYYY-MM-DD):")
  val start_date = scala.io.StdIn.readLine()
  println("Choose start time(Hour):")
  val start_time = scala.io.StdIn.readLine()
  println("Choose end date (YYYY-MM-DD):")
  val end_date = scala.io.StdIn.readLine()
  println("Choose end time(Hour):")
  val end_time = scala.io.StdIn.readLine()

  val start_time_total = s"${start_date}T${start_time}:00:00"
  val end_time_total = s"${end_date}T${end_time}:00:00"

  return List(data_id, start_time_total, end_time_total)

}

// Time must be in 2023-01-01T00:00:00 format
// DataId - 245 247
// example APIRequest("245", "2023-01-01T00:00:00", "2023-01-02T00:00:00")
def APIRequest(DataID: String, StartTime: String, EndTime: String): List[Double] = {


  case class DataItem(value: Double)
  case class ApiResponse(data: List[DataItem])

  val request = basicRequest
          .get(uri"https://data.fingrid.fi/api/data?datasets=${DataID}&startTime=${StartTime}Z&endTime=${EndTime}Z")
          .header("x-api-key", "73fc1886cab94b028a409172089a5871")
          .response(asJson[ApiResponse])

  val response = request.send(DefaultSyncBackend())

  return response.body match {
    case Left(error) =>
      List.empty
    case Right(apiResponse) =>
      apiResponse.data.map(_.value)



  }

}
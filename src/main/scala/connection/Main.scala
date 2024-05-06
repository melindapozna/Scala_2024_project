package connection

import cats.syntax.set
import io.circe.Decoder.state
import io.circe.generic.auto.*
import plants.*
import sttp.client4.*
import sttp.client4.circe.*

import scala.io.StdIn
import scala.util.{Failure, Success, Try}



object Main {

  def main(args: Array[String]): Unit = {
    
    // Connection check

    val request = basicRequest
      .get(uri"https://data.fingrid.fi/api/notifications/active")
      .header("x-api-key", "73fc1886cab94b028a409172089a5871")

    val response = request.send(DefaultSyncBackend())

    response.body match {
    case Left(error) =>
      println(s"Error: $error")
    case Right(body) =>
      if (response.isSuccess) {
        // Main program here

        println("Choose an option")
        println("1)Generate power")
        println("2)Check storage")
        println("3)Data analysis")
        println("0)Exit")
        val user_choice = scala.io.StdIn.readInt()
        user_choice match {
          case 1 => 
            val prop = Type_Time_Choice()
            val power_list = APIRequest(prop(0), prop(1), prop(2))
            println("How many plants you want to use?")
            val amount = scala.io.StdIn.readInt()
            if (prop(0) == "245") {
              val solar_plant = new SolarPlant()
              solar_plant.produceEnergy(power_list, amount)
            }
            else if (prop(0) == "247") {
              val solar_plant = new WindPlant()
              solar_plant.produceEnergy(power_list, amount)
            }
          case 2 =>  
            // Choose between total, solar, wind
          case 3 =>
            // Mean, avarage etc.
          case 0 => 
          case _ => println("Wrong input")
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
  
  
  println("Choose start time:")
  val start_time = scala.io.StdIn.readLine()
  println("Choose end time:")
  val end_time = scala.io.StdIn.readLine()
  
  return List(data_id, start_time, end_time)
  
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

package sttp.client4.examples

import io.circe.generic.auto._
import sttp.client4._
import sttp.client4.circe._
import scala.util.{Try, Success, Failure}
import cats.syntax.set



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
            // Shoose dates and type of power
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

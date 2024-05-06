package connection

import io.circe.generic.auto.*
import plants.APIRequest
import sttp.client4.*
import sttp.client4.circe.*

import scala.util.{Failure, Success, Try}



object Request {

  def main(args: Array[String]): Unit = {
    
    // Connection check

    val request = basicRequest
      .get(uri"https://data.fingrid.fi/api/notifications/active")
      .header("x-api-key", "73fc1886cab94b028a409172089a5871")

    val response = request.send(DefaultSyncBackend())

    if (response.isServerError) {
      println(s"Server Error, Got response code: ${response.code}")
    }
    // Documentation says that even non 2xx codes will still be pesived as success
    // But I didn't test it, doesn't really matter for us, at least for now
    else if (response.isSuccess) {
      println(s"Connection sucesseful, Got response code: ${response.code}")
      
      APIRequest
    }
      
   else {
      println(s"Something went wrong")
   }
    
    
  }

}


// This part is working method to get Wind power generator forecast(245, 247 is for solar panels) for certain dates
// Documentation for API is confusing so I'm not sure what those values even are, but this can be used as template anyways
object APIRequest {

  // This stuff must be changed depending on  what fields in JSON you want to get
  case class DataItem(value: Double)
  case class ApiResponse(data: List[DataItem])

  val request = basicRequest
      .get(uri"https://data.fingrid.fi/api/data?datasets=245&startTime=2023-01-01T00:00:00Z&endTime=2023-01-02T00:00:00Z")
      .header("x-api-key", "73fc1886cab94b028a409172089a5871")
      .response(asJson[ApiResponse])

  val response = request.send(DefaultSyncBackend())

  
  println(response.body.toString)

}

// I initially wanted to make this into the method where you can pass URL as argument but
// If I understand everything correctly, then datasetId is the only thing we will need
// to change between different requests 


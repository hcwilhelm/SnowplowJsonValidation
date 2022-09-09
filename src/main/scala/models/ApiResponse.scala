package models

import io.circe.Encoder
import io.circe.generic.semiauto._

final case class ApiResponse(action: ApiAction, id: String, status: ApiStatus, message: Option[String])

object ApiResponse {
  implicit val apiResponseEncoder: Encoder[ApiResponse] = deriveEncoder[ApiResponse]
}

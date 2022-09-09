package models

import io.circe.Encoder

sealed trait ApiStatus

object ApiStatus {
  case object Success extends ApiStatus
  case object Error   extends ApiStatus

  implicit val apiActionEncoder: Encoder[ApiStatus] = Encoder[String].contramap {
    case Success => "sucess"
    case Error   => "error"
  }
}

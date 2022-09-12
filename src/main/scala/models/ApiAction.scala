package models

import io.circe.Encoder

sealed trait ApiAction

object ApiAction {
  case object UploadedSchema    extends ApiAction
  case object FetchSchema       extends ApiAction
  case object ValidatedDocument extends ApiAction

  implicit val apiActionEncoder: Encoder[ApiAction] = Encoder[String].contramap {
    case UploadedSchema    => "uploadedSchema"
    case FetchSchema       => "uploadedSchema"
    case ValidatedDocument => "validateDocument"
  }
}

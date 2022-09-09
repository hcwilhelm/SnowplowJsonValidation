package apps

import io.circe.parser._
import io.circe.syntax._
import models.{ApiAction, ApiResponse, ApiStatus}
import repos.JsonSchemaRepo
import zhttp.http._
import zio.ZIO

object JsonSchemaApp {
  def apply() =
    Http.collectZIO[Request] {

      case req @ Method.POST -> !! / "schema" / id =>
        for {
          body     <- req.bodyAsString
          schema   <- ZIO.succeed(parse(body))
          response <- schema match {
                        case Left(_) =>
                          ZIO.succeed(invalidSchemaResponse(id))

                        case Right(schema) =>
                          JsonSchemaRepo.insert(id, schema).as(validSchemaResponse(id))
                      }
        } yield response

      case Method.GET -> !! / "schema" / id =>
        JsonSchemaRepo.getById(id).map {
          case None => notExisitingSchemaResponse(id)
          case Some(schema) => Response.json(schema.toString())
        }

      case req @ Method.POST -> !! / "validate" / id =>
        for {
          body <- req.bodyAsString


        } yield ???
    }


  def invalidSchemaResponse(id: String) =
    Response.json(
      ApiResponse(ApiAction.UploadedSchema, id, ApiStatus.Error, Some("Invalid JSON")).asJson.toString
    ).setStatus(Status.BadRequest)

  def validSchemaResponse(id: String) =
    Response.json(
      ApiResponse(ApiAction.UploadedSchema, id, ApiStatus.Success, None).asJson.toString()
    )

  def notExisitingSchemaResponse(id: String) =
    Response.json(
      ApiResponse(ApiAction.FetchSchema, id, ApiStatus.Error, Some(s"The schema with id: $id dose not exist")).asJson.toString()
    ).setStatus(Status.NotFound)
}

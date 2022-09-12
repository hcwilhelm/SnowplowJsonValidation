package apps

import io.circe.parser._
import io.circe.syntax._
import models.AppError.{ SchemaNotFoundError, SchemaParsingError }
import models.{ ApiAction, ApiResponse, ApiStatus, AppError }
import repos.JsonSchemaRepo
import validation.JsonSchemaValidation
import zhttp.http._
import zio.ZIO

object JsonSchemaApp {
  def apply() =
    Http.collectZIO[Request] {

      case req @ Method.POST -> !! / "schema" / id =>
        (for {
          body   <- req.bodyAsString.orDie
          schema <- ZIO.fromEither(parse(body)).mapError(_ => SchemaParsingError(id))
          _      <- JsonSchemaRepo.insert(id, schema)
        } yield ())
          .as(validSchemaResponse(id))
          .catchAll(error => ZIO.succeed(invalidSchemaResponse(id, error)))

      case Method.GET -> !! / "schema" / id =>
        JsonSchemaRepo
          .getById(id)
          .someOrFail(SchemaNotFoundError(id))
          .map(shema => Response.json(shema.toString()))
          .catchAll(error => ZIO.succeed(invalidSchemaResponse(id, error)))

      case req @ Method.POST -> !! / "validate" / id =>
        (for {
          schemaJson <- JsonSchemaRepo.getById(id).someOrFail(SchemaNotFoundError(id))
          body       <- req.bodyAsString.orDie
          json       <- ZIO.fromEither(parse(body)).mapError(_ => SchemaParsingError(id))
          schema     <- JsonSchemaValidation.loadSchema(schemaJson)
          _          <- JsonSchemaValidation.validateJson(schema, json.deepDropNullValues)
        } yield ())
          .as(validValidationResponse(id))
          .catchAll(error => ZIO.succeed(invalidValidationResponse(id, error)))
    }

  def invalidValidationResponse(id: String, error: AppError) =
    Response
      .json(
        ApiResponse(
          ApiAction.ValidatedDocument,
          id,
          ApiStatus.Error,
          Some(error.toString)
        ).asJson.dropNullValues.toString
      )
      .setStatus(Status.BadRequest)

  def validValidationResponse(id: String) =
    Response.json(
      ApiResponse(ApiAction.ValidatedDocument, id, ApiStatus.Success, None).asJson.dropNullValues.toString()
    )

  def invalidSchemaResponse(id: String, error: AppError) =
    Response
      .json(
        ApiResponse(ApiAction.UploadedSchema, id, ApiStatus.Error, Some(error.toString)).asJson.dropNullValues.toString
      )
      .setStatus(Status.BadRequest)

  def validSchemaResponse(id: String) =
    Response.json(
      ApiResponse(ApiAction.UploadedSchema, id, ApiStatus.Success, None).asJson.dropNullValues.toString()
    )
}

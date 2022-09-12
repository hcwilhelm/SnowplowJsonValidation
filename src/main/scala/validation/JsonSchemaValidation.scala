package validation

import io.circe.Json
import io.circe.schema.Schema
import models.AppError.SchemaValidationError
import zio._

object JsonSchemaValidation {

  def loadSchema(schema: Json) =
    ZIO.succeed(Schema.load(schema))

  def validateJson(schema: Schema, json: Json) =
    ZIO
      .fromEither(schema.validate(json).toEither)
      .mapError(nel => SchemaValidationError(nel.toList.mkString(" / ")))
}

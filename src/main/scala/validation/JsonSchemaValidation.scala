package validation

import io.circe.Json
import io.circe.schema.Schema
import zio._

object JsonSchemaValidation {

  def loadSchema(schema: Json) = ZIO.succeed(Schema.load(schema))

  def validateJson(schema: Schema, json: Json) = ZIO.succeed(schema.validate(json).toEither)
}

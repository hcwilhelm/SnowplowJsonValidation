package repos

import io.circe.Json
import models.AppError.DBError
import zio.{ IO, ZIO }

trait JsonSchemaRepo {
  def insert(id: String, schema: Json): IO[DBError, Unit]

  def getById(id: String): IO[DBError, Option[Json]]
}

object JsonSchemaRepo {
  def insert(id: String, schema: Json) = ZIO.serviceWithZIO[JsonSchemaRepo](_.insert(id, schema))

  def getById(id: String) = ZIO.serviceWithZIO[JsonSchemaRepo](_.getById(id))
}

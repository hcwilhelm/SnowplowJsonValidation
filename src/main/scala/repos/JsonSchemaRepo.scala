package repos

import io.circe.Json
import zio.{Task, ZIO}

trait JsonSchemaRepo {
  def insert(id: String, schema: Json): Task[Unit]

  def getById(id: String): Task[Option[Json]]
}

object JsonSchemaRepo {
  def insert(id: String, schema: Json) = ZIO.serviceWithZIO[JsonSchemaRepo](_.insert(id, schema))

  def getById(id: String) = ZIO.serviceWithZIO[JsonSchemaRepo](_.getById(id))
}

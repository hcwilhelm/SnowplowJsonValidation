package repos

import io.circe.Json
import io.circe.parser._
import io.getquill.{H2ZioJdbcContext, SnakeCase}
import zio._

import javax.sql.DataSource

class H2JsonSchemaRepo(ds: DataSource) extends JsonSchemaRepo {

  private val ctx = new H2ZioJdbcContext(SnakeCase)
  private val dsLayer = ZLayer.succeed(ds)

  import H2JsonSchemaRepo.JsonSchemaTable
  import ctx._

  override def insert(id: String, schema: Json): Task[Unit] =
    ctx.run {
      query[JsonSchemaTable]
        .insertValue(lift(JsonSchemaTable(id, schema.noSpaces)))
        .onConflictIgnore
    }.provide(dsLayer).unit

  override def getById(id: String): Task[Option[Json]] =
    ctx.run {
      query[JsonSchemaTable]
        .filter(_.id == lift(id))
        .map(_.jsonSchema)
    }.provide(dsLayer).map(_.headOption.map(parse).map(_.getOrElse(Json.Null)))
}

object H2JsonSchemaRepo {
  private final case class JsonSchemaTable(id: String, jsonSchema: String)

  def layer: ZLayer[DataSource, Nothing, H2JsonSchemaRepo] = ZLayer.fromFunction(new H2JsonSchemaRepo(_))
}
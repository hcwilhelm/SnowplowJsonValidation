package repos

import io.circe.Json
import io.circe.parser._
import io.getquill.{ H2ZioJdbcContext, SnakeCase }
import javax.sql.DataSource
import models.AppError.DBError
import zio._

class H2JsonSchemaRepo(ds: DataSource) extends JsonSchemaRepo {

  private val ctx     = new H2ZioJdbcContext(SnakeCase)
  private val dsLayer = ZLayer.succeed(ds)

  import H2JsonSchemaRepo.JsonSchemaTable
  import ctx._

  override def insert(id: String, schema: Json): IO[DBError, Unit] =
    ctx
      .run {
        query[JsonSchemaTable]
          .insertValue(lift(JsonSchemaTable(id, schema.noSpaces)))
          .onConflictIgnore
      }
      .provide(dsLayer)
      .unit
      .mapError(DBError)

  override def getById(id: String): IO[DBError, Option[Json]] =
    (for {
      jsonString <- ctx
                      .run {
                        query[JsonSchemaTable]
                          .filter(_.id == lift(id))
                          .map(_.jsonSchema)
                      }
                      .provide(dsLayer)
                      .map(_.headOption)
                      .mapError(DBError)
                      .some
      json       <- ZIO.fromEither(parse(jsonString)).mapError(DBError).asSomeError
    } yield json).unsome
}

object H2JsonSchemaRepo {
  final private case class JsonSchemaTable(id: String, jsonSchema: String)

  def layer: ZLayer[DataSource, Nothing, H2JsonSchemaRepo] = ZLayer.fromFunction(new H2JsonSchemaRepo(_))
}

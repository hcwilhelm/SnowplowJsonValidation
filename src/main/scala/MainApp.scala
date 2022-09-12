import apps.{ HealthCeckApp, JsonSchemaApp }
import io.getquill.jdbczio.Quill
import repos.H2JsonSchemaRepo
import zhttp.service.Server
import zio._

object MainApp extends ZIOAppDefault {

  val dataSourceLayer = Quill.DataSource.fromPrefix("SnowplowApp")
  val appLayer        = dataSourceLayer >>> H2JsonSchemaRepo.layer

  val server = Server.start(
    port = 8080,
    http = HealthCeckApp() ++ JsonSchemaApp()
  )

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    server.provide(appLayer)
}

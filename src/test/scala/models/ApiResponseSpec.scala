package models

import io.circe.syntax._
import io.circe.parser._
import zio.ZIO
import zio.test.{ZIOSpecDefault, assertTrue}

object ApiResponseSpec extends ZIOSpecDefault {

  private val apiResponse = ApiResponse(ApiAction.UploadedSchema, "foo-schema", ApiStatus.Error, Some("Some message"))
  private val apiResponseJson =
    """
      |{
      |  "action" : "uploadedSchema",
      |  "id" : "foo-schema",
      |  "status" : "error",
      |  "message" : "Some message"
      |}
      |""".stripMargin

  def spec = suite("ApiResponseSpec")(
    test("Encode ApiResponse to json") {
      for {
        json <- ZIO.succeed(apiResponse.asJson)
        expextedJson <- ZIO.fromEither(parse(apiResponseJson))
      } yield assertTrue(json == expextedJson)
    }
  )
}

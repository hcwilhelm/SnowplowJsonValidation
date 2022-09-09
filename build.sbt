ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "SnowplowJsonValidation"
  )

libraryDependencies ++= Seq (
  "dev.zio"         %% "zio"                % "2.0.2",
  //"dev.zio"         %% "zio-json"           % "0.3.0-RC11",
  "io.d11"          %% "zhttp"              % "2.0.0-RC10",
  "io.getquill"     %% "quill-zio"          % "4.3.0",
  "io.getquill"     %% "quill-jdbc-zio"     % "4.3.0",
  "com.h2database"   % "h2"                 % "2.1.214",
  "ch.qos.logback"   % "logback-classic"    % "1.4.0",
  "io.circe"        %% "circe-json-schema"  % "0.1.0",
  "io.circe"        %% "circe-generic"      % "0.14.1",
  "io.circe"        %% "circe-parser"       % "0.14.1",
  "dev.zio"         %% "zio-test"           % "2.0.2" % Test,
  "dev.zio"         %% "zio-test-sbt"       % "2.0.2" % Test
)

resolvers += "jitpack".at("https://jitpack.io")

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
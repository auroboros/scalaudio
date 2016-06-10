name := "scalaudio"

version in ThisBuild := "0.01"

scalaVersion in ThisBuild := "2.11.7"

lazy val root = project.in(file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(publish :=())
  .settings(publishLocal :=())
  .aggregate(scalaudioCore,
    scalaudioBuffer,
    scalaudioAMP,
    scalaudioActor,
    scalaudioRPC,
    scalaudioRPCFrontend,
    scalaudioBenchmark)

lazy val scalaudioCore = project.in(file("scalaudio-core"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)

lazy val scalaudioBuffer = project.in(file("scalaudio-buffer"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioCore % "test->test;compile->compile")

lazy val scalaudioAMP = project.in(file("scalaudio-amp"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioCore % "test->test;compile->compile")

lazy val scalaudioActor = project.in(file("scalaudio-actor"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioCore % "test->test;compile->compile")

lazy val scalaudioRPC = project.in(file("scalaudio-rpc"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioCore % "test->test;compile->compile")

lazy val scalaudioRPCFrontend = project.in(file("scalaudio-rpc-frontend"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioCore % "test->test;compile->compile")
  .enablePlugins(PlayScala)

lazy val scalaudioBenchmark = project.in(file("scalaudio-benchmark"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioBuffer, scalaudioAMP, scalaudioActor, scalaudioCore % "test->test;compile->compile")
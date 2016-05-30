name := "scalaudio"

version in ThisBuild := "0.01"

scalaVersion in ThisBuild := "2.11.7"

lazy val root = project.in(file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(publish :=())
  .settings(publishLocal :=())
  .aggregate(scalaudioCore, scalaudioAmp)

lazy val scalaudioCore = project.in(file("scalaudio-core"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)

lazy val scalaudioAmp = project.in(file("scalaudio-amp"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioCore % "test->test;compile->compile")
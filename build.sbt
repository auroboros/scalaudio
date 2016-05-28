name := "scalaudio"

version in ThisBuild := "0.01"

scalaVersion in ThisBuild := "2.11.7"

lazy val root = project.in(file("."))
  .settings(publish :=())
  .settings(publishLocal :=())
  .aggregate(scalaudioCore, scalaudioAmp)

lazy val scalaudioCore = project.in(file("scalaudio-core"))

lazy val scalaudioAmp = project.in(file("scalaudio-amp"))
  .dependsOn(scalaudioCore % "test->test;compile->compile")
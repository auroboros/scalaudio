name := "scalaudio"

version in ThisBuild := "0.01"

scalaVersion in ThisBuild := "2.11.8"

libraryDependencies in ThisBuild ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "junit" % "junit" % "4.12" % "test"
)

fork in run := true

lazy val commonSettings = Seq(
  test in assembly := {},
  packageOptions in assembly ~= { pos =>
    pos.filterNot { po =>
      po.isInstanceOf[Package.MainClass]
    }
  }
)

lazy val root = project.in(file("."))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(Defaults.itSettings: _*)
  .aggregate(
    scalaudioCore,
    scalaudioBuffer,
    scalaudioAMP,
    scalaudioActor,
    scalaudioRPC,
    scalaudioBenchmark)

lazy val scalaudioCore = project.in(file("scalaudio-core"))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioRPC)

lazy val scalaudioBuffer = project.in(file("scalaudio-buffer"))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioCore % "test->test;compile->compile")

lazy val scalaudioAMP = project.in(file("scalaudio-amp"))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioCore % "test->test;compile->compile")

lazy val scalaudioActor = project.in(file("scalaudio-actor"))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioCore % "test->test;compile->compile")

lazy val scalaudioRPC = project.in(file("scalaudio-rpc"))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(Defaults.itSettings: _*)

lazy val scalaudioBenchmark = project.in(file("scalaudio-benchmark"))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioBuffer, scalaudioAMP, scalaudioActor, scalaudioCore % "test->test;compile->compile")
  .enablePlugins(JmhPlugin)
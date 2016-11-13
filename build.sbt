name := "scalaudio"
organization in ThisBuild := "org.auroboros"
version in ThisBuild := "0.0.1-SNAPSHOT"

homepage in ThisBuild := Some(url("https://github.com/auroboros/scalaudio"))
licenses in ThisBuild := Seq("copyright" -> url("https://github.com/auroboros/scalaudio/blob/master/license.txt"))

pomExtra in ThisBuild := <scm>
  <url>git@github.com:auroboros/scalaudio.git</url>
  <connection>scm:git:git@github.com:auroboros/scalaudio.git</connection>
</scm>
  <developers>
    <developer>
      <id>fat0wl</id>
      <name>John McGill</name>
      <url>https://github.com/auroboros</url>
    </developer>
  </developers>

scalaVersion in ThisBuild := "2.11.8"

publishMavenStyle in ThisBuild := true

publishTo in ThisBuild := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

resolvers in ThisBuild += Resolver.mavenLocal

libraryDependencies in ThisBuild ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "junit" % "junit" % "4.12" % "test"
)

fork in run := true

lazy val root = project.in(file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(publishArtifact := false)
  .aggregate(
    scalaudioCore,
    scalaudioBuffer,
    scalaudioAMP,
    scalaudioActor,
    scalaudioRPC,
    scalaudioBenchmark)

lazy val scalaudioCore = project.in(file("scalaudio-core"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioRPC)

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

lazy val scalaudioBenchmark = project.in(file("scalaudio-benchmark"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .dependsOn(scalaudioBuffer, scalaudioAMP, scalaudioActor, scalaudioCore % "test->test;compile->compile")
  .enablePlugins(JmhPlugin)
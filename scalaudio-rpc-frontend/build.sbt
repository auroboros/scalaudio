name := "play-scala"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
//  jdbc,
//  cache,
//  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

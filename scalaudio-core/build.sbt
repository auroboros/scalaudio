name := "scalaudio-core"

version := "0.01"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.softsynth" % "jsyn" % "16.7.3" from "http://www.softsynth.com/jsyn/developers/archives/jsyn_16_7_3.jar",
  "org.apache.commons" % "commons-math3" % "3.6",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.novocode" % "junit-interface" % "0.11" % "test",
  "junit" % "junit" % "4.12" % "test"
)
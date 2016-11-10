name := "scalaudio-core"

unmanagedBase := baseDirectory.value / "../lib"

libraryDependencies ++= Seq(
  "com.softsynth" % "jsyn" % "16.7.3" % "provided",
  "org.apache.commons" % "commons-math3" % "3.6",
//  "org.auroboros" % "signalz" % "0.0.1",
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)
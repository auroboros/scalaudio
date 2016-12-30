name := "scalaudio-core"

libraryDependencies ++= Seq(
  "org.auroboros" %% "signal-z" % "0.1.1-SNAPSHOT" changing(),
  "org.clojars.sidec" % "jsyn" % "16.7.3",
  "org.apache.commons" % "commons-math3" % "3.6",
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)
name := "scalaudio-benchmark"

publishArtifact := false

libraryDependencies ++= Seq(
  "org.openjdk.jmh" % "jmh-core" % "1.12",
  "com.github.yannrichet" % "JMathPlot" % "1.0.1"
)
name := "scalaudio-rpc"

publishArtifact := false

libraryDependencies in ThisBuild ++= Seq(
  "org.apache.thrift" % "libthrift" % "0.9.3",
  "org.eclipse.jetty" % "jetty-servlet" % "9.4.0.M0",
  "org.eclipse.jetty" % "jetty-servlets" % "9.4.0.M0"
)
name := "scalaudio-benchmark"

resolvers += "jzy3d Snapshots" at "http://maven.jzy3d.org/snapshots"
resolvers += "jzy3d Releases" at "http://maven.jzy3d.org/releases"

libraryDependencies ++= Seq(
  "org.openjdk.jmh" % "jmh-core" % "1.12",
  "org.jzy3d" % "jzy3d-api" %  "1.0.0-SNAPSHOT" //"0.9.1"
  //"de.erichseifert.gral" % "gral-core" % "0.11"
)
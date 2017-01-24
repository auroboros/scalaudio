name := "scalaudio-amp"

libraryDependencies ++= Seq(
  "nz.ac.waikato.cms.weka" % "weka-stable" % "3.8.1" //% "optional"
)

unmanagedBase := baseDirectory.value / "../lib"
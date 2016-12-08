#scalaudio

scalaudio is a library is to facilitate audio synthesis/analysis on the JVM by wrapping the Java Sound API in more productive syntax. Its goal is to provide extreme flexibility while reducing verbosity of audio processing code, with the added bonus of type-checking. It aims to be pretty modular (DI for config and output engines via implicits) yet use the same syntax for both real-time and offline processing. Efficiency is a secondary goal, though even in current form some mutable data structures are used to avoid performance snags of constant memory allocation.

### Build info
This project is currently compiled with Scala 2.11 only (cross-compilation support coming soon).

For use within an SBT project, add the following dependency in build.sbt ("amp" is currently the main library module):
```scala
libraryDependencies += "org.auroboros" %% "scalaudio-amp" % "0.1.0-SNAPSHOT"
```

If referencing a snapshot version, the Sonatype snapshot repository must be added as well:
```scala
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
```

Or for other build tools, see:

https://mvnrepository.com/artifact/org.auroboros

###why?
This project was initially created to facilitate my own computer music composition & audio machine learning projects but I decided to open source it since I only get to work on it in spurts (between my dayjob, other music composition, and toying around with audio in Clojure... some lib from that venture will probably be appearing here soon as well).

It was loosely inspired by Phil Burk's excellent library JSyn (in fact, JSyn is still a dependency as scalaudio borrows some of its utils, though this dependency will likely be cut in a future release).

###contribute!
Please feel free to contribute PRs, I will happily review & pull as I am quite actively supporting this project.

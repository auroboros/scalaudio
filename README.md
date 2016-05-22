#scalaudio

scalaudio is a library is to facilitate audio synthesis/analysis on the JVM by wrapping the Java Sound API in more productive syntax. Its goal is to provide extreme flexibility while reducing verbosity of audio processing code, with the added bonus of type-checking. It aims to be pretty modular (DI for config and output engines via implicits) yet use the same syntax for both real-time and offline processing. Efficiency is a secondary goal, though even in current form some mutable data structures are used to avoid performance snags of constant memory allocation.

###Getting started

#####Single channel noise generator example
```scala
object MyFirstComposition extends App with ScalaudioSyntaxHelpers {
    implicit val audioContext = AudioContext(ScalaudioConfig(NOutChannels = 1))

    val noiseGen = new NoiseGen with AudioTimeline
    noiseGen.play(5 seconds)
}
```

###Key Library Components
#####1. ScalaudioConfig
ScalaudioConfig contains a bunch of the parameters relevant to system configuration. Many are experimental but here is a sampling of some of the currently most relevant pieces... Defaults are supplied for all, so this can be constructed only explicitly specifying the values the user wishes to override.
```scala
case class ScalaudioConfig(BeatsPerMinute: Double = 120,
                           BeatsPerMeasure: Double = 4,
                           AutoStartStop: Boolean = true,
                           FramesPerBuffer: Int = 32,
                           NOutChannels: Int = 2,
                           NInChannels: Int = 1,
                           SamplingRate: Int = 44100,
                           FFTBufferSize: Int = 32,
                           ...
```                           
#####2. AudioContext
An AudioContext is basically an operating workspace pinned to a particular Scalaudio config. If no config is passed in, the default config will be used. This context should usually be defined implicitly, as most of the libraries functions accept it implicitly and it is should be transparently passed through most scopes.
```scala
implicit val audioContext = AudioContext()
```
#####3. AudioTimeline
An AudioTimeline is necessary for sound output. It's basically similar to the concept of a DAW timeline (we need some kind of time continuum along which to operate, eh?). Part of the reason for this is so that we can have a master (global) clock, which is possibly a bit of poor design but allows a simple way to avoid re-computation of buffers in units that have already been run for a given point in the timeline. This helps with efficiency and takes the onus off of the user to create signal chains that only trigger computation once per timeline buffer frame.
#####4. Output Engines
Output engines include Playback, Recording, etc. (in the future, writing analysis data to file/datasources). Making these interchangeable is what allows the same syntax to be used in both online & offline modes. A list of outputEngines is passed to the play function of AudioTimeline, meaning technically multiple engines can be used at the same time. ScalaudioSyntaxHelpers provides a default engine (Playback).
```scala
trait AudioTimeline {

  def play(duration : AudioDuration)(implicit audioContext: AudioContext, outputEngines : List[OutputEngine]) = ...
```
#####5. AudioDuration
AudioDuration is similar to Scala's FiniteDuration but supports more audio terminology (beats, measures, buffers, samples). This allows for us to re-express the duration in different terms as is convenient (with whole samples being the finest resolution). AudioDurations can be converted to/from FiniteDuration pretty easily, so typical durations ("5 seconds") can be passed in most places where AudioDurations are required and will be converted implicitly via methods in ScalaudioSyntaxHelpers.
#####6. ScalaudioSyntaxHelpers
This trait includes a bunch of implicit conversions and other useful syntactic sugar.

###why?
This project was initially created to facilitate my own computer music composition & audio machine learning projects but I decided to open source it since I only get to work on it in spurts (between my dayjob, other music composition, and toying around with audio in Clojure... some lib from that venture will probably be appearing here soon as well).

It was loosely inspired by Phil Burk's excellent library JSyn (in fact, JSyn is still a dependency as scalaudio borrows some of its utils, though this dependency will likely be cut in a future release).

###contribute!
Please contribute & get in touch, it will make me way more likely to work on this project regularly instead of just doing some periodic commits between bouts of screwing around with machine learning & Clojure, heh.

There are some big holes in the codebase (higher-level composition, automating parameters handling signal vs. control rate, & more!) and I have ideas as to how to tackle them but would appreciate help & input if anyone's got the time.

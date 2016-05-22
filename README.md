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
#####FuncGen (anonymous UnitGen -- the main thesis)
```scala
implicit val audioContext = AudioContext(ScalaudioConfig())

val noiseGen : NoiseGen = NoiseGen()
val panner : StereoPanner = StereoPanner()

val testBufferFunc : () => List[Array[Double]] = () => {
  noiseGen.outputBuffers() feed panner.processBuffers // feed is a provided syntax helper for chaining
}

val anonUnitGen = new FuncGen(testBufferFunc) with AudioTimeline
anonUnitGen.play(1000 buffers)
```
Probably the core thesis of this library is the bufferFunc unit gen. This is a very tiny convenience wrapper that represents an anonymous unit gen. What is the point? Well, the point is really that an entire signal chain can be viewed as an anonymous unit generator. The output collection component (AudioTimeline) is essentially just looking for a parameterless function it can call repeatedly to generate buffers. As long as the signal chain ends with a component that has such a signature (represented by the abstract "outputBuffers()" signature in AudioTimeline), it can fit into the playback engine. Originally I had developed a method of constructing a SignalChain unit gen that was essentially the combination of unitgens/filters, but it seemed silly to prescribe an arbitrary shape to this structure where a lot of flexibility would typically be desired. Therefore, this was replaced with the FuncGen, a simple wrapper that accepts a constructor arg for a function that generates signal output.
The implementation is pretty simple --
```scala
case class FuncGen(bufferFunc : () => MultichannelAudio) extends UnitGen {
  override def computeBuffer(params : Option[UnitParams] = None) = internalBuffers = bufferFunc()
}
```
###Key Components
#####ScalaudioConfig
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
#####AudioContext
An AudioContext is basically an operating workspace pinned to a particular Scalaudio config. If no config is passed in, the default config will be used. This context should usually be defined implicitly, as most of the library's functions accept it implicitly and pass it transparently through inner scopes.
```scala
implicit val audioContext = AudioContext()
```
#####AudioTimeline
To play audio, you need to mix in the AudioTimeline trait and implement its one unimplemented function (this not-so-coincidentally is a signature provided by any UnitGen).
```scala
  def outputBuffers(params : Option[UnitParams] = None)(implicit audioContext: AudioContext) : List[Array[Double]]
```
An AudioTimeline is necessary for sound output. It's basically similar to the concept of a DAW timeline (we need some kind of time continuum along which to operate, eh?). Part of the reason for this is so that we can have a master (global) clock, which is possibly a bit of poor design but allows a simple way to avoid re-computation of buffers in units that have already been run for a given point in the timeline. This helps with efficiency and takes the onus off of the user to create signal chains that only trigger computation once per timeline buffer frame.
#####Output Engines
Output engines include Playback, Recording, etc. (in the future, writing analysis data to file/datasources). Making these interchangeable is what allows the same syntax to be used in both online & offline modes. A list of outputEngines is passed to the play function of AudioTimeline, meaning technically multiple engines can be used at the same time. ScalaudioSyntaxHelpers provides a default engine (Playback).
```scala
trait AudioTimeline {
  def play(duration : AudioDuration)(implicit audioContext: AudioContext, outputEngines : List[OutputEngine]) = ...
```
#####Type Aliases
To make signal flow more comprehensible (though arguably making naive code-reading less clear), a number of type aliases are introduced for different types of signal.
```scala
package object types {
  type AudioSignal = Array[Double]
  type ControlSignal = Double
  type Signal = Either[ControlSignal, AudioSignal]

  type MultichannelAudio = List[AudioSignal]
}
```
#####AudioDuration
AudioDuration is similar to Scala's FiniteDuration but supports more audio terminology (beats, measures, buffers, samples). This allows for us to re-express the duration in different terms as is convenient (with whole samples being the finest resolution). AudioDurations can be converted to/from FiniteDuration pretty easily, so typical durations ("5 seconds") can be passed in most places where AudioDurations are required and will be converted implicitly via methods in ScalaudioSyntaxHelpers.
#####Pitch
Pitch is basically the frequency domain equivalent of audio duration, though it is less developed and has some caveats to consider (bi-directional conversion issues due to the discrete nature of notation, for example).
#####ScalaudioSyntaxHelpers
This trait includes a bunch of implicit conversions and other useful syntactic sugar, and is basically meant to be mixed in with your main composition script (it is implied in above examples; see: all tests). At the time of writing this includes
  * "Signal Flow" syntax ("feed" & "mix", via implicit conversion to ChannelSetManipulator)
  * "Durations" syntax (via implicit conversion to AudioDuration)
  * "Pitch" syntax (via implicit conversion to Pitch)
  * "Timing Events" syntax
  * Default output engines

###why?
This project was initially created to facilitate my own computer music composition & audio machine learning projects but I decided to open source it since I only get to work on it in spurts (between my dayjob, other music composition, and toying around with audio in Clojure... some lib from that venture will probably be appearing here soon as well).

It was loosely inspired by Phil Burk's excellent library JSyn (in fact, JSyn is still a dependency as scalaudio borrows some of its utils, though this dependency will likely be cut in a future release).

###contribute!
Please contribute & get in touch, it will make me way more likely to work on this project regularly instead of just doing some periodic commits between bouts of screwing around with machine learning & Clojure, heh.

There are some big holes in the codebase (higher-level composition, automating parameters handling signal vs. control rate, & more!) and I have ideas as to how to tackle them but would appreciate help & input if anyone's got the time.

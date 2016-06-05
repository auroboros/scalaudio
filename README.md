#scalaudio

scalaudio is a library is to facilitate audio synthesis/analysis on the JVM by wrapping the Java Sound API in more productive syntax. Its goal is to provide extreme flexibility while reducing verbosity of audio processing code, with the added bonus of type-checking. It aims to be pretty modular (DI for config and output engines via implicits) yet use the same syntax for both real-time and offline processing. Efficiency is a secondary goal, though even in current form some mutable data structures are used to avoid performance snags of constant memory allocation.

###Modules
* core -- engine utils & a bunch of unitgens/processors based on traditional buffer-by-buffer processing
* [scalaudioAMP](https://github.com/auroboros/scalaudio/tree/master/scalaudio-amp) -- unitgens/processors focused on sample-by-sample processing & based on immutable messaging pipelines for state simplification

###Getting started

#####Single channel noise generator example
```scala
object MyFirstComposition extends App with ScalaudioSyntaxHelpers {
  implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

  val noiseGen = NoiseGen()
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

val anonUnitGen = new FuncGen(testBufferFunc)
anonUnitGen.play(1000 buffers)
```
Probably the core thesis of this library is the bufferFunc unit gen. This is a very tiny convenience wrapper that represents an anonymous unit gen. What is the point? Well, the point is really that an entire signal chain can be viewed as an anonymous unit generator. The output collection component (AudioTimeline) is essentially just looking for a parameterless function it can call repeatedly to generate buffers. As long as the signal chain ends with a component that has such a signature (represented by the abstract "outputBuffers()" signature in AudioTimeline), it can fit into the playback engine. Originally I had developed a method of constructing a SignalChain unit gen that was essentially the combination of unitgens/filters, but it seemed silly to prescribe an arbitrary shape to this structure where a lot of flexibility would typically be desired. Therefore, this was replaced with the FuncGen, a simple wrapper that accepts a constructor arg for a function that generates signal output.
The implementation is pretty simple --
```scala
case class FuncGen(bufferFunc : () => MultichannelAudio) extends UnitGen {
  override def computeBuffer(params : Option[UnitParams] = None) = internalBuffers = bufferFunc()
}
```
###why?
This project was initially created to facilitate my own computer music composition & audio machine learning projects but I decided to open source it since I only get to work on it in spurts (between my dayjob, other music composition, and toying around with audio in Clojure... some lib from that venture will probably be appearing here soon as well).

It was loosely inspired by Phil Burk's excellent library JSyn (in fact, JSyn is still a dependency as scalaudio borrows some of its utils, though this dependency will likely be cut in a future release).

###contribute!
Please contribute & get in touch, it will make me way more likely to work on this project regularly instead of just doing some periodic commits between bouts of screwing around with machine learning & Clojure, heh.

There are some big holes in the codebase (higher-level composition, automating parameters handling signal vs. control rate, & more!) and I have ideas as to how to tackle them but would appreciate help & input if anyone's got the time.

##Buffer-Based Signal Processing Engine
This module includes synthesis components from the first design of the engine, which is targeted at traditional buffer-based processing (the engine's inner loop expects a full audio buffer to pass to the system audio output). This may provide some efficiency gains but introduces some impedance between sample-rate and control-rate signals (control rate is applied once per buffer). Special care must be taken since the master clock is basically jumping at intervals of 1 buffer (introduces issues like "how do we handle real-time input? add it to a queue & process with the next buffer? create an internal clock within the generators that uses a finer time resolution?"). The introduction of the AMP module was largely to experiment with veering away from this non-ideal approach, with the mutable piece of that library (not yet released) intended to regain some of the performance benefits of mutable data / caching.
#####BufferSyntax
This trait includes a bunch of implicit conversions and other useful syntactic sugar, and is basically meant to be mixed in with your main composition script (it is implied in above examples; see: all tests). At the time of writing, BufferSyntax includes
  * "Signal Flow" syntax ("feed" & "mix", via implicit conversion to ChannelSetManipulator)
  * "Timing Events" syntax
 
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

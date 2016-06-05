#Core Components
##ScalaudioConfig
ScalaudioConfig contains a bunch of the parameters relevant to system configuration. Many are experimental but here is a sampling of some of the currently most relevant pieces... Defaults are supplied for all, so this can be constructed only explicitly specifying the values the user wishes to override.
```scala
case class ScalaudioConfig(beatsPerMinute: Double = 120,
                           beatsPerMeasure: Double = 4,
                           autoStartStop: Boolean = true,
                           framesPerBuffer: Int = 32,
                           nOutChannels: Int = 2,
                           nInChannels: Int = 1,
                           samplingRate: Int = 44100,
                           fftBufferSize: Int = 32,
                           ...
```                           
#####AudioContext
An AudioContext is basically an operating workspace pinned to a particular Scalaudio config. If no config is passed in, the default config will be used. This context should usually be defined implicitly, as most of the library's functions accept it implicitly and pass it transparently through inner scopes.
```scala
implicit val audioContext = AudioContext()
```
##Types
To make signal flow more comprehensible (though arguably making naive code-reading less clear), a number of type aliases are introduced for different types of signal.
```scala
package object types {
  type Sample = Double
  type Frame = List[Sample]

  type AudioSignal = Array[Double]
  type AudioRate[T] = Array[T]

  type ControlRate[T] = T

  type Signal[T] = Either[ControlRate[T], AudioRate[T]]

  type MultichannelAudio = List[AudioSignal]
}
```
#####AudioDuration
AudioDuration is similar to Scala's FiniteDuration but supports more audio terminology (beats, measures, buffers, samples). This allows for us to re-express the duration in different terms as is convenient (with whole samples being the finest resolution). AudioDurations can be converted to/from FiniteDuration pretty easily, so typical durations ("5 seconds") can be passed in most places where AudioDurations are required and will be converted implicitly via methods in ScalaudioSyntaxHelpers.
#####Pitch
Pitch is basically the frequency domain equivalent of audio duration, though it is less developed and has some caveats to consider (bi-directional conversion issues due to the discrete nature of notation, for example).
##CoreSyntax
This trait includes a bunch of implicit conversions and other useful syntactic sugar, and is basically meant to be mixed in with your main composition script (it is implied in above examples; see: all tests). In the core this includes
  * Durations (implicit conversions to AudioDuration)
  * Pitch (implicit conversions to Pitch)
  * Default output engines

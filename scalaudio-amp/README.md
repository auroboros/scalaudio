## scalaudioAMP

AMP (audio messaging pipeline) is intended to be a POC of stateless of a more immutable signal processing paradigm but will also introduce some mutable modules that follow a similar paradigm but are "minimally stateful" to avoid unnecessary re-computations.

```scala
implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

var state = SineState(0, 440.Hz, 0)

val frameFunc = () => {
  state = SineStateGen.nextState(state)
  List(state.sample)
}

FrameFuncAmpOutput(frameFunc).play(5 seconds)
```

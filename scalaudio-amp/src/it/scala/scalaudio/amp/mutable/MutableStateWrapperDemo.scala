package scalaudio.amp.mutable

import scala.concurrent.duration._
import scalaudio.amp.immutable.filter.{DelayFilterState, DelayFilterStateGen}
import scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import scalaudio.core.engine.samplewise.AmpOutput
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}
/**
  * Created by johnmcgill on 7/11/16.
  */
class MutableStateWrapperDemo extends ScalaudioCoreTestHarness {
  "MutableStateWrapper" should "produce sine without var in outer scope" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val wrappedSine = new MutableStateWrapper[OscState](SineStateGen, OscState(0, 440.Hz, 0))

    val frameFunc = () => {
      Array(wrappedSine.nextState().sample)
    }

    AmpOutput(frameFunc).play(5.seconds)
  }

  "MutableStateWrapper" should "use pre-transformer for automation" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val preTransformer = (s : OscState) => s.copy(
      pitch = (s.pitch.toHz + .2).Hz
    )
    val wrappedSine = new MutableStateWrapper[OscState](SineStateGen,
      OscState(0, 440.Hz, 0),
      preTransformer)


    val frameFunc = () => {
      Array(wrappedSine.nextState().sample)
    }

    AmpOutput(frameFunc).play(5 seconds)
  }

  "MutableStateWrapper" should "be chainable with stateful filters" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val wrappedSine = new MutableStateWrapper[OscState](SineStateGen, OscState(0, 440.Hz, 0))
    val wrappedDelay = new MutableStateWrapper[DelayFilterState](DelayFilterStateGen,
      DelayFilterStateGen.initialState(1.second),
      _.copy(sample = wrappedSine.state.sample)
    )

    val frameFunc = () => {
      wrappedSine.nextState()
      Array(wrappedDelay.nextState().sample)
    }

    AmpOutput(frameFunc).play(5.seconds)
  }
}

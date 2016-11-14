package scalaudio.amp.mutable

import scala.concurrent.duration._
import scalaudio.amp.immutable.filter.{DelayFilterState, DelayFilterStateGen}
import scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import scalaudio.core.engine.samplewise.AmpOutput
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

/**
  * Created by johnmcgill on 7/11/16.
  */
class MutableAudioStateWrapperDemo extends ScalaudioCoreTestHarness {
  "MutableStateWrapper" should "be chainable with chain-op!" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val wrappedSine = MutableAudioStateWrapper[OscState](SineStateGen, OscState(0, 440.Hz, 0))
    val wrappedDelay = MutableAudioStateWrapper[DelayFilterState](DelayFilterStateGen,
      DelayFilterStateGen.initialState(1.second))

    val chainedUnits = wrappedSine ~> wrappedDelay

    val frameFunc = () => {
      Array(chainedUnits.nextState().sample)
    }

    AmpOutput(frameFunc).play(5.seconds)
  }
}

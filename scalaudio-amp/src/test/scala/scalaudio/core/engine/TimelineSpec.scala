package scalaudio.core.engine

import scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import scalaudio.core.engine.samplewise.AmpOutput
import scalaudio.core.{AudioContext, ScalaudioCoreTestHarness}

/**
  * Created by johnmcgill on 6/15/16.
  */
class TimelineSpec extends ScalaudioCoreTestHarness {
  "Continuous play" should "play to expected point" in {
    implicit val audioContext : AudioContext = AudioContext()

    var oscState = OscState(0, 440.Hz, 0)
    var counter = 0

    val frameFunc = () => {
      oscState = SineStateGen.nextState(oscState)
      counter += 1
      Array(oscState.sample)
    }

    AmpOutput(frameFunc).playWhile(() => counter < 100)

    audioContext.currentTime.toSamples shouldEqual 100
  }
}

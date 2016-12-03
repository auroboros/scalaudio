package scalaudio.core.engine

import scalaudio.amp.immutable.ugen.{OscState, Sine}
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

/**
  * Created by johnmcgill on 6/15/16.
  */
class TimelineSpec extends ScalaudioCoreTestHarness {
  "Continuous play" should "play to expected point" in {
    implicit val audioContext : AudioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var oscState = OscState(0, 440.Hz, 0)
    var counter = 0

    val frameFunc = () => {
      oscState = Sine.nextState(oscState)
      counter += 1
      Array(oscState.sample)
    }

    StreamCollector(frameFunc).playWhile(() => counter <= 3000)

    audioContext.currentTime.toSamples shouldEqual 3000
  }
}

package scalaudio.units.ugen

import scalaudio.core.{AudioContext, ScalaudioCoreTestHarness}

/**
  * Created by johnmcgill on 7/10/16.
  */
class PhasorSpec extends ScalaudioCoreTestHarness {
  "1 Hz Phasor" should "output 0 to 2 pi over 1 second + 1 sample" in {
    implicit val audioContext = AudioContext()

    val oscState = OscState(0, 1.Hz, 0)

    val secondPlusOneOfStates = (1 to audioContext.config.samplingRate + 1).scanLeft(oscState){(curr, acc) =>
      Phasor.immutable.nextState(curr)
    }.tail

    secondPlusOneOfStates.length shouldEqual (audioContext.config.samplingRate + 1)
    secondPlusOneOfStates.head.sample shouldEqual 0
    Math.abs(secondPlusOneOfStates.last.sample - 2 * Math.PI) should be < .000001
  }
}

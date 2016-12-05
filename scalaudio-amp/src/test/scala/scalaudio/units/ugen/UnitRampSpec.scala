package scalaudio.units.ugen

import scalaudio.core.{AudioContext, ScalaudioCoreTestHarness}

/**
  * Created by johnmcgill on 7/10/16.
  */
class UnitRampSpec extends ScalaudioCoreTestHarness {
  "1 Hz unit ramp" should "output 0 to 1 over 1 second + 1 sample" in {
    implicit val audioContext = AudioContext()

    val oscState = new OscState(0, 1.Hz, 0)

    val secondPlusOneOfStates = (1 to audioContext.config.samplingRate + 1).scanLeft(oscState){(curr, acc) =>
      UnitRamp.nextState(curr)
    }.tail

    secondPlusOneOfStates.length shouldEqual (audioContext.config.samplingRate + 1)
    secondPlusOneOfStates.head.sample shouldEqual 0
    Math.abs(secondPlusOneOfStates.last.sample - 1) should be < .000001
  }
}

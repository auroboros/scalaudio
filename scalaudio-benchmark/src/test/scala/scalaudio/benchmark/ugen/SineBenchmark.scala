package scalaudio.benchmark.ugen

import java.time.Instant

import scala.concurrent.duration._
import scalaudio.core.engine.StreamCollector
import scalaudio.core.engine.io.SpeedTestDummy
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}
import scalaudio.units.ugen.{OscState, Sine}

/**
  * Created by johnmcgill on 6/6/16.
  */
class SineBenchmark extends ScalaudioCoreTestHarness {

  "scalaudioAMP" should "clock sine production" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var state: OscState = OscState(0, 440.Hz, 0)

    val frameFunc = () => {
      state = Sine.nextState(state)
      Array(state.sample)
    }

    val start = Instant.now.toEpochMilli
    StreamCollector(frameFunc, Some(List(SpeedTestDummy()))).play(5 hours)
    val end = Instant.now.toEpochMilli

    println((end - start).millis)

    // 10 mins
    // withGain: 4050 without: 4170

    // 5 hrs
    // 430568 (430 secs) (6.5 mins)
  }
}

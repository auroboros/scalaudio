package com.scalaudio.benchmark.ugen

import java.time.Instant

import com.scalaudio.actor.mutable.filter.GainActor
import com.scalaudio.actor.mutable.ugen.SineActor
import com.scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import com.scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}
import com.scalaudio.core.engine.{OutputEngine, SpeedTestDummy}
import com.scalaudio.core.engine.samplewise.FrameFuncAmpOutput

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 6/6/16.
  */
class SineBenchmark extends ScalaudioCoreTestHarness {
  implicit val engines: List[OutputEngine] = List(SpeedTestDummy())

  "scalaudioAMP" should "clock sine production" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1, autoStartStop = false))

    var state: OscState = OscState(0, 440.Hz, 0)

    val frameFunc = () => {
      state = SineStateGen.nextState(state)
      Array(state.sample * .7)
    }

    val start = Instant.now.toEpochMilli
    FrameFuncAmpOutput(frameFunc).play(10 minutes)(audioContext, engines)
    val end = Instant.now.toEpochMilli

    println((end - start).millis) // 4050
  }

  "scalaudioActor" should "clock sine production" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1, autoStartStop = false))

    val sineActor = new SineActor(440.Hz)
    val gainActor = new GainActor(.7)

    val frameFunc = () => {
      gainActor.filter(sineActor.nextFrame())
    }

    val start = Instant.now.toEpochMilli
    FrameFuncAmpOutput(frameFunc).play(10 minutes)(audioContext, engines)
    val end = Instant.now.toEpochMilli

    println((end - start).millis) // 6740
  }
}

package com.scalaudio.actor.mutable.ugen

import com.scalaudio.core.engine.samplewise.FrameFuncAmpOutput
import com.scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}

import scala.concurrent.duration._
/**
  * Created by johnmcgill on 6/6/16.
  */
class SineActorDemo extends ScalaudioCoreTestHarness {
  "Sine actor" should "produce sine audio output" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sineActor = new SineActor(480.Hz)

    val frameFunc = () => {
      List(sineActor.nextSample())
    }

    FrameFuncAmpOutput(frameFunc).play(5 seconds)
  }
}

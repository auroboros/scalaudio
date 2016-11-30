package scalaudio.actor.mutable.ugen

import scala.concurrent.duration._
import scalaudio.core.engine.StreamCollector
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}
/**
  * Created by johnmcgill on 6/6/16.
  */
class SineActorDemo extends ScalaudioCoreTestHarness {
  "Sine actor" should "produce sine audio output" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val sineActor = new SineActor(440.Hz)

    StreamCollector(Array(sineActor.nextSample())).play(5 seconds)
  }
}

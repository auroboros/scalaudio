package scalaudio.amp.mutable

import signalz.StatefulProcessor

import scala.concurrent.duration._
import scalaudio.core.{AudioContext, ScalaudioConfig, ScalaudioCoreTestHarness}
import scalaudio.units.AmpSyntax
import scalaudio.units.filter.{DelayFilterState, SimpleDelay}
import scalaudio.units.ugen.{ImmutableSine, OscState, Sine}
import scalaz.Scalaz._

/**
  * Created by johnmcgill on 7/11/16.
  */
class StatefulProcessorDemo extends ScalaudioCoreTestHarness with AmpSyntax {
  "StatefulProcessor" should "produce sine without var in outer scope" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val ff = Sine.immutable.asFunction(OscState(0, 440.Hz, 0))
      .map(state => Array(state.sample))

    playback(ff, 5.seconds)
  }

  "StatefulProcessor" should "use pre-transformer for automation" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val preTransformer = (s: OscState, u: Unit) => s.copy(
      pitch = (s.pitch.toHz + .2).Hz
    )
    val ff = Sine.immutable.asFunction.withModifier(
      OscState(0, 440.Hz, 0),
      preTransformer
    ) map (state => Array(state.sample))

    playback(ff, 5 seconds)
  }

  "StatefulProcessors" should "be chainable" in {

    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val delayFilter = SimpleDelay.immutable.asFunction.withModifier(
      SimpleDelay.initialState(3.seconds),
      (delayFilterState: DelayFilterState, newSample: Double) => delayFilterState.copy(sample = newSample)
    )

    val ff = Sine.immutable.asFunction(OscState(0, 440.Hz, 0))
      .map(_.sample)
      .map(delayFilter)
      .map(_.sample)
      .map(Array(_))

    playback(ff, 5.seconds)
  }
}

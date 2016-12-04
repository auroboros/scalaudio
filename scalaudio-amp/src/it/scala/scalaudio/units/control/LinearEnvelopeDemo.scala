package scalaudio.units.control

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.TreeMap
import scala.concurrent.duration._
import scalaudio.units.filter.GainFilter
import scalaudio.units.ugen.{OscState, Square}
import scalaudio.core.engine.StreamCollector
import scalaudio.core.types.AudioDuration
import scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
/**
  * Created by johnmcgill on 5/29/16.
  */
class LinearEnvelopeDemo extends FlatSpec with Matchers with CoreSyntax {
  "Square wave with linear env" should "ramp up over 5 seconds" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var envState = EnvelopeState(0, TreeMap(
      (1.second : AudioDuration) -> LinearEnvelope(0, 1, 5.seconds)
    ))
    var sineState = OscState(0, 660.Hz, 0)

    val frameFunc = () => {
      sineState = Square.nextState(sineState)
      envState = Envelope.nextState(envState)
      GainFilter.applyGainToFrame(envState.value)(Array(sineState.sample))
    }

    StreamCollector(frameFunc).play(7 seconds)
  }

  "Square wave with multiple ramps/points" should "fluctuate accordingly" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var envState = EnvelopeState(0, TreeMap(
      (1.second : AudioDuration) -> LinearEnvelope(0, 1, 5.seconds),
      (9.seconds : AudioDuration) -> LinearEnvelope(1, .2, 3.seconds),
      (13.seconds : AudioDuration) -> PointEnvelope(.85)
    ))
    var sineState = OscState(0, 660.Hz, 0)

    val frameFunc = () => {
      sineState = Square.nextState(sineState)
      envState = Envelope.nextState(envState)
      GainFilter.applyGainToFrame(envState.value)(Array(sineState.sample))
    }

    StreamCollector(frameFunc).play(15 seconds)
  }
}

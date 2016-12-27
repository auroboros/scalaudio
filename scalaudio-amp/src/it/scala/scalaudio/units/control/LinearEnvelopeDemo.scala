package scalaudio.units.control

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.TreeMap
import scala.concurrent.duration._
import scalaudio.core.types.{AudioDuration, Frame}
import scalaudio.core.{AudioContext, ScalaudioConfig}
import scalaudio.units.AmpSyntax
import scalaudio.units.filter.GainFilter
import scalaudio.units.ugen.{OscState, Square}
import scalaz.Scalaz._
import scalaz._

/**
  * Created by johnmcgill on 5/29/16.
  */
class LinearEnvelopeDemo extends FlatSpec with Matchers with AmpSyntax {

  // MUTABLE
  "Square wave with mutable linear env" should "ramp up over 5 seconds" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    val envQueue = List(
      TimedEnvelopeSegment(1.second: AudioDuration, LinearEnvelope(0, 1, 5.seconds))
    )

    val envelopeFunc: (Unit) => (Frame) => Frame = Envelope(envQueue).asReflexiveFunction().map(_._1)
      .map(g => GainFilter.applyGainToFrame(g)(_))

    playback(
      Square.immutable.asFunction(OscState(0, 660.Hz, 0)).map(o => Array(o.sample))
        .map(frame => envelopeFunc()(frame)),
      7 seconds
    )
  }

  // IMMUTABLE

  "Square wave with linear env" should "ramp up over 5 seconds" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var envState = EnvelopeState(0, TreeMap(
      (1.second: AudioDuration) -> LinearEnvelope(0, 1, 5.seconds)
    ))
    var sineState = OscState(0, 660.Hz, 0)

    val frameFunc = () => {
      sineState = Square.immutable.nextState(sineState)
      envState = Envelope.immutable.nextState(envState)
      GainFilter.applyGainToFrame(envState.value)(Array(sineState.sample))
    }

    playback(frameFunc, 7 seconds)
  }

  "Square wave with multiple ramps/points" should "fluctuate accordingly" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var envState = EnvelopeState(0, TreeMap(
      (1.second: AudioDuration) -> LinearEnvelope(0, 1, 5.seconds),
      (9.seconds: AudioDuration) -> LinearEnvelope(1, .2, 3.seconds),
      (13.seconds: AudioDuration) -> PointEnvelope(.85)
    ))
    var sineState = OscState(0, 660.Hz, 0)

    val frameFunc = () => {
      sineState = Square.immutable.nextState(sineState)
      envState = Envelope.immutable.nextState(envState)
      GainFilter.applyGainToFrame(envState.value)(Array(sineState.sample))
    }

    playback(frameFunc, 15 seconds)
  }
}

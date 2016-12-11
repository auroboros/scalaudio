package scalaudio.units.ugen

import org.scalatest.{FlatSpec, Matchers}
import signalz.StatefulProcessor

import scala.concurrent.duration._
import scalaudio.core.AudioContext
import scalaudio.core.types.{Frame, Pitch}
import scalaudio.units.AmpSyntax
import scalaudio.units.filter.{RangeScaler, Rescaler}
import scalaz.Scalaz._

/**
  * Created by johnmcgill on 8/1/16.
  */
class FmSynthDemo extends FlatSpec with Matchers with AmpSyntax {
  "FM synth" should "be possible using 2 sine synths" in {
    implicit val audioContext = AudioContext()

    // try 0 -> 300, 0 -> 10000, 10000 -> 110000, 500 -> 600

    val ff = fmSynthesis(66.Hz)

    playback(ff, 15.seconds)
  }

  def fmSynthesis(controlFreq: Pitch): () => Frame = {

    val sinGen2 = StatefulProcessor.withModifier[OscState, Pitch](Sine.immutable.nextState,
      OscState(0, 0.Hz, 0),
      (oscState, pitch) => oscState.copy(pitch = pitch)
    )

    StatefulProcessor[OscState](Sine.immutable.nextState,
      OscState(0, controlFreq, 0)
    ).nextState map (_.sample) map (RangeScaler.scale(Rescaler(-1, 1, 0, 300)) _)
      .map(_.Hz)
      .map(sinGen2.nextState)
      .map(_.sample)
      .map(s => Array.fill(2)(s))
  }
}

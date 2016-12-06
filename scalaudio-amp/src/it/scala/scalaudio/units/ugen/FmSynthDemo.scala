package scalaudio.units.ugen

import org.scalatest.{FlatSpec, Matchers}
import signalz.StatefulProcessor

import scala.concurrent.duration._
import scalaudio.core.engine.FunctionGraphTimeline
import scalaudio.core.types.Pitch
import scalaudio.core.{AudioContext, CoreSyntax}
import scalaudio.units.filter.{RangeScaler, Rescaler}
import scalaz.Scalaz._

/**
  * Created by johnmcgill on 8/1/16.
  */
class FmSynthDemo extends FlatSpec with Matchers with CoreSyntax {
  "FM synth" should "be possible using 2 sine synths" in {
    implicit val audioContext = AudioContext()

    // try 0 -> 300, 0 -> 10000, 10000 -> 110000, 500 -> 600

    //    var collector: Double = null

    val sinGen2 = StatefulProcessor.withModifier[OscState, Pitch](Sine.nextState,
      OscState(0, 0.Hz, 0),
      (oscState, pitch) => oscState.copy(pitch = pitch)
    )

    val ff: () => Array[Double] = StatefulProcessor[OscState](Sine.nextState,
      OscState(0, 66.Hz, 0)
    ).nextState map (_.sample) map (RangeScaler.scale(Rescaler(-1, 1, 0, 300)) _)
      .map(_.Hz)
      .map(sinGen2.nextState)
      .map(_.sample)
      .map(s => Array.fill(2)(s))

    FunctionGraphTimeline(ff).play(15.seconds)
  }
}

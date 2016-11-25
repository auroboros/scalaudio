package scalaudio.amp.immutable.ugen

import org.scalatest.{FlatSpec, Matchers}
import signalz.StatefulProcessor

import scala.concurrent.duration._
import scalaudio.amp.immutable.filter.{RangeScaler, Rescaler}
import scalaudio.core.engine.samplewise.AmpOutput
import scalaudio.core.{AudioContext, CoreSyntax}
import scalaz._
import Scalaz._

/**
  * Created by johnmcgill on 8/1/16.
  */
class FmSynthDemo extends FlatSpec with Matchers with CoreSyntax {
  "FM synth" should "be possible using 2 sine synths" in {
    implicit val audioContext = AudioContext()

    // try 0 -> 300, 0 -> 10000, 10000 -> 110000, 500 -> 600

//    var collector: Double = null

    var holder: Double = 0 // TODO: there MUST be a better way to solve this with currying, multiple param lists
    val sinGen2 = StatefulProcessor(SineStateGen.nextState,
      OscState(0, 0.Hz, 0),
      Some(
        (oscState: OscState) => oscState.copy(pitch = holder.Hz)
      )
    )

    val ff: () => Array[Double] = StatefulProcessor(SineStateGen.nextState,
      OscState(0, 66.Hz, 0)
    ).nextState map (_.sample) map (RangeScaler.scale(Rescaler(-1, 1, 0, 300)) _)
      .map { (scaledOutput) =>
        holder = scaledOutput
        sinGen2.nextState().sample
      } map (s => Array.fill(2)(s))

    AmpOutput(ff).play(15.seconds)
  }
}

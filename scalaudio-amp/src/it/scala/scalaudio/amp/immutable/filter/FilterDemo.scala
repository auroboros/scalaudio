package scalaudio.amp.immutable.filter

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import scalaudio.core.engine.samplewise.AmpOutput
import scalaudio.core.types.Frame
import scalaudio.core.{AudioContext, CoreSyntax}
/**
  * Created by johnmcgill on 5/29/16.
  */
class FilterDemo extends FlatSpec with Matchers with CoreSyntax {
  "Gain & splitter" should "be chainable on sine" in {
    implicit val audioContext = AudioContext()

    var sineState = OscState(0, 440.Hz, 0)
    var splitterOut : Frame = Array.empty[Double]

    val frameFunc = () => {
      sineState = SineStateGen.nextState(sineState)
      splitterOut = SplitFilter.split(sineState.sample, 2)
      GainFilter.applyGainToFrame(splitterOut, .05)
    }

    AmpOutput(frameFunc).play(5 seconds)
  }
}

package scalaudio.units.ugen

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import scalaudio.units.AmpSyntax
/**
  * Created by johnmcgill on 5/29/16.
  */
class SquareStateGenDemo extends FlatSpec with Matchers with AmpSyntax {
  "Square state gen" should "produce squarewave audio output" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var state : OscState = OscState(0, 440.Hz, 0)

    val frameFunc = () => {
      state = Square.immutable.nextState(state)
      Array(state.sample)
    }

    playback(frameFunc, 2 seconds)
  }
}

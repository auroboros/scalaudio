package scalaudio.amp.immutable.ugen

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scalaudio.core.engine.StreamCollector
import scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}

/**
  * Created by johnmcgill on 5/29/16.
  */
class SawtoothStateGenDemo extends FlatSpec with Matchers with CoreSyntax {
  "Sawtooth state gen" should "produce sine audio output" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var state : OscState = OscState(0, 440.Hz, 0)

    val frameFunc = () => {
      state = Sawtooth.nextState(state)
      println(state.sample)
      Array(state.sample)
    }

    StreamCollector(frameFunc).play(5 seconds)
  }
}

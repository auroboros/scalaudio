package scalaudio.units.ugen

import org.scalatest.{FlatSpec, Matchers}

import scalaudio.core.{AudioContext, CoreSyntax}

/**
  * Created by johnmcgill on 5/27/16.
  */
class SineSpec extends FlatSpec with Matchers with CoreSyntax{
  "Sine state gen" should "be scriptable as a ugen" in {
    implicit val audioContext = AudioContext()

    var state = OscState(0, 440.Hz, 0)

    1 to 1000 foreach {_ =>
      state = ImmutableSine.nextState(state)
      println(state)
    }
  }
}
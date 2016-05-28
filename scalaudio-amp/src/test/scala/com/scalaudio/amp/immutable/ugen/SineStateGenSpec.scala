package com.scalaudio.amp.immutable.ugen

import com.scalaudio.AudioContext
import com.scalaudio.syntax.ScalaudioSyntaxHelpers
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by johnmcgill on 5/27/16.
  */
class SineStateGenSpec extends FlatSpec with Matchers with ScalaudioSyntaxHelpers{
  "Sine state gen" should "be scriptable as a ugen" in {
    implicit def audioContext = AudioContext()

    var state = SineState(0, 440.Hz, 0)

    1 to 1000 foreach {_ =>
      state = SineStateGen.nextState(state)
      println(state)
    }
  }
}

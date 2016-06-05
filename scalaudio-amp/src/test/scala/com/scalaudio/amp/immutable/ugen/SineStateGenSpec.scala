package com.scalaudio.amp.immutable.ugen

import com.scalaudio.amp.engine.FrameFuncAmpOutput
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 5/27/16.
  */
class SineStateGenSpec extends FlatSpec with Matchers with CoreSyntax{
  "Sine state gen" should "be scriptable as a ugen" in {
    implicit val audioContext = AudioContext()

    var state = OscState(0, 440.Hz, 0)

    1 to 1000 foreach {_ =>
      state = SineStateGen.nextState(state)
      println(state)
    }
  }
}
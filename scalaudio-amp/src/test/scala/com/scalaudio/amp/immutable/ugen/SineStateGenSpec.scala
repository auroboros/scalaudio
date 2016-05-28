package com.scalaudio.amp.immutable.ugen

import com.scalaudio.amp.engine.FrameFuncAmpOutput
import com.scalaudio.core.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.core.{AudioContext, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._

/**
  * Created by johnmcgill on 5/27/16.
  */
class SineStateGenSpec extends FlatSpec with Matchers with ScalaudioSyntaxHelpers{
  "Sine state gen" should "be scriptable as a ugen" in {
    implicit val audioContext = AudioContext()

    var state = SineState(0, 440.Hz, 0)

    1 to 1000 foreach {_ =>
      state = SineStateGen.nextState(state)
      println(state)
    }
  }

  "Sine state gen" should "produce sine audio output" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var state = SineState(0, 440.Hz, 0)

    val frameFunc = () => {
      state = SineStateGen.nextState(state)
      List(state.sample)
    }

    FrameFuncAmpOutput(frameFunc).play(5 seconds)
  }
}

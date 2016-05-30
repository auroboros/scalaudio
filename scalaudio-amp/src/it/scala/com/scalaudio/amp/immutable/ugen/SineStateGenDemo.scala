package com.scalaudio.amp.immutable.ugen

import com.scalaudio.amp.engine.FrameFuncAmpOutput
import com.scalaudio.core.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.core.{AudioContext, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
/**
  * Created by johnmcgill on 5/29/16.
  */
class SineStateGenDemo extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Sine state gen" should "produce sine audio output" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var state : OscState = OscState(0, 440.Hz, 0)

    val frameFunc = () => {
      state = SineStateGen.nextState(state)
      List(state.sample)
    }

    FrameFuncAmpOutput(frameFunc).play(5 seconds)
  }
}
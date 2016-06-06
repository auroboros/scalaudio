package com.scalaudio.amp.immutable.ugen

import com.scalaudio.core.engine.samplewise.FrameFuncAmpOutput
import com.scalaudio.core.{AudioContext, CoreSyntax, ScalaudioConfig}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
/**
  * Created by johnmcgill on 5/29/16.
  */
class SquareStateGenDemo extends FlatSpec with Matchers with CoreSyntax {
  "Square state gen" should "produce squarewave audio output" in {
    implicit val audioContext = AudioContext(ScalaudioConfig(nOutChannels = 1))

    var state : OscState = OscState(0, 440.Hz, 0)

    val frameFunc = () => {
      state = SquareStateGen.nextState(state)
      List(state.sample)
    }

    FrameFuncAmpOutput(frameFunc).play(2 seconds)
  }
}

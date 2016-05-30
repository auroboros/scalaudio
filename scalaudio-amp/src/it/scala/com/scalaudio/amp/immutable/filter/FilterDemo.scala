package com.scalaudio.amp.immutable.filter

import com.scalaudio.amp.engine.FrameFuncAmpOutput
import com.scalaudio.amp.immutable.ugen.{OscState, SineStateGen}
import com.scalaudio.core.AudioContext
import com.scalaudio.core.syntax.ScalaudioSyntaxHelpers
import com.scalaudio.core.types._
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
/**
  * Created by johnmcgill on 5/29/16.
  */
class FilterDemo extends FlatSpec with Matchers with ScalaudioSyntaxHelpers {
  "Gain & splitter" should "be chainable on sine" in {
    implicit val audioContext = AudioContext()

    var sineState = OscState(0, 440.Hz, 0)
    var splitterOut : Frame = Nil

    val frameFunc = () => {
      sineState = SineStateGen.nextState(sineState)
      splitterOut = SplitFilter.split(sineState.sample, 2)
      GainFilter.applyGain(splitterOut, .05)
    }

    FrameFuncAmpOutput(frameFunc).play(5 seconds)
  }
}

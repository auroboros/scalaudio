package com.scalaudio.amp.immutable.ugen

import com.scalaudio.amp.immutable.filter.{RangeScaler, Rescaler}
import com.scalaudio.amp.mutable.{MutableAudioStateWrapper, MutableStateWrapper}
import com.scalaudio.core.engine.samplewise.AmpOutput
import com.scalaudio.core.{AudioContext, CoreSyntax}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
/**
  * Created by johnmcgill on 8/1/16.
  */
class FmSynthDemo extends FlatSpec with Matchers with CoreSyntax {
  "FM synth" should "be possible using 2 sine synths" in {
    implicit val audioContext = AudioContext()

    val scale = RangeScaler.scale(Rescaler(-1, 1, 0, 300))(_)
    // try 0 -> 300, 0 -> 10000, 10000 -> 110000, 500 -> 600

    val sin1 = MutableAudioStateWrapper[OscState](SineStateGen,
      OscState(0, 66.Hz, 0), // 66 Hz
      postTransformer = (oscState: OscState) => oscState.copy(
        sample = scale(oscState.sample)
      )
    )

    val sin2 = MutableAudioStateWrapper[OscState](SineStateGen,
      OscState(0, 0.Hz, 0),
      preTransformer = (oscState: OscState) => oscState.copy(
        pitch = sin1.state.sample.Hz
      )
    )

    val frameFunc = () => {
      sin1.nextState()
      val outSample = sin2.nextState().sample
      Array.fill(2)(outSample)
    }

    AmpOutput(frameFunc).play(15.seconds)
  }
}

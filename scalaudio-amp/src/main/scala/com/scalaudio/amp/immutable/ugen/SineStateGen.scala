package com.scalaudio.amp.immutable.ugen

import com.scalaudio.core.AudioContext
import com.scalaudio.core.syntax.Pitch
import com.scalaudio.core.types.Sample

/**
  * Created by johnmcgill on 5/27/16.
  */
case class SineState(sample: Sample,
                     pitch: Pitch,
                     phi: Double)

object SineStateGen {
  def nextState(current: SineState)(implicit audioContext: AudioContext) : SineState = {
    val w = 2 * Math.PI * current.pitch.toHz / audioContext.config.samplingRate

    current.copy(
      sample = Math.sin(current.phi),
      phi = current.phi + w
    )
  }
}
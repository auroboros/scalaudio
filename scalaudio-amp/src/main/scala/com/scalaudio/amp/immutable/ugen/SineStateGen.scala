package com.scalaudio.amp.immutable.ugen

import com.scalaudio.AudioContext

/**
  * Created by johnmcgill on 5/27/16.
  */

object SineStateGen {
  def nextState(current: SineState)(implicit audioContext: AudioContext) : SineState = {
    val w = 2 * Math.PI * current.pitch.toHz / audioContext.config.samplingRate
    val phiInc = w * audioContext.config.framesPerBuffer

    current.copy(
      sample = Math.sin(w * current.phi),
      phi = current.phi + phiInc
    )
  }
}
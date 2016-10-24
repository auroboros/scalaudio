package com.scalaudio.amp.immutable.ugen

import com.scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 7/10/16.
  */
object UnitRamp extends OscStateGen {
  override def nextState(current: OscState)(implicit audioContext: AudioContext): OscState = {
    val w = current.pitch.toHz / audioContext.config.samplingRate

    current.copy(
      sample = current.phi,
      phi = current.phi + w // TODO: mod 1??
    )
  }
}
package com.scalaudio.amp.immutable.ugen
import com.scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 7/10/16.
  */
object Phasor extends OscStateGen {
  override def nextState(current: OscState)(implicit audioContext: AudioContext): OscState = {
    val w = 2 * Math.PI * current.pitch.toHz / audioContext.config.samplingRate

    current.copy(
      sample = current.phi,
      phi = current.phi + w
    )
  }
}
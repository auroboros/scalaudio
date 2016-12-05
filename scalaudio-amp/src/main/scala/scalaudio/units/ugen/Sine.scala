package scalaudio.units.ugen

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 5/27/16.
  */

object Sine extends Osc {
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState = {
    val w = 2 * Math.PI * current.pitch.toHz / audioContext.config.samplingRate

    current.copy(
      sample = Math.sin(current.phi),
      phi = current.phi + w
    )
  }
}
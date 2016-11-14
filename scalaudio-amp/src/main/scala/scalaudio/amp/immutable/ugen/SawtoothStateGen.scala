package scalaudio.amp.immutable.ugen

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 7/10/16.
  */
object SawtoothStateGen extends OscStateGen {
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState = {
    val w = 2 * Math.PI * current.pitch.toHz / audioContext.config.samplingRate

    current.copy(
      sample = Math.sin(current.phi).signum,
      phi = ((current.phi + w) % 1.0) * 2 - 1
    )
  }
}
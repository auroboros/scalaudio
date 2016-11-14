package scalaudio.amp.immutable.ugen

import com.scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 5/29/16.
  */

object SquareStateGen extends OscStateGen {
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState = {
    val w = 2 * Math.PI * current.pitch.toHz / audioContext.config.samplingRate

    current.copy(
      sample = Math.sin(current.phi).signum,
      phi = current.phi + w
    )
  }
}
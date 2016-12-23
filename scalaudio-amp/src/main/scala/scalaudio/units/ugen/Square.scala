package scalaudio.units.ugen

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 5/29/16.
  */
object Square {
  val immutable = new ImmutableSquare{}
}

trait ImmutableSquare extends ImmutableOsc {
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState = {
    val w = 2 * Math.PI * current.pitch.toHz / audioContext.config.samplingRate

    current.copy(
      sample = Math.sin(current.phi).signum,
      phi = current.phi + w
    )
  }
}
package scalaudio.units.ugen

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 7/10/16.
  */
object Phasor {
  val immutable = new ImmutablePhasor {}
}

trait ImmutablePhasor extends ImmutableOsc {
  override def nextState(current: OscState)(implicit audioContext: AudioContext): OscState = {
    val w = 2 * Math.PI * current.pitch.toHz / audioContext.config.samplingRate

    current.copy(
      sample = current.phi,
      phi = current.phi + w
    )
  }
}
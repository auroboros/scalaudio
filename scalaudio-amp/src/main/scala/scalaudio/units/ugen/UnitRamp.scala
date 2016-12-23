package scalaudio.units.ugen

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 7/10/16.
  */
object UnitRamp {
  val immutable = new ImmutableUnitRamp {}
}

trait ImmutableUnitRamp extends ImmutableOsc {
  override def nextState(current: OscState)(implicit audioContext: AudioContext): OscState = {
    val w = current.pitch.toHz / audioContext.config.samplingRate

    current.copy(
      sample = current.phi,
      phi = current.phi + w // TODO: mod 1??
    )
  }
}
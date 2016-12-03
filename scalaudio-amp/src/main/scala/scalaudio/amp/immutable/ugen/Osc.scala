package scalaudio.amp.immutable.ugen

import signalz.SequentialState

import scalaudio.core.AudioContext
import scalaudio.core.types.{Pitch, _}

/**
  * Created by johnmcgill on 5/29/16.
  */
case class OscState(sample: Sample,
                    pitch: Pitch,
                    phi: Double)

trait Osc extends SequentialState[OscState, AudioContext]{
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState
}
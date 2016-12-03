package scalaudio.amp.immutable.ugen

import signalz.SequentialState

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 5/29/16.
  */
trait Osc extends SequentialState[OscState, AudioContext]{
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState
}
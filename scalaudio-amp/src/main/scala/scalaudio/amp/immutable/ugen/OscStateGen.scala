package scalaudio.amp.immutable.ugen

import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 5/29/16.
  */
// TODO: Delete??
trait OscStateGen {
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState
}
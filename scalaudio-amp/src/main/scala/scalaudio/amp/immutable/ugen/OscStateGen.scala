package scalaudio.amp.immutable.ugen

import scalaudio.amp.immutable.StateProgressor
import scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 5/29/16.
  */
trait OscStateGen extends StateProgressor[OscState] {
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState
}
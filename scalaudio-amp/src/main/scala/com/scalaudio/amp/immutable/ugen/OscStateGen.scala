package com.scalaudio.amp.immutable.ugen

import com.scalaudio.amp.immutable.StateProgressor
import com.scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 5/29/16.
  */
trait OscStateGen extends StateProgressor[OscState] {
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState
}
package com.scalaudio.amp.immutable.ugen

import com.scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 5/29/16.
  */
trait UnitOsc {
  def nextState(current: OscState)(implicit audioContext: AudioContext) : OscState
}

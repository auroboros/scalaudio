package com.scalaudio.amp.immutable.filter

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.Frame

/**
  * Created by johnmcgill on 5/28/16.
  */

object GainFilter {
  // Don't use "StateGen" approach since gain is actually pretty stateless?
  def applyGain(frame: Frame, gain: Double)(implicit audioContext: AudioContext): Frame = frame.map(_ * gain)
}

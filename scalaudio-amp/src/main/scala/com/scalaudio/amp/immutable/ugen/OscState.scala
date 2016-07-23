package com.scalaudio.amp.immutable.ugen

import com.scalaudio.amp.immutable.AudioUnitState
import com.scalaudio.core.types.{Pitch, _}

/**
  * Created by johnmcgill on 5/29/16.
  */
case class OscState(sample: Sample,
                       pitch: Pitch,
                       phi: Double) extends AudioUnitState {
  def overwriteSample(sample: Sample): OscState = this.copy(sample = sample)
}
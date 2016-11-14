package scalaudio.amp.immutable.ugen

import com.scalaudio.core.types.{Pitch, _}

/**
  * Created by johnmcgill on 5/29/16.
  */
case class OscState(sample: Sample,
                       pitch: Pitch,
                       phi: Double) {

  def overwriteSample(s: Sample) = this.copy(sample = s)
}
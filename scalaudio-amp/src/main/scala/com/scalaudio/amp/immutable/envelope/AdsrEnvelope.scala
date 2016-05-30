package com.scalaudio.amp.immutable.envelope

import com.scalaudio.core.syntax.AudioDuration

import scala.collection.immutable.SortedMap

/**
  * Created by johnmcgill on 5/29/16.
  */
case class AdsrEnvelope(attackDuration : AudioDuration,
                        attackPeak : Double,
                        decayDuration : AudioDuration,
                        decayRestingPoint : Double,
                        sustainDuration : AudioDuration,
                        releaseDuration : AudioDuration) {

  def asLinearEnvelopes(startTime: AudioDuration) : SortedMap[AudioDuration, LinearEnvelope] = ???
}

package com.scalaudio.amp.immutable.envelope

import com.scalaudio.core.syntax.AudioDuration

import scala.collection.immutable.{SortedMap, TreeMap}

/**
  * Created by johnmcgill on 5/29/16.
  */
case class AdsrEnvelope(attackDuration: AudioDuration,
                        attackPeak: Double,
                        decayDuration: AudioDuration,
                        decayRestingPoint: Double,
                        sustainDuration: AudioDuration,
                        releaseDuration: AudioDuration) {

  def asLinearEnvelopes(startTime: AudioDuration): SortedMap[AudioDuration, LinearEnvelope] = {
    val decayStart = startTime + attackDuration
    val sustainStart = decayStart + decayDuration
    val releaseStart = sustainStart + sustainDuration

    TreeMap(
      startTime -> LinearEnvelope(0, attackPeak, attackDuration),
      decayStart -> LinearEnvelope(attackPeak, decayRestingPoint, decayDuration),
      releaseStart -> LinearEnvelope(decayRestingPoint, 0, releaseDuration)
    )
  }
}

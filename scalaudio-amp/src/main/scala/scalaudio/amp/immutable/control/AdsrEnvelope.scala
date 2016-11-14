package scalaudio.amp.immutable.control

import com.scalaudio.core.types.AudioDuration

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

  def asLinearEnvelopes(startTime: AudioDuration, startVal: Double = 0): SortedMap[AudioDuration, LinearEnvelope] = {
    val decayStart = startTime + attackDuration
    val sustainStart = decayStart + decayDuration
    val releaseStart = sustainStart + sustainDuration

    TreeMap(
      startTime -> LinearEnvelope(startVal, attackPeak, attackDuration),
      decayStart -> LinearEnvelope(attackPeak, decayRestingPoint, decayDuration),
      releaseStart -> LinearEnvelope(decayRestingPoint, 0, releaseDuration)
    )
  }
}

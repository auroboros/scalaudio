package scalaudio.units.control

import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 12/29/16.
  */
case class AdsrSegment(attackDuration: AudioDuration,
                       attackPeak: Double,
                       decayDuration: AudioDuration,
                       sustainValue: Double,
                       sustainDuration: AudioDuration,
                       releaseDuration: AudioDuration) extends EnvelopeSegment {

  // Relative times
  val decayStart = attackDuration
  val sustainStart = decayStart + decayDuration
  val releaseStart = sustainStart + sustainDuration

  val attackSegment = LinearEnvelope(0, attackPeak, attackDuration)
  val decaySegment = LinearEnvelope(attackPeak, sustainValue, decayDuration)
  val releaseSegment = LinearEnvelope(sustainValue, 0, releaseDuration)

  override def valueAtRelativeTime(relativeTime: AudioDuration): Double = if (relativeTime < decayStart) {
    attackSegment.valueAtRelativeTime(relativeTime)
  } else if (relativeTime < sustainStart) {
    decaySegment.valueAtRelativeTime(relativeTime - decayStart)
  } else if (relativeTime < releaseStart) {
    sustainValue
  } else {
    releaseSegment.valueAtRelativeTime(relativeTime - releaseStart)
  }

  override val length: AudioDuration = releaseStart + releaseDuration // TODO: Off-by-one? -1? Unit test for this... Same with start times
}


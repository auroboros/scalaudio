package scalaudio.units.control

import scalaudio.core.AudioContext
import scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 5/29/16.
  */
trait EnvelopeSegment {
  def valueAtRelativeTime(relativeTime: AudioDuration) : Double
  val length : AudioDuration

  def endTime(startTime: AudioDuration) : AudioDuration = startTime + length
}

case class LinearEnvelope(startVal: Double, endVal: Double, override val length: AudioDuration) extends EnvelopeSegment {
  override def valueAtRelativeTime(relativeTime: AudioDuration) : Double =
    AudioDuration.linearDurationInterpolation(relativeTime, length, startVal, endVal)
}

case class PointEnvelope(value: Double)(implicit val audioContext: AudioContext) extends EnvelopeSegment {
  val length = AudioDuration(0)
  override def valueAtRelativeTime(relativeTime: AudioDuration) : Double = value
}

case class TimedEnvelopeSegment(startTime: AudioDuration, envelopeSegment: EnvelopeSegment) {

  def valueAtTime(absoluteTime: AudioDuration) = envelopeSegment.valueAtRelativeTime(absoluteTime - startTime)

  def endTime = startTime + envelopeSegment.length
}
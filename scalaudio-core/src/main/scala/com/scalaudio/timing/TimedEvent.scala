package com.scalaudio.timing

import com.scalaudio.AudioContext
import com.scalaudio.syntax.AudioDuration

/**
  * Created by johnmcgill on 12/29/15.
  */
case class TimedEvent(val startTime: AudioDuration, val event: CapsuleEvent) {
  def endTime : AudioDuration = startTime + event.duration

  def endVal : Double = event.endVal
}

// SINGLE EVENTS
abstract class CapsuleEvent() { // "Gesture" a better name maybe?
  def valueAtRelativeTime(audioDuration: AudioDuration) : Double

  def duration : AudioDuration

  def endVal : Double
}

case class ValueChange(val value : Double)(implicit audioContext: AudioContext) extends CapsuleEvent {
  override val endVal = value
  override val duration : AudioDuration = AudioDuration(0)

  override def valueAtRelativeTime(relativeDuration : AudioDuration): Double = value
}

//TODO : Base this & ADSR on AudioDurations
case class ValueRamp(override val duration : AudioDuration, val startVal : Double, override val endVal : Double) extends CapsuleEvent {
  override def valueAtRelativeTime(relativeDuration: AudioDuration): Double =
    startVal + (endVal - startVal) * (relativeDuration.toSamples.toDouble / duration.toSamples)
}

case class ExpSweepingRamp()
case class LogSweepingRamp()

// COMPOSITE EVENTS
case class TimedCompositeEvent(val startTime: AudioDuration, val compositeEvent: CompositeCapsuleEvent)

abstract class CompositeCapsuleEvent() {
  def toTimedEventList(startTime : AudioDuration) : List[TimedEvent]
}

case class ADSRCurve(val attackDuration : AudioDuration, val attackPeak : Double, val decayDuration : AudioDuration, val decayRestingPoint : Double,
                     val sustainDuration : AudioDuration, val releaseDuration : AudioDuration) extends CompositeCapsuleEvent {

  override def toTimedEventList(startTime : AudioDuration) : List[TimedEvent] =
    List(TimedEvent(startTime, ValueRamp(attackDuration, 0, attackPeak)),
      TimedEvent(startTime + attackDuration, ValueRamp(decayDuration, attackPeak, decayRestingPoint)),
      TimedEvent(startTime + attackDuration + decayDuration + sustainDuration, ValueRamp(releaseDuration, decayRestingPoint, 0)))
}
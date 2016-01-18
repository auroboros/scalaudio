package com.scalaudio.timing

/**
  * Created by johnmcgill on 12/29/15.
  */
case class TimedEvent(val startFrame : Int, val event : CapsuleEvent){
  def endFrame : Int = startFrame + event.duration

  def endVal : Double = event.endVal
}

// SINGLE EVENTS
abstract class CapsuleEvent() { // "Gesture" a better name maybe?
  def valueAtRelativeFrame(relativeFrame : Int) : Double

  def duration : Int

  def endVal : Double
}

case class ValueChange(val value : Double) extends CapsuleEvent {
  override val endVal = value
  override val duration = 0
  override def valueAtRelativeFrame(relativeFrame : Int): Double = value
}

case class ValueRamp(override val duration : Int, val startVal : Double, override val endVal : Double) extends CapsuleEvent {
  override def valueAtRelativeFrame(relativeFrame : Int): Double =
    startVal + (endVal - startVal) * (relativeFrame.toDouble / duration)
}

case class ExpSweepingRamp()
case class LogSweepingRamp()

// COMPOSITE EVENTS
case class TimedCompositeEvent(val startFrame : Int, val compositeEvent : CompositeCapsuleEvent)

abstract class CompositeCapsuleEvent() {
  def toTimedEventList(startFrame : Int) : List[TimedEvent]
}

case class ADSRCurve(val attackDuration : Int, val attackPeak : Double, val decayDuration : Int, val decayRestingPoint : Double,
                     val sustainDuration : Int, val releaseDuration : Int) extends CompositeCapsuleEvent {

  override def toTimedEventList(startFrame : Int) : List[TimedEvent] =
    List(TimedEvent(startFrame, ValueRamp(attackDuration, 0, attackPeak)),
      TimedEvent(startFrame + attackDuration, ValueRamp(decayDuration, attackPeak, decayRestingPoint)),
      TimedEvent(startFrame + attackDuration + decayDuration + sustainDuration, ValueRamp(releaseDuration, decayRestingPoint, 0)))
}
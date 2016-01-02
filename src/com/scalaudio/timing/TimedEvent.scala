package com.scalaudio.timing

/**
  * Created by johnmcgill on 12/29/15.
  */
case class TimedEvent(val startFrame : Int, val event : CapsuleEvent){
  def toControlPointList : List[(Int, Double)] =
    event match {
      case ValueChange(v) => List((startFrame,v))
      case vr @ ValueRamp(_,_,_) => vr.toControlPointList(startFrame)
    }

  def endFrame : Int =
    event match {
      case ValueChange(_) => startFrame
      case vr : ValueRamp => startFrame + vr.duration
    }

  def endVal : Double =
    event match {
      case vc @ ValueChange(_) => vc.value
      case vr : ValueRamp => vr.endVal
    }
}

abstract class CapsuleEvent() { // "Gesture" a better name maybe?
  def valueAtRelativeFrame(relativeFrame : Int) : Double
}

case class ValueChange(val value : Double) extends CapsuleEvent {
  override def valueAtRelativeFrame(relativeFrame : Int): Double = value
}

// TODO: Break this into 4 component TimedEvents & give those to constructor?
case class ADSRCurve(val attackDuration : Int, val attackPeak : Double, val decayDuration : Int, val decayRestingPoint : Double,
                    val sustainDuration : Int, val releaseDuration : Int) extends CapsuleEvent {
  override def valueAtRelativeFrame(relativeFrame : Int): Double = 0 // TODO: IMPLEMENT!!
}

case class ValueRamp(val duration : Int, val startVal : Double, val endVal : Double) extends CapsuleEvent {
  def toControlPointList(startFrame : Int) =
    List.tabulate(duration)(n => (startFrame + n, startVal + (endVal - startVal) * (n / duration)))

  override def valueAtRelativeFrame(relativeFrame : Int): Double =
    startVal + (endVal - startVal) * (relativeFrame.toDouble / duration)
}
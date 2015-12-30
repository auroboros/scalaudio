package com.scalaudio.timing

/**
  * Created by johnmcgill on 12/29/15.
  */
case class TimedEvent(val startFrame : Int, val event : CapsuleEvent){
  def toControlPointList[T] =
    event match {
      case ValueChange(v : T) => List((startFrame,v))
      case vr @ ValueRamp() => vr.toControlPointList(startFrame)
    }
}

abstract class CapsuleEvent()

case class ValueChange[T](val value : T) extends CapsuleEvent

// TODO: Break this into 4 component TimedEvents & give those to constructor?
case class ADSRCurve(val attackDuration : Int, val attackPeak : Double, val decayDuration : Int, val decayRestingPoint : Double,
                    val sustainDuration : Int, val releaseDuration : Int) extends CapsuleEvent {
  def breakIntoControlPointList = {

  }
}

case class ValueRamp(val duration : Int, val startVal : Double, val endVal : Double) extends CapsuleEvent {
  def toControlPointList(startFrame : Int) =
    List.tabulate(duration)(n => (startFrame + n, startVal + (endVal - startVal) * (n / duration)))
}
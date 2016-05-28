package com.scalaudio.core.filter

/**
  * Created by johnmcgill on 12/26/15.
  */
case class ClipFilter(lowerLimit : Double, upperLimit : Double) extends SampleIndependentControllableFilter {
  override def defaultCtrlParam: Double = 0

  override def processSample(sig: Double, ctrl: Double): Double =
    if (sig < lowerLimit) {
      lowerLimit
    } else if (sig > upperLimit){
      upperLimit
    } else sig
}
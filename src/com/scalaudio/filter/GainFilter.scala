package com.scalaudio.filter

/**
  * Created by johnmcgill on 12/19/15.
  */
case class GainFilter(val gain : Double = 1) extends SampleIndependentControllableFilter {
  override val defaultCtrlParam = gain

  override def processSample(sig : Double, ctrlGain : Double): Double =
    sig * ctrlGain
}
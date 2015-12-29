package com.scalaudio.filter

import scala.runtime.Tuple2Zipped

/**
  * Created by johnmcgill on 12/19/15.
  */
case class GainFilter(val gain : Double = 1) extends SampleIndependentControllableFilter {
  override val defaultCtrlParam = gain

  override def processSample(sig : Double, ctrl : Double): Double =
    sig * ctrl
}
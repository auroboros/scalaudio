package com.scalaudio.buffer.filter.analysis

import com.scalaudio.buffer.filter.SampleIndependentControllableFilter

/**
  * Created by johnmcgill on 1/6/16.
  */
class ZeroCrossingDetector(val threshold : Double = 0.0001) extends SampleIndependentControllableFilter {
  var armed : Boolean = true
  var count : Long = 0

  override def defaultCtrlParam: Double = threshold

  override def processSample(sig: Double, ctrlThreshold: Double): Double =
    if (sig < -ctrlThreshold) {
      armed = true
      0
    } else if (armed & (sig > ctrlThreshold)) {
      count += 1
      armed = false
      1
    } else 0
}
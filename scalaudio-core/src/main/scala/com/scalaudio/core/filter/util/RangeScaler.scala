package com.scalaudio.core.filter.util

import com.scalaudio.core.filter.Filter

/**
  * Created by johnmcgill on 12/24/15.
  */
case class RangeScaler(newLow : Double, newHigh : Double, oldLow : Double = -1, oldHigh : Double = 1) extends Filter {
  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] = {
    inBuffers map (_ map (x => (x - oldLow) * (newHigh - newLow) / (oldHigh - oldLow) + newLow ))
  }
}

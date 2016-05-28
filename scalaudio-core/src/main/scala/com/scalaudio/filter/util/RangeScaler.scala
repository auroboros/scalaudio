package com.scalaudio.filter.util

import com.scalaudio.filter.Filter

/**
  * Created by johnmcgill on 12/24/15.
  */
case class RangeScaler(val newLow : Double, val newHigh : Double, val oldLow : Double = -1, val oldHigh : Double = 1) extends Filter {
  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] = {
    inBuffers map (_ map (x => (x - oldLow) * (newHigh - newLow) / (oldHigh - oldLow) + newLow ))
  }
}

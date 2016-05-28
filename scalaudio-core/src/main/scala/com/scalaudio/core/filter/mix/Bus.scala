package com.scalaudio.core.filter.mix

import com.scalaudio.core.filter.Filter

/**
  * Simple bus to collect signal (can build arbitrary list in)
  *
  * Created by johnmcgill on 12/20/15.
  */
case class Bus() extends Filter {
  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] = inBuffers
}
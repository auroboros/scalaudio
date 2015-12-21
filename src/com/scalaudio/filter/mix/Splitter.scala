package com.scalaudio.filter.mix

import com.scalaudio.filter.Filter

/**
  * Created by johnmcgill on 12/20/15.
  */
case class Splitter(val nOutput : Int) extends Filter {
  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] = {
    if (inBuffers.size != 1) throw new Exception("Splitter accepts 1 channel only")
    List.fill(nOutput)(inBuffers(0))
  }
}
package com.scalaudio.filter.mix

import com.scalaudio.filter.Filter

/**
  * Created by johnmcgill on 12/22/15.
  */
case class StereoPanner(val initPan : Double = .5) extends Filter {
  // 0 (left) to 1 (right)
  var pan = initPan

  override def processBuffers(inBuffers: List[Array[Double]]): List[Array[Double]] = {
    if (inBuffers.size != 1) throw new Exception("Stereo Panner accepts 1 channel, not " + inBuffers.size)
    List(inBuffers.head map (_ * Math.sqrt(1-pan)), inBuffers.head map (_ * Math.sqrt(pan)))
  }
}
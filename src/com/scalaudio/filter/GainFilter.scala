package com.scalaudio.filter

import com.scalaudio.unitgen.SigOut

/**
  * Created by johnmcgill on 12/19/15.
  */
class GainFilter(val gain : Double) extends Filter {
  def outputBuffer(bufferIn : Array[Double]) : Array[Double] =
    bufferIn map (x => x * gain)
}
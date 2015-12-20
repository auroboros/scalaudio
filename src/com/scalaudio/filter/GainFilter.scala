package com.scalaudio.filter

import com.scalaudio.unitgen.UnitGen

/**
  * Created by johnmcgill on 12/19/15.
  */
class GainFilter(val gain : Double) extends Filter {
  def processBuffers(inBuffers : List[Array[Double]]) : List[Array[Double]] =
    inBuffers map (_ map (x => x * gain))
}
package com.scalaudio.mix

/**
  * Created by johnmcgill on 12/20/15.
  */
trait Mixer {
  def outputBuffer(inBuffers : List[Array[Double]]) : List[Array[Double]]
}

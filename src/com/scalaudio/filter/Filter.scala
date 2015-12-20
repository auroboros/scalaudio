package com.scalaudio.filter

/**
  * Created by johnmcgill on 12/19/15.
  */
trait Filter {
  def processBuffers(inBuffers : List[Array[Double]]) : List[Array[Double]]
}
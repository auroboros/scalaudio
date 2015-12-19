package com.scalaudio.filter

/**
  * Created by johnmcgill on 12/19/15.
  */
trait Filter {
  def outputBuffer(bufferIn : Array[Double]) : Array[Double]
}

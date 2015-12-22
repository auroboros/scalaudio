package com.scalaudio.filter

import com.scalaudio.engine.BufferComputer

/**
  * Created by johnmcgill on 12/19/15.
  */
trait Filter extends BufferComputer {
  def processBuffers(inBuffers : List[Array[Double]]) : List[Array[Double]]
}
package com.scalaudio.engine

/**
  * Created by johnmcgill on 1/22/16.
  */
trait OutputEngine {
  def handleBuffer(buffers : List[Array[Double]])
}
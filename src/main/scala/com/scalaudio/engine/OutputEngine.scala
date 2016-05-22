package com.scalaudio.engine

import com.scalaudio.AudioContext

/**
  * Created by johnmcgill on 1/22/16.
  */
trait OutputEngine {
  def handleBuffer(buffers : List[Array[Double]])
}
package com.scalaudio.unitgen

import com.scalaudio.ScalaudioConfig

/**
  * Created by johnmcgill on 12/18/15.
  */
class SineGen extends SigOut with ScalaudioConfig {
  def outputBuffer : Array[Double] =
    (1 to FramesPerBuffer map (i => Math.sin(i * Math.PI * 2.0 / FramesPerBuffer))).toArray
}
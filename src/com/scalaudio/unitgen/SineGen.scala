package com.scalaudio.unitgen

import com.scalaudio.ScalaudioConfig

/**
  * Created by johnmcgill on 12/18/15.
  */
class SineGen(val initFreq : Double = 440) extends UnitGen with ScalaudioConfig {
  var freq = initFreq
  var phaseOffset = 0 // Should be in radians in case freq changes

  def outputBuffer : Array[Double] =
    (1 to FramesPerBuffer map (i => Math.sin(i * Math.PI * 2.0 / FramesPerBuffer))).toArray
}
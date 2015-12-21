package com.scalaudio.unitgen

import com.scalaudio.Config

/**
  * Created by johnmcgill on 12/18/15.
  */
class SineGen(val initFreq : Double = 440) extends UnitGen {
  var freq = initFreq
  var phaseOffset = 0 // Should be in radians in case freq changes

  def outputBuffers : List[Array[Double]] =
    List((1 to Config.FramesPerBuffer map (i =>
        Math.sin(i * Math.PI * 2.0 / Config.FramesPerBuffer)
        )).toArray)
}
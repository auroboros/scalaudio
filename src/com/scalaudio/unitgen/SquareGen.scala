package com.scalaudio.unitgen

import com.scalaudio.Config

/**
  * Created by johnmcgill on 12/21/15.
  */
class SquareGen(override val initFreq : Double = 440) extends SineGen(initFreq) {
  override def outputBuffers : List[Array[Double]] = {
    val obs = List((1 to Config.FramesPerBuffer map (i =>
      Math.sin(w * i + phi).signum.toDouble
      )).toArray)
    phi += phiInc
    obs
  }
}
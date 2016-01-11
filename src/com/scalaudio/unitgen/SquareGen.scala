package com.scalaudio.unitgen

import com.scalaudio.Config

/**
  * Created by johnmcgill on 12/21/15.
  */
class SquareGen(val initFreq : Double = 440) extends UnitOsc with UnitGen {
  setFreq(initFreq)

  override def computeBuffer : List[Array[Double]] = {
    // Could also just take output of super and map signum.toDouble (would be clearer but require 2nd traversal tho...)
    val obs = List((1 to Config.FramesPerBuffer map (i =>
      Math.sin(w * i + phi).signum.toDouble
      )).toArray)
    phi += phiInc
    obs
  }
}
package com.scalaudio.unitgen

import com.scalaudio.Config

/**
  * Created by johnmcgill on 12/21/15.
  */
class SawtoothGen(val initFreq : Double = 440) extends UnitOsc with UnitGen {
  setFreq(initFreq)

  override def computeBuffer : List[Array[Double]] = {
    val obs = List((1 to Config.FramesPerBuffer map (i =>
      (((w * i + phi) % period) / period) * 2 - 1
      )).toArray)
    phi += phiInc
    obs
  }
}

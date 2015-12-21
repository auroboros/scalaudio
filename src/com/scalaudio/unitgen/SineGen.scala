package com.scalaudio.unitgen

import com.scalaudio.Config

/**
  * Created by johnmcgill on 12/18/15.
  */
class SineGen(val initFreq : Double = 440) extends UnitGen {
  private var phi : Double = 0 // phase : should be in radians in case freq changes

  private var freq : Double = 0
  private var w : Double = 0
  private var phiInc : Double = 0

  setFreq(initFreq)

  def outputBuffers : List[Array[Double]] = {
    val obs = List((1 to Config.FramesPerBuffer map (i =>
      Math.sin(w * i + phi) // Need to remove parens around (i + phi) & calculate phi properly based on phase in radians
      )).toArray)
    phi += phiInc
    obs
  }

  def setFreq(newFreq : Double) = {
    freq = newFreq
    w = 2 * Math.PI * freq / Config.SamplingRate
    phiInc = w * Config.FramesPerBuffer
  }
}
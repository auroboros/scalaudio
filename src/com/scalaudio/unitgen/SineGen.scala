package com.scalaudio.unitgen

import com.scalaudio.Config

/**
  * Created by johnmcgill on 12/18/15.
  */
case class SineGen(val initFreq : Double = 440) extends UnitOsc with UnitGen {
  setFreq(initFreq)

  override def computeBuffer : List[Array[Double]] = {
    val obs = List((1 to Config.FramesPerBuffer map (i =>
      Math.sin(w * i + phi) // Need to remove parens around (i + phi) & calculate phi properly based on phase in radians
      )).toArray)
    phi += phiInc
    obs
  }

  def computeBufferWithControl(ctrlFreq : Double) : List[Array[Double]] = {
    setFreq(ctrlFreq) //TODO: This is a pretty bad hack (maybe...), layout of UnitGens that accept ctrl signals should be re-imagined
    computeBuffer
  }
}
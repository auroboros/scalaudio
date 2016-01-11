package com.scalaudio.unitgen

import com.scalaudio.Config

/**
  * Created by johnmcgill on 1/6/16.
  */
trait UnitOsc {
  protected var phi : Double = 0 // phase : should be in radians in case freq changes

  protected var freq : Double = 0
  protected var w : Double = 0 // Creates 0 to 2pi phaser with length of the period, essentially?
  protected var phiInc : Double = 0
  protected var period : Double = 0

  def setFreq(newFreq : Double) = {
    freq = newFreq
    period = Config.SamplingRate / freq
    w = 2 * Math.PI * freq / Config.SamplingRate
    phiInc = w * Config.FramesPerBuffer
  }
}

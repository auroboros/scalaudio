package com.scalaudio.unitgen

import com.scalaudio.Config
import com.scalaudio.syntax.Pitch

/**
  * Created by johnmcgill on 1/6/16.
  */
trait UnitOsc extends UnitGen {
  internalBuffers = List(Array.fill(Config.FramesPerBuffer)(0))

  protected var phi : Double = 0 // phase : should be in radians in case freq changes

  protected var freq : Pitch = Pitch(0)
  protected var w : Double = 0 // Creates 0 to 2pi phaser with length of the period, essentially?
  protected var phiInc : Double = 0
  protected var period : Double = 0

  def setFreq(newFreq : Pitch) = {
    freq = newFreq
    period = Config.SamplingRate / freq.toHz
    w = 2 * Math.PI * freq.toHz / Config.SamplingRate
    phiInc = w * Config.FramesPerBuffer
  }
}

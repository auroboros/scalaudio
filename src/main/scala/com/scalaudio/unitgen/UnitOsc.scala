package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.syntax.Pitch

/**
  * Created by johnmcgill on 1/6/16.
  */
abstract class UnitOsc(implicit audioContext: AudioContext) extends UnitGen {
  internalBuffers = List(Array.fill(framesPerBuffer)(0))

  def framesPerBuffer(implicit audioContext: AudioContext) = audioContext.config.FramesPerBuffer

  protected var phi : Double = 0 // phase : should be in radians in case freq changes

  protected var freq : Pitch = Pitch(0)
  protected var w : Double = 0 // Creates 0 to 2pi phaser with length of the period, essentially?
  protected var phiInc : Double = 0
  protected var period : Double = 0

  def setFreq(newFreq : Pitch)(implicit audioContext: AudioContext) = {
    freq = newFreq
    period = audioContext.config.SamplingRate / freq.toHz
    w = 2 * Math.PI * freq.toHz / audioContext.config.SamplingRate
    phiInc = w * audioContext.config.FramesPerBuffer
  }
}

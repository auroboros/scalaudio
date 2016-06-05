package com.scalaudio.buffer.unitgen

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.{Pitch, Signal}

/**
  * Created by johnmcgill on 1/6/16.
  */
case class OscillatorParams(freq : Option[Signal[Pitch]] = None, phase : Option[Signal[Double]] = None)

abstract class UnitOsc(val initFreq : Pitch, val initPhase : Double)(implicit audioContext: AudioContext) extends UnitGen {
  type UnitParams = OscillatorParams

  protected var phi : Double = 0 // phase : should be in radians in case freq changes
  protected var freq : Pitch = Pitch(0)
  protected var w : Double = 0 // Creates 0 to 2pi phaser with length of the period, essentially?
  protected var phiInc : Double = 0
  protected var period : Double = 0 // Only here for sawtooth gen??

  // State initialization
  internalBuffers = List(Array.fill(framesPerBuffer)(0))
  handleParams(OscillatorParams(Some(Left(initFreq)), Some(Left(initPhase))))

  def framesPerBuffer(implicit audioContext: AudioContext) = audioContext.config.framesPerBuffer

  def setFreq(newFreq : Pitch)(implicit audioContext: AudioContext) = {
    freq = newFreq
    period = audioContext.config.samplingRate / freq.toHz
    w = 2 * Math.PI * freq.toHz / audioContext.config.samplingRate
    phiInc = w * audioContext.config.framesPerBuffer
  }

  def handleParams(params: OscillatorParams, frame: Int = 0) = {
    params.freq foreach {
      case Left(controlFreq) => if (frame == 0) setFreq(controlFreq)
      case Right(signalFreq) => setFreq(signalFreq(frame))
    }

    params.phase foreach {
      case Left(controlPhase) => if (frame == 0) phi = controlPhase
      case Right(signalPhase) => phi = signalPhase(frame)
    }
  }
}

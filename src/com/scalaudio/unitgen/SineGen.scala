package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.syntax.{UnitParams, Pitch, PitchRichDouble}

/**
  * Created by johnmcgill on 12/18/15.
  */
case class SineGenCtrlParams(val freq : Option[Pitch] = None, val phase : Option[Double] = None) extends UnitParams

case class SineGen(val initFreq : Pitch = PitchRichDouble(440).Hz, val initPhase : Double = 0)(implicit audioContext: AudioContext) extends UnitOsc {
  setFreq(initFreq)
  phi = initPhase // TODO: scale this based on phaser ratio?

  override def computeBuffer(paramsOption : Option[UnitParams] = None) = {
    paramsOption match {
      case Some(p) => p match {
        case SineGenCtrlParams(freqOption,phaseOption) => {
          freqOption match {
            case Some(f) => setFreq(f)
            case None =>
          }
          phaseOption match {
            case Some(p) => phi = p
            case None =>
          }
        }
        case x => throw new Exception("Unhandled params " + x + "in SineGen")
      }
      case None =>
    }

    0 until audioContext.config.FramesPerBuffer foreach (i =>
      internalBuffers(0)(i) = Math.sin(w * i + phi)
    )
    phi += phiInc
  }
}
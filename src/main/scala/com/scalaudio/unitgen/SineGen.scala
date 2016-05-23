package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.syntax.{Pitch, PitchRichDouble}
import com.scalaudio.types.Signal

/**
  * Created by johnmcgill on 12/18/15.
  */
case class SineGenParams(freq : Option[Signal[Pitch]] = None, phase : Option[Signal[Double]] = None)

case class SineGen(initFreq : Pitch = PitchRichDouble(440).Hz, initPhase : Double = 0)(implicit audioContext: AudioContext) extends UnitOsc {
  type UnitParams = SineGenParams

  handleParams(SineGenParams(Some(Left(initFreq)), Some(Left(initPhase))))

  def computeBuffer(paramsOpt: Option[SineGenParams] = None) = {

    0 until audioContext.config.FramesPerBuffer foreach { i =>
      paramsOpt foreach (params => handleParams(params, i))
      internalBuffers.head(i) = Math.sin(w * i + phi)
    }
    phi += phiInc
  }

  def handleParams(params: SineGenParams, frame: Int = 0) = {
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
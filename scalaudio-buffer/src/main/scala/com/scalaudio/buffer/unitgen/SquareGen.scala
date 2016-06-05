package com.scalaudio.buffer.unitgen

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.PitchRichInt
import com.scalaudio.core.types.{Pitch, PitchRichInt}

/**
  * Created by johnmcgill on 12/21/15.
  */
class SquareGen(override val initFreq: Pitch = PitchRichInt(440).Hz,
                override val initPhase : Double = 0)(implicit audioContext: AudioContext) extends UnitOsc(initFreq, initPhase) {

  override def computeBuffer(paramsOpt: Option[UnitParams] = None) = {
    0 until audioContext.config.framesPerBuffer foreach { i =>
      paramsOpt foreach (params => handleParams(params, i))
      internalBuffers.head(i) = Math.sin(w * i + phi).signum.toDouble
    }
    phi += phiInc
  }
}
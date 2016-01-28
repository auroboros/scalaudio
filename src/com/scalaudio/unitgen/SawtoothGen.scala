package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.syntax.{UnitParams, PitchRichInt, Pitch}

/**
  * Created by johnmcgill on 12/21/15.
  */
class SawtoothGen(val initFreq : Pitch = PitchRichInt(440).Hz)(implicit audioContext: AudioContext) extends UnitOsc {
  setFreq(initFreq)

  override def computeBuffer(params : Option[UnitParams] = None) = {
    0 until audioContext.config.FramesPerBuffer foreach (i =>
      internalBuffers.head(i) = (((w * i + phi) % period) / period) * 2 - 1
    )
    phi += phiInc
  }
}
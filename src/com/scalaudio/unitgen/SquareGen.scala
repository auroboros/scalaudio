package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.syntax.{Pitch, PitchRichInt}

/**
  * Created by johnmcgill on 12/21/15.
  */
class SquareGen(val initFreq : Pitch = PitchRichInt(440).Hz)(implicit audioContext: AudioContext) extends UnitOsc {
  setFreq(initFreq)

  override def computeBuffer(params : Option[UnitGenParams] = None) = {
    0 to (audioContext.config.FramesPerBuffer - 1) foreach (i =>
      internalBuffers(0)(i) = Math.sin(w * i + phi).signum.toDouble
    )
    phi += phiInc
  }
}
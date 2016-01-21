package com.scalaudio.unitgen

import com.scalaudio.Config
import com.scalaudio.syntax.{PitchRichInt, Pitch}

/**
  * Created by johnmcgill on 12/21/15.
  */
class SquareGen(val initFreq : Pitch = PitchRichInt(440).Hz) extends UnitOsc {
  setFreq(initFreq)

  override def computeBuffer = {
    0 to (Config.FramesPerBuffer - 1) foreach (i =>
      internalBuffers(0)(i) = Math.sin(w * i + phi).signum.toDouble
    )
    phi += phiInc
  }
}
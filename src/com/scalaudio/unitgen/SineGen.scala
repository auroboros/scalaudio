package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.syntax.{Pitch, PitchRichDouble}

/**
  * Created by johnmcgill on 12/18/15.
  */
case class SineGen(val initFreq : Pitch = PitchRichDouble(440).Hz)(implicit audioContext: AudioContext) extends UnitOsc {
  setFreq(initFreq)

  override def computeBuffer = {
    0 to (audioContext.config.FramesPerBuffer - 1) foreach (i =>
      internalBuffers(0)(i) = Math.sin(w * i + phi) // Need to remove parens around (i + phi) & calculate phi properly based on phase in radians
    )
    phi += phiInc
  }

  def computeBufferWithControl(ctrlFreq : Pitch) : List[Array[Double]] = {
    setFreq(ctrlFreq) //TODO: This is a pretty bad hack (maybe...), layout of UnitGens that accept ctrl signals should be re-imagined
    computeBuffer
    internalBuffers
  }
}
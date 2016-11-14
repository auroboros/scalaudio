package com.scalaudio.buffer.unitgen

import com.scalaudio.core.AudioContext
import scalaudio.core.engine.Interleaver

/**
  * Created by johnmcgill on 12/20/15.
  */
case class LineIn()(implicit audioContext: AudioContext) extends UnitGen {
  override def computeBuffer(params : Option[UnitParams] = None) = {
    var buffer : Array[Double] = Array[Double](audioContext.config.framesPerBuffer * audioContext.config.nInChannels)
    audioContext.audioInput.read(buffer)
    internalBuffers = if (audioContext.config.nInChannels > 1) Interleaver.deinterleave(buffer, audioContext.config.nInChannels) else List(buffer)
  }
}

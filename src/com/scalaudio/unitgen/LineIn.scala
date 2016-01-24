package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.engine.Interleaver

/**
  * Created by johnmcgill on 12/20/15.
  */
class LineIn(implicit audioContext: AudioContext) extends UnitGen {
  override def computeBuffer = {
    var buffer : Array[Double] = Array[Double](audioContext.config.FramesPerBuffer * audioContext.config.NInChannels)
    audioContext.audioInput.read(buffer)
    internalBuffers = if (audioContext.config.NInChannels > 1) Interleaver.deinterleave(buffer, audioContext.config.NInChannels) else List(buffer)
  }
}
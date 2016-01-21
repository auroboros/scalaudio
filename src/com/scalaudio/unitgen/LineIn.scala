package com.scalaudio.unitgen

import com.scalaudio.engine.Interleaver
import com.scalaudio.{Config, AudioContext}

/**
  * Created by johnmcgill on 12/20/15.
  */
class LineIn extends UnitGen {
  override def computeBuffer = {
    var buffer : Array[Double] = Array[Double](Config.FramesPerBuffer * Config.NInChannels)
    AudioContext.audioInput.read(buffer)
    internalBuffers = if (Config.NInChannels > 1) Interleaver.deinterleave(buffer, Config.NInChannels) else List(buffer)
  }
}
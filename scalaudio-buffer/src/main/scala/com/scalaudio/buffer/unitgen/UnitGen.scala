package com.scalaudio.buffer.unitgen

import com.scalaudio.buffer.engine.{BufferComputer, OutputTerminal}
import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.MultichannelAudio

/**
  * Created by johnmcgill on 6/5/16.
  */
trait UnitGen extends BufferComputer with OutputTerminal {
  type UnitParams

  override def audioOut(implicit audioContext: AudioContext) = outputBuffers()

  def outputBuffers(params : Option[UnitParams] = None)(implicit audioContext: AudioContext) : MultichannelAudio = {
    if (lastComputedFrame != currentFrame) {
      computeBuffer(params)
      lastComputedFrame = currentFrame
    }
    internalBuffers
  }

  // Updates internal buffer
  def computeBuffer(params : Option[UnitParams]) : Unit
}

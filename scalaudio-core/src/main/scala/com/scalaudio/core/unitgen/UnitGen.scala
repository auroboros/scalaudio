package com.scalaudio.core.unitgen

import com.scalaudio.core.AudioContext
import com.scalaudio.core.engine.{BufferComputer, OutputTerminal}
import com.scalaudio.core.types.MultichannelAudio


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
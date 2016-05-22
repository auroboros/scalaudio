package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.engine.BufferComputer
import com.scalaudio.syntax.UnitParams
import com.scalaudio.types._


trait UnitGen extends BufferComputer {
  def outputBuffers(params : Option[UnitParams] = None)(implicit audioContext: AudioContext) : MultichannelAudio = {
    if (lastComputedFrame != currentFrame) {
      computeBuffer(params)
      lastComputedFrame = currentFrame
    }
    internalBuffers
  }

  // Updates internal buffer
  def computeBuffer(params : Option[UnitParams] = None) : Unit
}
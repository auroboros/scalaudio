package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.engine.BufferComputer
import com.scalaudio.syntax.UnitParams


trait UnitGen extends BufferComputer {
  def outputBuffers(params : Option[UnitParams] = None)(implicit audioContext: AudioContext) : List[Array[Double]] = {
    if (lastComputedFrame != currentFrame) {
      computeBuffer(params)
      lastComputedFrame = currentFrame
    }
    internalBuffers
  }

  // Updates internal buffer
  def computeBuffer(params : Option[UnitParams] = None) : Unit
}
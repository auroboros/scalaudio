package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.engine.BufferComputer

/**
  * Created by johnmcgill on 12/19/15.
  */
trait UnitGen extends BufferComputer {
  def outputBuffers(implicit audioContext: AudioContext) : List[Array[Double]] = {
    if (lastComputedFrame != currentFrame) {
      computeBuffer
      lastComputedFrame = currentFrame
    }
    internalBuffers
  }

  // Updates internal buffer
  def computeBuffer : Unit
}
package com.scalaudio.unitgen

import com.scalaudio.Config
import com.scalaudio.engine.BufferComputer

/**
  * Created by johnmcgill on 12/19/15.
  */
trait UnitGen extends BufferComputer {
  def outputBuffers : List[Array[Double]] = {
    if (lastComputedFrame != currentFrame) {
      internalBuffers = computeBuffer
      lastComputedFrame = currentFrame
    }
    internalBuffers
  }

  def computeBuffer : List[Array[Double]]
}
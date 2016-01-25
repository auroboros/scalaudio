package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.engine.BufferComputer

/**
  * Created by johnmcgill on 12/19/15.
  */
abstract class UnitGenParams

trait UnitGen extends BufferComputer {
  def outputBuffers(params : Option[UnitGenParams] = None)(implicit audioContext: AudioContext) : List[Array[Double]] = {
    if (lastComputedFrame != currentFrame) {
      computeBuffer(params)
      lastComputedFrame = currentFrame
    }
    internalBuffers
  }

  // Updates internal buffer
  def computeBuffer(params : Option[UnitGenParams] = None) : Unit
}
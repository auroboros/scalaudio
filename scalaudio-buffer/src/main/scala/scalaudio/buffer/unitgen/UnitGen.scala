package scalaudio.buffer.unitgen

import scalaudio.buffer.BufferComputer
import scalaudio.core.AudioContext
import scalaudio.core.types.MultichannelAudio

/**
  * Created by johnmcgill on 6/5/16.
  */
trait UnitGen extends BufferComputer {
  type UnitParams

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

package com.scalaudio.core.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.MultichannelAudio

/**
  * Created by johnmcgill on 12/21/15.
  */
trait BufferComputer {
  var internalBuffers : MultichannelAudio = Nil
  var lastComputedFrame = -1

  def currentFrame(implicit audioContext: AudioContext) = audioContext.State.currentBuffer
}
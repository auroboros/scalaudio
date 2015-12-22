package com.scalaudio.engine

import com.scalaudio.AudioContext

/**
  * Created by johnmcgill on 12/21/15.
  */
trait BufferComputer {
  var internalBuffers : List[Array[Double]] = Nil
  var lastComputedFrame = -1

  def currentFrame = AudioContext.State.currentFrame
}

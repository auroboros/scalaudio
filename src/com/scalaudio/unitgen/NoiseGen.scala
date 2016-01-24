package com.scalaudio.unitgen

import com.scalaudio.AudioContext

/**
  * Created by johnmcgill on 12/18/15.
  */
case class NoiseGen()(implicit audioContext: AudioContext) extends UnitGen {
  override def computeBuffer =
    0 to (audioContext.config.FramesPerBuffer - 1) foreach (i =>
      internalBuffers(0)(i) = Math.random * 2 - 1
    )
}
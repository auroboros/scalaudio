package com.scalaudio.unitgen

import com.scalaudio.AudioContext
import com.scalaudio.syntax.UnitParams

/**
  * Created by johnmcgill on 12/18/15.
  */
case class NoiseGen()(implicit audioContext: AudioContext) extends UnitGen {
  internalBuffers = List(Array.fill(audioContext.config.FramesPerBuffer)(0))

  override def computeBuffer(params : Option[UnitParams] = None) =
    0 to (audioContext.config.FramesPerBuffer - 1) foreach (i =>
      internalBuffers(0)(i) = Math.random * 2 - 1
    )
}
package com.scalaudio.core.unitgen

import com.scalaudio.core.AudioContext

/**
  * Created by johnmcgill on 12/18/15.
  */
case class NoiseGen()(implicit audioContext: AudioContext) extends UnitGen {
  internalBuffers = List(Array.fill(audioContext.config.framesPerBuffer)(0))

  override def computeBuffer(params : Option[UnitParams] = None) =
    0 until audioContext.config.framesPerBuffer foreach (i =>
      internalBuffers.head(i) = Math.random * 2 - 1
    )
}
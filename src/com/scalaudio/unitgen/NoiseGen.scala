package com.scalaudio.unitgen

import com.scalaudio.Config

/**
  * Created by johnmcgill on 12/18/15.
  */
case class NoiseGen() extends UnitGen {
  override def computeBuffer =
    0 to (Config.FramesPerBuffer - 1) foreach (i =>
      internalBuffers(0)(i) = Math.random * 2 - 1
    )
}
package com.scalaudio.unitgen

import com.scalaudio.Config

/**
  * Created by johnmcgill on 12/18/15.
  */
class NoiseGen extends UnitGen {
  def outputBuffers = List(Array.fill[Double](Config.FramesPerBuffer)(Math.random * 2 - 1))
}
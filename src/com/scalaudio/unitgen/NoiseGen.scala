package com.scalaudio.unitgen

import com.scalaudio.ScalaudioConfig

/**
  * Created by johnmcgill on 12/18/15.
  */
class NoiseGen extends UnitGen with ScalaudioConfig {
  def outputBuffer = Array.fill[Double](FramesPerBuffer)(Math.random * 2 - 1)
}
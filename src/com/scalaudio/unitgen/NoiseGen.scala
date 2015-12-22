package com.scalaudio.unitgen

import com.scalaudio.Config

/**
  * Created by johnmcgill on 12/18/15.
  */
case class NoiseGen() extends UnitGen {
  override def computeBuffer = List(Array.fill[Double](Config.FramesPerBuffer)(Math.random * 2 - 1))
}
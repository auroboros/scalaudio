package com.scalaudio.filter.analysis

import com.scalaudio.AudioContext
import com.scalaudio.filter.SampleIndependentControllableFilter

/**
  * Created by johnmcgill on 1/7/16.
  *
  * This accumulator just counts signals in window that equal 1. Should also add a window summer.
  */
class Accumulator(val windowSize : Int = 1000) extends SampleIndependentControllableFilter {
  var hits : List[Int] = Nil
  override def defaultCtrlParam: Double = windowSize // In samples

  override def processSample(sig: Double, ctrlWindowSize: Double): Double = {
    val currentFrame = AudioContext.State.currentFrame
    hits.dropWhile(_ < currentFrame - ctrlWindowSize)
    if (sig == 1) hits ++ List(currentFrame)
    hits.size
  }
}
package com.scalaudio.core.filter.analysis

import com.scalaudio.core.AudioContext
import com.scalaudio.core.filter.SampleIndependentControllableFilter

/**
  * Created by johnmcgill on 1/7/16.
  *
  * This accumulator just counts signals in window that equal 1. Should also add a window summer.
  */
class Accumulator(val windowSize : Int = 1000)(implicit audioContext: AudioContext) extends SampleIndependentControllableFilter {
  var hits : List[Int] = Nil
  override def defaultCtrlParam: Double = windowSize // TODO: In samples - change to AudioDuration

  override def processSample(sig: Double, ctrlWindowSize: Double) : Double = {
    hits.dropWhile(_ < currentFrame - ctrlWindowSize)
    if (sig == 1) hits ++ List(currentFrame)
    hits.size
  }
}
package com.scalaudio.core.engine

import com.scalaudio.core.AudioContext
import com.scalaudio.core.types.AudioDuration

/**
  * Created by johnmcgill on 6/7/16.
  */
trait OutputTerminal {
  val processingRate : TimeResolution

  val outputEngines : List[OutputEngine]

  def preStart() = ()

  def start() = {
    preStart()
    outputEngines.foreach(_.start())
  }

  def stop() = outputEngines.foreach(_.stop())

  def processTick(currentTime: AudioDuration)(implicit audioContext: AudioContext)
}